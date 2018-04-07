/**
 * 扩展JQuery Ajax
 **/
var _ajax = $.ajax;
$.ajax = function (options) {
    let originBeforeSend = options.beforeSend;
    let originComplete = options.complete;
    let originSuccess = options.success;
    let originError = options.error;

    var fn = {
        error: function (jqXHR, textStatus, errorThrown){
            console.log('执行了 error');

            if (originError){
                originError(jqXHR, textStatus, errorThrown);
            }
        },
        success: function (data, textStatus, jqXHR) {
            console.log('执行了 success');

            if (originSuccess){
                originSuccess(data, textStatus, jqXHR);
            }
        },
        complete: function ( jqXHR, textStatus ){
            console.log('执行了 complete');

            if (originComplete) {
                originComplete(jqXHR, textStatus);
            }
        },
        beforeSend: function (jqXHR, settings ) {
            console.log('执行了 beforeSend');

            if (originBeforeSend) {
                originBeforeSend(jqXHR, settings);
            }
        }
    };
    var _options = $.extend(options, fn);
    _ajax(_options);
};

/**
 * 设置Bootstrap 默认行为
 */
if ($.fn.bootstrapTable.defaults) {
    $.extend($.fn.bootstrapTable.defaults, {
        responseHandler: function (res) {
            if (res.success && res.data.rows) {
                return {
                    rows: res.data.rows,
                    total: res.data.total
                }
            } else {
                return {rows: [], total: 0}
            }
        },
        onLoadError: function() {
            console.log("加载数据失败");
        },
        queryParams: function (params) {
            if (this.tableQueryForm) {
                let formParams = $(this.tableQueryForm).serializeArray();
                for (key in formParams) {
                    params[formParams[key].name] = formParams[key].value;
                }
            }
            return params;
        },
        buttonsClass: 'outline-primary',
        striped: true,
        classes: 'table table-no-bordered',
        pagination: true,
        sidePagination: "server",
        queryParamsType : "limit"
    });
}

/**
 * 扩展JQuery 消息弹出框
 */
$.extend({
    alertInfo: function (title, text) {
        return new PNotify({
            title: title,
            text: text,
            type: 'info',
            animate: {
                animate: true,
                in_class: 'bounceIn',
                out_class: 'bounceOut'
            }
        });
    },
    alertSuccess: function (title, text) {
        return new PNotify({
            title: title,
            text: text,
            type: 'success',
            animate: {
                animate: true,
                in_class: 'bounceIn',
                out_class: 'bounceOut'
            }
        });
    },
    alertError: function (title, text) {
        return new PNotify({
            title: title,
            text: text,
            type: 'error',
            animate: {
                animate: true,
                in_class: 'bounceIn',
                out_class: 'bounceOut'
            }
        });
    },
    alertWarning: function (title, text) {
        return new PNotify({
            title: title,
            text: text,
            animate: {
                animate: true,
                in_class: 'bounceIn',
                out_class: 'bounceOut'
            }
        });
    }
});