var PB = function ($) {
    var mainClass = Class.extend({
        recordlivename: 0,
        videotypejsonMap: [],      //视频类型对象数组
        livevideojsonMap: [],      //实况流对象数组
        playbackvideojsonMap: [],  //回放流对象数组
        initOcxWindownum: 1,       //控件默认开启窗口个数
        ocxHeight: "400px",        //控件默认高度
        islocallogin: false,       //是否本地登录标志位
        iscloudlogin: false,       //是否云端登录标志位
        EVMSjsonMap: [],           //一体机下加载的所有设备集合
        cloudEVMSjsonMap: [],      //云端一体机下加载的所有设备集合
        CLOUDjsonMap: [],          //云端所有设备
        queryjsonMap: [],          //查询结果集合
        ip: null,
        port: null,
        username: null,
        password: null,
        protocol: null,
        devicetype: null,
        clouddevicetype: null,
        channelList: null,
        localchalisttable: null,     //局域网通道表格对象
        localquerytable: null,       //查询视频录像表格对象
        clouddevlisttable: null,     //云账号登录设备列表表格对象
        clouddevchllisttable: null,  //云账号登录设备通道列表表格对象
        DeviceHandle: null,          //登录设备的凭证ID
        cloudDeviceHadle: null,      //云账号设备handle
        CloudHandle: null,           //云登录账号凭证ID
        queryHandle: null,           //查询所需凭证ID
        PlayBackBeginTime: null,     //回放开始时间标志位
        PlayBackEndTime: null,       //回放结束时间标志位
        DownLoadHandle: null,        //文件下载时间标志位
        init: function () {
            this.destory_activex();
            this.initPage();
            this.initData();
            this.initEvent();
            this.testlogin();
        },
        initPage: function () {
            debugger;
            video.initOcx();
            var msg, icon;
            var retcode = top.sdk_viewer.execFunction("NetSDKSetPlayWndNum", this.initOcxWindownum);
            if (0 != retcode) {
                msg = $.lang.tip["tipinitOcxfail"];
                icon = TIPS_TYPE.FAIL;
            } else {
                msg = $.lang.tip["tipinitOcxsuc"];
                icon = TIPS_TYPE.SUCCEED;
            }
            this.initPagebtn();
            $MB.n_warning(msg);
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
            this.startVideo();

            if(type != 2) {
                // disable cloud controller
                $("#_plugin_trl_presetul button").attr("disabled", true);
            } else {
                $("#_plugin_trl_presetul button").attr("disabled", false);
            }
        },

        /*************************************** test login  **********************************/
        testlogin: function() {
            debugger;
            this.ip = "192.168.1.130";
            this.port = 80;
            this.username = "admin";
            this.password = "123456";
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
                    $MB.n_warning($.lang.tip["getlocallistfail"], TIPS_TYPE.FAIL);
                    return;
                }
            } else if (this.devicetype == deviceTypestr.EVMS) {
                SDKRet = top.sdk_viewer.execFunction("NETDEV_FindDevChnList", this.DeviceHandle, 0, 0);
                if (SDKRet == -1) {
                    $MB.n_warning($.lang.tip["getlocallistfail"], TIPS_TYPE.FAIL);
                    return;
                } else {
                    this.EVMSjsonMap = [];
                    // this.getevmsdevicelist(SDKRet);
                }
            }

        },

        /*************************************** show multi windows  **********************************/
        rendersdkviewwindow: function(num) {
            debugger;
            this.initOcxWindownum = num;
            var retcode = top.sdk_viewer.execFunction("NetSDKSetPlayWndNum", this.initOcxWindownum);
            if (0 != retcode) {
                msg = $.lang.tip["tipinitOcxfail"];
                icon = TIPS_TYPE.FAIL;
            } else {
                msg = $.lang.tip["tipinitOcxsuc"];
                icon = TIPS_TYPE.SUCCEED;
            }
        },

        /*************************************** 本地登录 相关 **********************************/
        // 局域网登录
        login: function (data) {
            debugger;
            var SDKRet = top.sdk_viewer.execFunction(pluginInterfce["NETDEV_Login"], data);
            var msg;
            var icon;
            if (-1 == SDKRet) {
                msg = $.lang.tip["tiploginfail"];
                icon = TIPS_TYPE.FAIL;
            } else {
                var result = JSON.parse(SDKRet);
                this.DeviceHandle = result.UserID;
                msg = $.lang.tip["tiploginsuc"];
                icon = TIPS_TYPE.SUCCEED;
            }
            $MB.n_warning(msg);
        },

        loginOut: function () {
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_Logout", this.DeviceHandle);
            if (SDKRet == -1) {
                $MB.n_warning($.lang.tip["userlogoutFail"], TIPS_TYPE.FAIL);
                return;
            } else {
                this.DeviceHandle = -1;
                this.n_warning($.lang.tip["userlogoutSuc"], TIPS_TYPE.SUCCEED);}
        },

        /******************************* 查询相关 *********************************/
        queryclick: function () {
            WdatePicker({
                dateFmt: 'yyyy-MM-dd HH:mm:ss'
            })
        },

        commonQuery: function () {
            var BeginTime = $("#startQuerytime").val();
            var EndTime = $("#endQuerytime").val();
            if (BeginTime == "" || EndTime == "") {
                this.msgtipshow($.lang.tip["tipinputsearchtime"], TIPS_TYPE.CONFIRM);
                return;
            }
            BeginTime = BeginTime.replace(/-/g, "/");
            EndTime = EndTime.replace(/-/g, "/");
            var vBeginTime = (new Date(BeginTime).getTime()) / 1000;
            var vEndTime = (new Date(EndTime).getTime()) / 1000;
            var channelID = $("#DevchannelID").val();
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
                this.msgtipshow("Find OK!Please Click 'Find All' button to Get File", TIPS_TYPE.SUCCEED);
            }
            else {
                this.msgtipshow("Not find", TIPS_TYPE.CONFIRM);
            }
        },

        findall: function () {
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
            } else {
                if (this.queryjsonMap.length == 0) {
                    this.msgtipshow("Not find", TIPS_TYPE.CONFIRM);
                } else {
                    this.createQuerytable();
                    this.closefind();
                }
            }
        },

        createQuerytable: function () {
            var str = '<table id="querytable" class="querytable"></table>';
            $("#querytablediv").html(str);
            var width = Number($("#queryBtn").width());
            //创建查询结果表格
            var querygridSetting = {
                datatype: "local",
                width: width,
                height: 100,
                colNames: [
                    "开始时间",
                    "结束时间"
                ],
                colModel: [
                    {name: "tBeginTime", align: "center", width: 80, sortable: false},
                    {name: "tEndTime", align: "center", width: 80, sortable: false},
                ]
            };
            this.createTable(querygridSetting, this.queryjsonMap, "querytable");
        },

        changeMStoDate: function (ms) {
            var datedata = new Date(ms);
            // return datedata;
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
                alert(jsonStr);
            }
            else {
                this.msgtipshow("Not find", TIPS_TYPE.CONFIRM);
            }
        },

        getLocalTime: function (nS) {
            return new Date(parseInt(nS) * 1000).toLocaleString().substr(0, 17)
        },

        closefind: function () {
            var msg, icon;
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_FindClose", this.queryHandle);
            if (-1 != SDKRet) {
                msg = "Find Success";
                icon = TIPS_TYPE.SUCCEED;
            } else {
                msg = "Find Fail";
                icon = TIPS_TYPE.FAIL;

            }
            this.msgtipshow(msg, icon);
        },

        playbackbytime: function () {
            debugger;
            var BeginTime = $("#startQuerytime").val();
            var EndTime = $("#endQuerytime").val();
            if (BeginTime == "" || EndTime == "") {
                this.msgtipshow($.lang.tip["tipinputsearchtime"], TIPS_TYPE.CONFIRM);
                return;
            }
            BeginTime = BeginTime.replace(/-/g, "/");
            EndTime = EndTime.replace(/-/g, "/");
            var vBeginTime = (new Date(BeginTime).getTime()) / 1000;
            var vEndTime = (new Date(EndTime).getTime()) / 1000;
            var channelID = $("#DevchannelID").val();
            var dataMap = {
                dwChannelID: channelID,
                tBeginTime: vBeginTime,
                tEndTime: vEndTime,
                dwLinkMode: Protocal.TRANSPROTOCAL_RTPTCP,
                dwFileType: EventType.ALL,
                dwPlaySpeed: 9
            };
            var jsonStr = JSON.stringify(dataMap);
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var obj = {
                streamtype: videostreamtype.playback,
                screenNum: ResourceId
            };
            this.videotypejsonMap[ResourceId] = obj;
            top.sdk_viewer.execFunction("NETDEV_StopPlayback", ResourceId);
            var retcode = top.sdk_viewer.execFunction("NETDEV_PlayBack", parseInt(ResourceId), this.DeviceHandle, jsonStr);
            if (-1 == retcode) {
                this.msgtipshow("playback fail", TIPS_TYPE.FAIL);
            }
        },

        stopplayback: function () {
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            this.videotypejsonMap[ResourceId] = null;
            var retcode = top.sdk_viewer.execFunction("NETDEV_StopPlayback", ResourceId);
            if (0 != retcode) {
                this.msgtipshow("stop fail", TIPS_TYPE.FAIL);
            }
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
                this.msgtipshow("Not find", TIPS_TYPE.FAIL);
            }
        },

        SetProgress: function () {
            debugger;
            var setprogresstime = $("#setprogresstime").val();
            if (setprogresstime == "") {
                this.msgtipshow($.lang.tip["tipinputsearchtime"], TIPS_TYPE.CONFIRM);
                return;
            }
            setprogresstime = setprogresstime.replace(/-/g, "/");
            var pullTime = parseInt((new Date(setprogresstime).getTime()) / 1000);
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var dataMap = {
                pulTime: pullTime,
                pulSpeed: 20
            };
            var jsonStr = JSON.stringify(dataMap);
            var SDKRet = top.sdk_viewer.execFunction("NETDEV_PlayBackControl", parseInt(ResourceId), PlayControl.NETDEV_PLAY_CTRL_SETPLAYTIME, jsonStr);
            if (-1 == SDKRet) {
                this.msgtipshow("Set Play Time Fail", TIPS_TYPE.FAIL);
            } else {
                this.msgtipshow("Set play Time Success", TIPS_TYPE.SUCCEED);
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
                this.msgtipshow("Resume Fail", TIPS_TYPE.FAIL);
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
                this.msgtipshow("Pause fail", TIPS_TYPE.FAIL);
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
