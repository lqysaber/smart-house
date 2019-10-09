var Index = function ($) {
    var mainClass = Class.extend({
        videotypejsonMap: [],      //视频类型对象数组
        initOcxWindownum: 1,       //控件默认开启窗口个数
        ocxHeight: "400px",        //控件默认高度
        ip: null,
        port: null,
        username: null,
        password: null,
        protocol: null,
        devicetype: null,
        DeviceHandle: null,          //登录设备的凭证ID
        init: function () {
            debugger;
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
            // disable cloud controller
            $("#_plugin_trl_presetul button").attr("disabled", true);
            $("#_plugin_trl_cursor button").attr("disabled", true);
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

            $("#_plugin_trl_presetul button").bind("click", function (e) {
                var id = e.target.id;
                _this.presetOperation(id);

            });
        },

        selectedDevChannel: function(channelId, winObj, type, channelTxt) {
            debugger;
            $("#_plugin_ctrl_list a").removeClass("active");
            $(winObj).addClass("active");
            $("#DevchannelID").val(channelId);
            $("#DevchannelTxt").val(channelTxt);

            if(type == 1) {
//                $("#_plugin_trl_presetul button").attr("disabled", false);
//                $("#_plugin_trl_cursor button").attr("disabled", false);
            	$("#_plugin_trl_all_btn_clound").removeClass("Ashy");
                $("#_plugin_trl_all_btn_clound button").attr("disabled", false);
                
            } else {
                // disable cloud controller
//                $("#_plugin_trl_presetul button").attr("disabled", true);
//                $("#_plugin_trl_cursor button").attr("disabled", true);
            	$("#_plugin_trl_all_btn_clound").addClass("Ashy");
            	$("#_plugin_trl_all_btn_clound button").attr("disabled", true);
            }
            this.startVideo();
        },

        /*********************************云台相关********************************/
        presetOperation: function (id) {
            debugger;
            var ptzcontrolcmd;
            switch (id) {
                case "turnNW":
                    ptzcontrolcmd = PtzCmd.LEFTUP;
                    break;
                case "turnUP":
                    ptzcontrolcmd = PtzCmd.TILTUP;
                    break;
                case "turnNE":
                    ptzcontrolcmd = PtzCmd.RIGHTUP;
                    break;
                case "turnL":
                    ptzcontrolcmd = PtzCmd.PANLEFT;
                    break;
                case "turnSTOP":
                    ptzcontrolcmd = PtzCmd.ALLSTOP;
                    break;
                case "turnR":
                    ptzcontrolcmd = PtzCmd.PANRIGHT;
                    break;
                case "turnSW":
                    ptzcontrolcmd = PtzCmd.LEFTDOWN;
                    break;
                case "turnDN":
                    ptzcontrolcmd = PtzCmd.TILTDOWN;
                    break;
                case "turnSE":
                    ptzcontrolcmd = PtzCmd.RIGHTDOWN;
                    break;
            }
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var retcode = top.sdk_viewer.execFunction("NETDEV_PTZControl", parseInt(ResourceId), ptzcontrolcmd, 2);
            if (0 != retcode) {
                $MB.n_warning($.lang.tip["tippresetturnfail"]);
                return ;
            }
            // this.startVideo();
        },

        cursorOperation: function (id) {
            debugger;
            var ptzcontrolcmd;
            switch (id) {
                case "FOCUS_N":
                    ptzcontrolcmd = PtzCmd.FOCUS_N;
                    break;
                case "FOCUS_F":
                    ptzcontrolcmd = PtzCmd.FOCUS_F;
                    break;
                case "ZOOM_I":
                    ptzcontrolcmd = PtzCmd.ZOOM_I;
                    break;
                case "ZOOM_O":
                    ptzcontrolcmd = PtzCmd.ZOOM_O;
                    break;
                case "IRIS_O":
                    ptzcontrolcmd = PtzCmd.IRIS_O;
                    break;
                case "IRIS_S":
                    ptzcontrolcmd = PtzCmd.IRIS_S;
                    break;
            }
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            var retcode = top.sdk_viewer.execFunction("NETDEV_PTZControl", parseInt(ResourceId), ptzcontrolcmd, 2);
            if (0 != retcode) {
                $MB.n_warning(id + "," + ptzcontrolcmd +" is error");
                return ;
            }
            // this.startVideo();
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
            var msg;
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
                this.n_warning($.lang.tip["userlogoutSuc"]);
            }
        },

        /*************************************** 实况相关 Begin **********************************/
        //播放视频
        startVideo: function () {
            debugger;
            var channelValue = Number($("#DevchannelID").val());
            if (channelValue == "") {
                $MB.n_warning("请选择监控点");
                return;
            }
            var dataMap = {
                dwChannelID: channelValue,
                dwStreamType: LiveStream.LIVE_STREAM_INDEX_MAIN,
                dwLinkMode: Protocal.TRANSPROTOCAL_RTPTCP,
                dwFluency: 0
            };
            var jsonStr = JSON.stringify(dataMap);
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            //将窗口与流保存下来
            var obj = {
                streamtype: videostreamtype.live,
                screenNum: ResourceId
            };
            var closeRetcode = this.stopVideo(ResourceId);
            if(-1 == closeRetcode) {
                $MB.n_warning($.lang.tip["tipstopvideofail"]+",监控点为:"+$("#DevchannelTxt").val()+",视频窗口为:"+ResourceId);
                return;
            }

            var openretcode = top.sdk_viewer.execFunction("NETDEV_RealPlay", parseInt(ResourceId), this.DeviceHandle, jsonStr);
            if (-1 == openretcode) {
                $MB.n_warning($.lang.tip["tipstartvideofail"]);
            } else {
                this.videotypejsonMap[ResourceId] = obj;
                $MB.n_success($.lang.tip["tipstartvideosuc"]);
            }

        },

        //关闭视频
        stopVideo: function (ResourceId) {
            debugger;
            // var ResourceId ;
            // if(!_resourceId) {
            //     ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            // } else {
            //     ResourceId = _resourceId;
            // }

            var videoObj = this.videotypejsonMap[ResourceId];
            if(null == videoObj || !videoObj) {
                return 0;
            }

            var retcode = top.sdk_viewer.execFunction("NETDEV_StopRealPlay", parseInt(ResourceId));
            if(retcode == -1) {
                return retcode;
            }

            this.videotypejsonMap[ResourceId] = null;
            this.bodyScroll();
            return retcode;
        },
        /******************************* 实况相关 END ***************************/

        stopallvideo: function () {
            for (var i = 0; i < this.videotypejsonMap.length; i++) {
                if (this.videotypejsonMap[i] == null) {
                    continue;
                } else {
                    this.stoponevideo(this.videotypejsonMap[i]["screenNum"]);
                }
            }
        },
        /**************************停止所有播放流********************/
        stoponevideo: function (id) {
            top.sdk_viewer.execFunction("NETDEV_StopRealPlay", id);
        },

        /**************************清理SDK并关闭线程********************/
        destory_activex: function () {
            debugger;
            if (top.sdk_viewer) {
                this.stopallvideo();
                var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
                top.sdk_viewer.execFunction("NETDEV_StopRealPlay", parseInt(ResourceId));
                top.sdk_viewer.execFunction("NETDEV_Cleanup");
                delete top.sdk_viewer;
            }
        },

        /*************************** 公用方法 Begin ****************************/
        //滚动调滑动一小步，为解决关闭视频最后一帧画面问题
        bodyScroll: function () {
            debugger;
            var t = $("#playerContainer").height();
            // $('body,html').animate({'height': t + 10}, 100);
            // $('body,html').animate({'height': t - 10}, 100);
            $('#playerContainer').animate({'height': t + 10}, 100);
            $('#playerContainer').animate({'height': t - 10}, 100);
        },
        //提示信息
        msgtipshow: function (msg, icon) {
            debugger;
            MSG.msgbox.show(msg, icon, 3000, 61, "errormsg");
        }
    });
    return new mainClass();
}(jQuery);
