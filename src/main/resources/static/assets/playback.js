var PB = function ($) {
    var mainClass = Class.extend({
        videotypejsonMap: [],      //视频类型对象数组
        initOcxWindownum: 1,       //控件默认开启窗口个数
        ocxHeight: "400px",        //控件默认高度
        queryjsonMap: [],          //查询结果集合
        ip: null,
        port: null,
        username: null,
        password: null,
        protocol: null,
        devicetype: null,
        channelList: null,
        DeviceHandle: null,          //登录设备的凭证ID
        queryHandle: null,           //查询所需凭证ID
        PlayBackBeginTime: null,     //回放开始时间标志位
        PlayBackEndTime: null,       //回放结束时间标志位
        init: function () {
            this.destory_activex();
            this.initPage();
            this.initData();
            this.initEvent();
            // this.testlogin();
        },
        initPage: function () {
            debugger;
            video.initOcx();
            var retcode = top.sdk_viewer.execFunction("NetSDKSetPlayWndNum", this.initOcxWindownum);
            if (0 != retcode) {
            	$MB.n_warning($.lang.tip["tipinitOcxfail"]);
            } else {
                $MB.n_success($.lang.tip["tipinitOcxsuc"]);
            }
            this.initPagebtn();
        },

        initPagebtn: function () {
            debugger;
        },

        initloginoutbtn: function () {
            // TODO
        },

        initData: function () {
            debugger;
            // 默认关闭日志
            top.sdk_viewer.execFunction(pluginInterfce["NETDEV_SetWriteLog"], 0);
        },

        initEvent: function () {
            var _this = this;
            $("#uniview_ctrl_win1").on("click", function () {
                _this.rendersdkviewwindow(1);
            });

            $("#uniview_ctrl_win4").on("click", function () {
                _this.rendersdkviewwindow(4);
            });
        },

        selectedDevChannel: function(channelId, winObj, type) {
            debugger;
            $("#_plugin_ctrl_list a").removeClass("active");
            $(winObj).addClass("active");
            $("#DevchannelID").val(channelId);
            this.commonQuery();
        },

        /*************************************** test login  **********************************/
        testlogin: function() {
            debugger;
            this.loginnvr("192.168.1.130", 80, "admin", "123456");
        },

        loginnvr: function(_ip, _port, _username, _password) {
            debugger;
            this.ip = _ip;
            this.port = _port;
            this.username = _username;
            this.password = _password;
            this.protocol = "0";
            this.devicetype = 101;
            var loginJsonMap = {
                "szIPAddr": this.ip,
                "dwPort": this.port,
                "szUserName": this.username,
                "szPassword": this.password,
                "dwLoginProto": this.protocol,
                "dwDeviceType": this.devicetype
            };
            var loginJsonstring = JSON.stringify(loginJsonMap);
            this.login(loginJsonstring);

            var SDKRet;
            if (this.devicetype == deviceTypestr.IPC || this.devicetype == deviceTypestr.NVR) {
                SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_QueryVideoChl"], this.DeviceHandle);
                if (SDKRet == -1) {
                    $MB.n_warning($.lang.tip["getlocallistfail"]);
                    return;
                }
            } else {
                $MB.n_warning("Please contact the administrator.");
            }
        },

        /*************************************** show multi windows  **********************************/
        rendersdkviewwindow: function(num) {
            debugger;
            this.initOcxWindownum = num;
            var retcode = top.sdk_viewer.execFunction("NetSDKSetPlayWndNum", this.initOcxWindownum);
            if (0 != retcode) {
            	 $MB.n_warning($.lang.tip["tipinitOcxfail"]);
            } 
        },

        /*************************************** 本地登录 相关 **********************************/
        // 局域网登录
        login: function (data) {
            debugger;
            var SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_Login"], data);
            if (-1 == SDKRet) {
            	$MB.n_warning($.lang.tip["tiploginfail"]);
            } else {
                var result = JSON.parse(SDKRet);
                this.DeviceHandle = result.UserID;
                $MB.n_success($.lang.tip["tiploginsuc"]);
            }
        },

        loginOut: function () {
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_Logout", this.DeviceHandle);
            if (SDKRet == -1) {
                $MB.n_warning($.lang.tip["userlogoutFail"]);
                return;
            } else {
                this.DeviceHandle = -1;
                $MB.n_warning($.lang.tip["userlogoutSuc"]);}
        },

        /******************************* 查询相关 *********************************/
        commonQuery: function () {
            debugger;
            var selectedDateStr = $("#_history_date_time_start").val();
            if (selectedDateStr == "") {
                $('#_inline_datepicker_plugin').datepicker('setDate', new Date());
                // $MB.n_warning($.lang.tip["tipinputsearchtime"]);
                return;
            }
            var BeginTime =  selectedDateStr+ " 0:0:0";
            var EndTime = selectedDateStr + " 23:59:59";

            BeginTime = BeginTime.replace(/-/g, "/");
            EndTime = EndTime.replace(/-/g, "/");
            var vBeginTime = (new Date(BeginTime).getTime()) / 1000;
            var vEndTime = (new Date(EndTime).getTime()) / 1000;
            var channelID = $("#DevchannelID").val();
            if (channelID == "") {
                $MB.n_warning("Please Select the Monitor");
                return;
            }
            var dataMap = {
                szFileName: 0,
                dwChannelID: channelID,
                dwFileType: EventType.ALL,
                tBeginTime: vBeginTime,
                tEndTime: vEndTime
            };
            var jsonStr = JSON.stringify(dataMap);

            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindFile", this.DeviceHandle, jsonStr);
            if (-1 != SDKRet) {
                this.queryHandle = SDKRet;
                this.findall();
                var _videoModule = new Object({
                    _channel_id : null,
                    _video_date: null,
                    _video_map : [],
                    _initData : function (_d_channel_id, _d_video_date){
                        this._channel_id = _d_channel_id;
                        this._video_date = _d_video_date;
                    },
                    _setVideoMap: function (_d_video_map) {
                        this._video_map = _d_video_map;
                    }
                });
                _videoModule._initData(channelID, selectedDateStr);
                _videoModule._setVideoMap(this.queryjsonMap);
                this.queryjsonMap = [];

                this.createQuerytable();
                this.closefind();
                this.playbackbytime(_videoModule);
            }
            else {
                $MB.n_warning("Not find");
            }
        },

        findall: function () {
            debugger;
            var result;
            var tBeginTime;
            var tEndTime;
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindNextFile", this.queryHandle);
            if (-1 != SDKRet) {
                result = JSON.parse(SDKRet);
                tBeginTime = this.changeMStoDate(result["tBeginTime"] * 1000);
                tEndTime = this.changeMStoDate(result["tEndTime"] * 1000);
                var dateobj = {
                    tBeginTime: tBeginTime,
                    tEndTime: tEndTime
                };
                this.queryjsonMap.push(dateobj);
                this.findall();
            }
        },

        createQuerytable: function () {
            $("#querytablediv").html('<table id="querytable" class="querytable"></table>');
            var hstr = "";
            $.each(this.queryjsonMap,function(n,v) {
                hstr += "<tr>";
                hstr += "<td>"+v.tBeginTime+"</td>";
                hstr += "<td>"+v.tEndTime+"</td>";
                hstr += "</tr>";
            });
            $("#querytable").html(hstr);
        },

        changeMStoDate: function (ms) {
            var datedata = new Date(ms);
            return datedata.toLocaleString();
        },
        
        findNextfile: function () {
            debugger;
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindNextFile", this.queryHandle);
            if (-1 != SDKRet) {
                var result = JSON.parse(SDKRet);
                this.PlayBackBeginTime = result.tBeginTime;
                this.PlayBackEndTime = result.tEndTime;
                var dataMap = {
                    BeginTime: this.getLocalTime(this.PlayBackBeginTime),
                    EndTime: this.getLocalTime(this.PlayBackEndTime)
                };
                var jsonStr = JSON.stringify(dataMap);
            }
            else {
                $MB.n_warning("Not find");
            }
        },

        getLocalTime: function (nS) {
            return new Date(parseInt(nS) * 1000).toLocaleString().substr(0, 17)
        },

        closefind: function () {
            debugger;
            var msg;
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindClose", this.queryHandle);
            if (-1 != SDKRet) {
                // $MB.n_success("Find Success");
            } else {
                $MB.n_warning("Find Fail");
            }
        },

        playbackbytime: function (vmobj) {
            debugger;
            var vt = vmobj._video_map.slice(0,1);
            // vmobj._video_map.map(function(elem, index, arr) {
            //     if(index==1) {
            //         vt = elem;
            //     }
            // });

            var dataMap = {
                dwChannelID: vmobj._channel_id,
                tBeginTime: vt.tBeginTime,
                tEndTime: vt.tEndTime,
                dwLinkMode: Protocal.TRANSPROTOCAL_RTPTCP,
                dwFileType: EventType.ALL,
                dwPlaySpeed: 9
            };

            var jsonStr = JSON.stringify(dataMap);
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var spbresultcode = this.stopplayback(ResourceId);
            if (0 != spbresultcode) {
                $MB.n_warning("stop fail,NetSDKGetFocusWnd is:"+ResourceId);
                return;
            }

            var obj = {
                streamtype: videostreamtype.playback,
                screenNum: ResourceId
            };
            var retcode = top.sdk_viewer.execFunction("NETDEV_PlayBack", parseInt(ResourceId), this.DeviceHandle, jsonStr);
            if (-1 == retcode) {
                $MB.n_warning("playback fail");
            } else {
                this.videotypejsonMap[ResourceId] = vmobj;
            }
        },

        stopplayback: function (_resourceId) {
            var ResourceId ;
            if(!_resourceId) {
                ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            } else {
                ResourceId = _resourceId;
            }

            var videoObj = this.videotypejsonMap[ResourceId];
            if(null == videoObj) {
                return 0;
            }

            var retcode = top.sdk_viewer.execFunction("NETDEV_StopPlayback", ResourceId);
            if (0 != retcode) {
                // $MB.n_warning("stop fail");
            } else {
                this.videotypejsonMap[ResourceId] = null;
            }
            return retcode;
        },

        GetProgress: function () {
            debugger;
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_GETPLAYTIME, jsonStr);
            if (-1 != SDKRet) {
                var result = JSON.parse(SDKRet);
                var PlayTime = result.PlayTime;
                var showplaytime = this.changeMStoDate(PlayTime * 1000);
                $("#getprogresstime").val(showplaytime);
            }
            else {
                $MB.n_warning("Not find");
            }
        },

        SetProgress: function () {
            debugger;
            var _hh = $("#_pb_hh").val();
            var _mm = $("#_pb_mm").val();
            var _ss = $("#_pb_ss").val();
            if (_hh == "" || _mm == "" || _ss == "") {
            	$MB.n_warning($.lang.tip["tipinputsearchtime"]);
                return;
            }

            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var videoObj = this.videotypejsonMap[ResourceId];
            if(null == videoObj) {
                $MB.n_warning("No stream in the video windows:"+ResourceId);
                return;
            }

            var setprogresstime = videoObj._video_date.replace(/-/g, "/");
            var pullTime = parseInt((new Date(setprogresstime + " "+_hh+":"+_mm+":"+_ss).getTime()) / 1000);

            var dataMap = {
                pulTime: pullTime,
                pulSpeed: 20
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_SETPLAYTIME, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Set Play Time Fail");
            } else {
                $MB.n_success("Set play Time Success");
            }
        },
        resumeProgress: function () {
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_RESUME, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Resume Fail");
            }
        },

        Pauseprogress: function () {
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_PAUSE, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Pause fail");
            }
        },

        /**************************停止播放单路回放流*******************/
        stoponeplaybackvideo: function (id) {
            top.sdk_viewer.execFunction("NETDEV_StopPlayback", id);
        },

        /**************************清理SDK并关闭线程********************/
        destory_activex: function () {
            if (top.sdk_viewer) {
                var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
                top.sdk_viewer.execFunction("NETDEV_CloseSound", parseInt(ResourceId));
                top.sdk_viewer.execFunction("NETDEV_StopRealPlay", parseInt(ResourceId));
                top.sdk_viewer.execFunction("NETDEV_Cleanup");
                delete top.sdk_viewer;
            }
        },

        /*************************** 公用方法 Begin ****************************/
        //滚动调滑动一小步，为解决关闭视频最后一帧画面问题
        bodyScroll: function () {
            var t = $(window).scrollTop();
            $('body,html').animate({'scrollTop': t - 10}, 100);
            $('body,html').animate({'scrollTop': t + 10}, 100);
        },
        //提示信息
        msgtipshow: function (msg, icon) {
            debugger;
            MSG.msgbox.show(msg, icon, 3000, 61, "errormsg");
        }
    });
    return new mainClass();
}(jQuery);
