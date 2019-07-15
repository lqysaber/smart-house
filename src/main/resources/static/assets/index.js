var Index = function ($) {
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

            $("#channel_list_ws").on("click", function () {
                _this.getChannellist();
            });
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
                // $("#playerContainer").css("height", this.ocxHeight);
                //屏蔽云登录
                // $("#cloudLogin").attr("disabled", true);
            }
            $MB.n_warning(msg);
        },

        //获取通道列表
        getChannellist: function () {
            debugger;
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
            var msg, icon;
            var tableHeight;
            if (SDKRet) {
                var str = '<table id="girdTable"></table>';
                $("#girdtableDiv").html(str);
                var jsonMap = JSON.parse(SDKRet);
                var dataMap = Utils.objectClone(jsonMap);
                for (var i = 0; i < dataMap["VideoChlList"]; i++) {
                    // for (var key in dataMap["VideoChlList"][i]) {
                    //     if (key == "bPtzSupported") {
                    //
                    //     }
                    // }
                }
                var tableDatas;
                var gridSetting;
                var colmodelwidth = "80px";
                if (this.devicetype == deviceTypestr.IPC || this.devicetype == deviceTypestr.NVR) {
                    tableDatas = jsonMap["VideoChlList"];
                    if (this.devicetype == deviceTypestr.IPC) {
                        tableHeight = 300;
                    } else if (this.devicetype == deviceTypestr.NVR) {
                        tableHeight = 300;
                    }
                    gridSetting = {
                        datatype: "local",
                        width: 1200,
                        height: tableHeight,
                        colNames: [
                            "是否支持云台",
                            "通道ID",
                            "设备类型",
                            "端口号",
                            "流个数",
                            // "IP地址类型",
                            // "通道类型",
                            "通道状态",
                            // "视频输入制式",
                            "通道名称",
                            "设备型号",
                            "IP地址"
                        ],
                        colModel: [
                            {name: "bPtzSupported", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwChannelID", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwDeviceType", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwPort", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwStreamNum", align: "center", width: colmodelwidth, sortable: false},
                            // {name: "enAddressType", align: "center", width: colmodelwidth, sortable: false},
                            // {name: "enChannelType", align: "center", width: colmodelwidth, sortable: false},
                            {name: "enStatus", align: "center", width: colmodelwidth, sortable: false},
                            // {name: "enVideoFormat", align: "center", width: colmodelwidth, sortable: false},
                            {name: "szChnName", align: "center", sortable: false},
                            {name: "szDeviceModel", align: "center", sortable: false},
                            {name: "szIPAddr", align: "center", sortable: false}
                        ]
                    };
                } else {
                    tableDatas = this.EVMSjsonMap;
                    gridSetting = {
                        datatype: "local",
                        width: 1200,
                        height: 300,
                        colNames: [
                            "通道名称",
                            "是否支持云台",
                            "支持最大流个数",
                            "通道ID",
                            "通道状态"
                        ],
                        colModel: [
                            {name: "szChnName", align: "center", width: colmodelwidth, sortable: false},
                            {name: "bSupportPTZ", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwMaxStream", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwChannelID", align: "center", width: colmodelwidth, sortable: false},
                            {name: "dwChnStatus", align: "center", width: colmodelwidth, sortable: false}
                        ]
                    };
                }
                msg = $.lang.tip["getlocallistsuc"];
                icon = TIPS_TYPE.SUCCEED;
            } else {
                msg = $.lang.tip["getlocallistfail"];
                icon = TIPS_TYPE.FAIL;
            }
            if (!tableDatas) {
                return;
            }
            this.createTable(gridSetting, tableDatas, "girdTable");
            $MB.n_warning(msg, icon);
        },

        //创建表格
        createTable: function (gridSetting, data, girdid) {
            var $grid = $("#" + girdid);
            if (girdid == "girdTable") {
                this.localchalisttable = $grid;
            }
            if (girdid == "querytable") {
                this.localquerytable = $grid;
            }
            if (girdid == "clouddevgirdTable") {
                this.clouddevlisttable = $grid;
            }

            if (girdid == "clouddevchllisttable") {
                this.clouddevchllisttable = $grid;
            }

            $grid.jqGrid(gridSetting);
            $grid.jqGrid(gridSetting);
            $grid.jqGrid("clearGridData");
            for (var i = 0; i < data.length; i++) {
                $grid.jqGrid('addRowData', i + 1, data[i]);
            }
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
        //清除表格
        destoryTable: function (id) {
            $.jgrid.gridDestroy("#" + id);
        }
    });
    return new mainClass();
}(jQuery);
