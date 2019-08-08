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
        speedBW: [4, 18, 19, 20, 21],
        speedFW: [9, 14, 15, 16, 17],
        currSpeedRT:18,                //
        nextSpeedRT:1,
        currSpeedGO:14,                //
        nextSpeedGO:1,
        init: function () {
            this.destory_activex();
            this.initPage();
            this.initData();
            this.initEvent();
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

        initData: function () {
            debugger;
            // this.setlogpath();
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

        /*************************************** login  **********************************/
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
            var loginJsonStr = JSON.stringify(loginJsonMap);
            this.login(loginJsonStr);

            if (this.devicetype == deviceTypestr.IPC || this.devicetype == deviceTypestr.NVR) {
                var SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_QueryVideoChl"], this.DeviceHandle);
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
            var channelID = $("#DevchannelID").val();
            if (channelID == "") {
                $MB.n_warning("Please Select the Monitor");
                return;
            }

            var selectedDateStr = $("#_history_date_time_start").val();
            if (selectedDateStr == "") {
                $('#_inline_datepicker_plugin').datepicker('setDate', new Date());
                return;
            }
            var BeginTime =  selectedDateStr+ " 0:0:0";
            var EndTime = selectedDateStr + " 23:59:59";

            BeginTime = BeginTime.replace(/-/g, "/");
            EndTime = EndTime.replace(/-/g, "/");
            var vBeginTime = (new Date(BeginTime).getTime()) / 1000;
            var vEndTime = (new Date(EndTime).getTime()) / 1000;

            var dataMap = {
                szFileName: 0,
                dwChannelID: channelID,
                dwFileType: EventType.ALL,
                tBeginTime: vBeginTime,
                tEndTime: vEndTime
            };

            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindFile", this.DeviceHandle, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Channel Id :"+channelID+"did not find playback video at "+selectedDateStr);
                return;
            }

            this.queryHandle = SDKRet;
            this.findall();
            var _videoModule = new Object();
            _videoModule._channel_id = channelID;
            _videoModule._video_date = selectedDateStr;
            _videoModule._video_map = this.queryjsonMap;

            this.createQuerytable();
            this.closefind();
            this.queryjsonMap = [];
            this.playbackbytime(_videoModule);
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
                    tEndTime: tEndTime,
                    rBeginTime: result["tBeginTime"],
                    rEndTime: result["tEndTime"]
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

        closefind: function () {
            debugger;
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindClose", this.queryHandle);
            if (-1 == SDKRet) {
                $MB.n_warning("Find Close Fail");
            }
        },

        playbackbytime: function (vmobj) {
            debugger;
            var _video_map = vmobj._video_map;
            var vt = _video_map.slice(0, 1);
            var dataMap = {
                dwChannelID: vmobj._channel_id,
                tBeginTime: vt[0]["rBeginTime"],
                tEndTime: vt[0]["rEndTime"],
                dwLinkMode: Protocal.TRANSPROTOCAL_RTPTCP,
                dwFileType: EventType.ALL,
                dwPlaySpeed: 9
            };

            var jsonStr = JSON.stringify(dataMap);
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var spbresultcode = this.stopplayback(ResourceId, "notips");
            if (-1 == spbresultcode) {
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

        stopplayback: function (_resourceId, _showTips) {
            var ResourceId = this.checkStreamExists(_resourceId);
            if(-1 == ResourceId) {
                if(!_showTips) {
                    $MB.n_warning("No stream in the video windows:"+ResourceId);
                }
                return 0;
            }

            var retcode = top.sdk_viewer.execFunction("NETDEV_StopPlayback", ResourceId);
            if (-1 == retcode) {
                if(!_showTips) {
                    $MB.n_warning("No stream in the video windows:"+ResourceId);
                }
                return retcode;
            }

            if(!_showTips) {
                $MB.n_success("stop playback success");
            }
            this.videotypejsonMap[ResourceId] = null;
            this.bodyScroll();
            return retcode;
        },

        getProgress: function () {
            debugger;
            var ResourceId = this.checkStreamExists();
            if(-1 == ResourceId) {
                $MB.n_warning("No stream in the video windows.");
                return ;
            }
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_GETPLAYTIME, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Please Contact The Administrator.");
                return ;
            }
            var result = JSON.parse(SDKRet);
            var pttime = result.PlayTime;
            var showplaytime = this.changeMStoDate(pttime * 1000);
            $("#getprogresstime").val(showplaytime);
        },

        setProgress: function () {
            debugger;
            var _pbvideo_time = $("#_pbvideo_progress_time").val();
            if (_pbvideo_time == "") {
                $MB.n_warning($.lang.tip["tipinputsearchtime"]);
                return;
            }

            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var videoObj = this.videotypejsonMap[ResourceId];
            if(null == videoObj) {
                $MB.n_warning("No stream in the video windows:"+ResourceId);
                return;
            }
            var _pb_datetime = videoObj._video_date + " " + _pbvideo_time;
            var setprogresstime = _pb_datetime.replace(/-/g, "/");
            var pullTime = parseInt((new Date(setprogresstime).valueOf()) / 1000);
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
            var ResourceId = this.checkStreamExists();
            if(-1 == ResourceId) {
                $MB.n_warning("No stream in the video windows.");
                return ;
            }
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_RESUME, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Resume Fail");
            } else {
                $MB.n_success("Resume Success");
            }
        },

        pauseProgress: function () {
            var ResourceId = this.checkStreamExists();
            if(-1 == ResourceId) {
                $MB.n_warning("No stream in the video windows.");
                return ;
            }
            var dataMap = {
                pulTime: 0,
                pulSpeed: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_PAUSE, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Pause fail");
            } else {
                $MB.n_success("Pause Success");
            }
        },

        speedRT: function() {
            debugger;
            var ResourceId = this.checkStreamExists();
            if(-1 == ResourceId) {
                $MB.n_warning("No stream in the video windows.");
                return ;
            }
            var dataMap = {
                pulTime: 0,
                pulSpeed: this.currSpeedRT
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_SETPLAYSPEED, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Speed backward fail");
                return;
            }

            $MB.n_success(this.showSpeedMSG(this.currSpeedRT));

            if(this.nextSpeedRT + 1 >= this.speedBW.length) {
                this.nextSpeedRT = 0;
            } else {
                this.nextSpeedRT ++ ;
            }
            this.currSpeedRT = this.speedBW.slice(this.nextSpeedRT, this.nextSpeedRT+1)[0];
        },

        speedGO: function() {
            debugger;
            var ResourceId = this.checkStreamExists();
            if(-1 == ResourceId) {
                $MB.n_warning("No stream in the video windows.");
                return ;
            }
            var dataMap = {
                pulTime: 0,
                pulSpeed: this.currSpeedGO
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_SETPLAYSPEED, jsonStr);
            if (-1 == SDKRet) {
                $MB.n_warning("Speed forward fail");
                return;
            }

            $MB.n_success(this.showSpeedMSG(this.currSpeedGO));

            if(this.nextSpeedGO + 1 >= this.speedFW.length) {
                this.nextSpeedGO = 0;
            } else {
                this.nextSpeedGO ++ ;
            }
            this.currSpeedGO = this.speedFW.slice(this.nextSpeedGO, this.nextSpeedGO+1)[0];
        },

        checkStreamExists: function(_resourceId) {
            var ResourceId = this.getCurrentFocusWindows(_resourceId);
            var videoObj = this.videotypejsonMap[ResourceId];
            if(null == videoObj || !videoObj) {
                return -1;
            }
            return ResourceId;
        },

        getCurrentFocusWindows: function(_resourceId) {
            var ResourceId ;
            if(!_resourceId) {
                ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            } else {
                ResourceId = _resourceId;
            }
            return ResourceId;
        },

        stopallvideo: function () {
            for (var i = 0; i < this.videotypejsonMap.length; i++) {
                if (this.videotypejsonMap[i] == null) {
                    continue;
                } else {
                    this.stoponeplaybackvideo(this.videotypejsonMap[i]["screenNum"]);
                }
            }
        },

        /******************************* 日志相关 BEGIN ***************************/
        /**
         * 开启日志
         * @constructor
         */
        OpenLog: function () {
            var SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_SetWriteLog"], 1);
            if (-1 != SDKRet) {
                $MB.n_success($.lang.tip["tiplogOpensuc"]);
            } else {
                $MB.n_warning($.lang.tip["tiplogOpenfail"]);
            }
        },
        /**
         * 关闭日志
         * @constructor
         */
        CloseLog: function () {
            var SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_SetWriteLog"], 0);
            if (-1 != SDKRet) {
                $MB.n_success($.lang.tip["tiplogClosesuc"]);
            } else {
                $MB.n_warning($.lang.tip["tiplogClosefail"]);
            }
        },

        setlogpath: function () {
            var pathurl = "D:\\sdklog";
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_SetLogPath", pathurl);
            if (SDKRet != -1) {
                $MB.n_success($.lang.tip["tiplogClosesuc"]);
            } else {
                $MB.n_warning($.lang.tip["tiplogClosefail"]);
            }
        },
        /******************************* 日志相关 END ***************************/


        /**************************停止播放单路回放流*******************/
        stoponeplaybackvideo: function (id) {
            top.sdk_viewer.execFunction("NETDEV_StopPlayback", id);
        },

        /**************************清理SDK并关闭线程********************/
        destory_activex: function () {
            if (top.sdk_viewer) {
                // this.stopallvideo();
                var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
                top.sdk_viewer.execFunction("NETDEV_StopPlayback", parseInt(ResourceId));
                top.sdk_viewer.execFunction("NETDEV_Cleanup");
                delete top.sdk_viewer;
            }
        },

        /*************************** 公用方法 Begin ****************************/
        //滚动调滑动一小步，为解决关闭视频最后一帧画面问题
        bodyScroll: function () {
            var t = $("playerContainer").height();
            $('body,html').animate({'height': t + 10}, 100);
            $('body,html').animate({'height': t - 10}, 100);
        },
        //提示信息
        msgtipshow: function (msg, icon) {
            debugger;
            MSG.msgbox.show(msg, icon, 3000, 61, "errormsg");
        },

        showSpeedMSG: function (num) {
            var msg ;
            switch (num) {
              case 18:
                  msg = "2倍速后退播放  ";
                  break;
              case 19:
                  msg = "4倍速后退播放   ";
                  break;
              case 20:
                  msg = "8倍速后退播放   ";
                  break;
              case 21:
                  msg = "16倍速后退播放   ";
                  break;
              case 4:
                  msg = "正常速度后退播放";
                  break;
              case 9:
                  msg = "正常速度前进播放";
                  break;
              case 14:
                  msg = "2倍速前进播放   ";
                  break;
              case 15:
                  msg = "4倍速前进播放   ";
                  break;
              case 16:
                  msg = "8倍速前进播放   ";
                  break;
              case 17:
                  msg = "16倍速前进播放  ";
                  break;
          }
          return msg;
        },

        changeMStoDate: function (ms) {
            var datedata = new Date(ms);
            return datedata.toLocaleString();
        }
    });
    return new mainClass();
}(jQuery);
