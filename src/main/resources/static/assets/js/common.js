var $MB = (function () {

    /*--------------------------------------
        Bootstrap Notify Notifications
    ---------------------------------------*/
    function _notify(message, type) {
        $.notify({
            icon: "glyphicon glyphicon-warning-sign",
            title: "",
            message: message,
            url: '',
            target: '_blank'
        }, {
            element: 'body',
            type: type,
            allow_dismiss: true,
            newest_on_top: true,
            showProgressbar: false,
            placement: {
                from: "top",
                align: "center"
            },
            offset: 20,
            spacing: 10,
            z_index: 3001,
            delay: 2500,
            timer: 1000,
            url_target: '_blank',
            mouse_over: false,
            animate: {
                enter: "animated fadeInDown",
                exit: "animated fadeOutUp"
            }
        });
    }

    // close modal
    function _closeModal(modalId) {
        $("#" + modalId).find("button.btn-hide").attr("data-dismiss", "modal").trigger('click');
    }

    // close and reset modal
    function _closeAndRestModal(modalId) {
        var $modal = $("#" + modalId);
        $modal.find("button.btn-hide").attr("data-dismiss", "modal").trigger('click');
        $modal.find("form")[0].reset();
    }

    // confirm弹窗
    function _confirm(settings, callback) {
        swal({
            text: settings.text,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: settings.confirmButtonText,
            cancelButtonText: "取消",
            allowOutsideClick: false,
            allowEscapeKey: false,
            animation: false
        }).then(callback);
    }

    /** 
     * ajax 拦截 403 状态码
     */
    $.ajaxSetup({
        statusCode: {
		    403: function (result, status, pearms) {
	            location.href = ctx;
		    }
        }
    });
    
    return {
        n_default: function (message) {
            _notify(message, "inverse");
        },
        n_info: function (message) {
            _notify(message, "info");
        },
        n_success: function (message) {
            _notify(message, "success");
        },
        n_warning: function (message) {
            _notify(message, "warning");
        },
        n_danger: function (message) {
            _notify(message, "danger");
        },
        closeModal: function (modalId) {
            _closeModal(modalId);
        },
        closeAndRestModal: function (modalId) {
            _closeAndRestModal(modalId);
        },
        confirm: function (settings, callback) {
            _confirm(settings, callback);
        }
    }
})($);