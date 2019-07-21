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
            // this.msgtipshow(msg, icon);
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

            $("#_plugin_trl_presetul button").bind("click", function (e) {
                var id = e.target.id;
                _this.presetOperation(id);

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

        /*************************************** 实况相关 Begin **********************************/
        //播放视频
        startVideo: function () {
            debugger;
            var msg;
            var icon;
            var channelValue = Number($("#DevchannelID").val());
            if (channelValue == "") {

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

            this.videotypejsonMap[ResourceId] = obj;

            top.sdk_viewer.execFunction("NETDEV_StopRealPlay", parseInt(ResourceId));
            var openretcode = top.sdk_viewer.execFunction("NETDEV_RealPlay", parseInt(ResourceId), this.DeviceHandle, jsonStr);
            if (0 != openretcode) {
                msg = $.lang.tip["tipstartvideofail"];
                icon = TIPS_TYPE.FAIL;
                $MB.n_warning(msg);
            } else {
                msg = $.lang.tip["tipstartvideosuc"];
                icon = TIPS_TYPE.SUCCEED;
                $MB.n_success(msg);
            }

        },

        //关闭视频
        stopVideo: function () {
            var msg;
            var icon;
            var ResourceId = top.sdk_viewer.execFunction("NetSDKGetFocusWnd");
            this.videotypejsonMap[ResourceId] = null;
            var retcode = top.sdk_viewer.execFunction("NETDEV_StopRealPlay", parseInt(ResourceId));
            if (0 != retcode) {
                msg = $.lang.tip["tipstopvideofail"];
                icon = TIPS_TYPE.FAIL;
            } else {
                msg = $.lang.tip ["tipstopvideosuc"];
                icon = TIPS_TYPE.SUCCEED;
            }
            $MB.n_success(msg);
        },
        /******************************* 实况相关 END ***************************/


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
