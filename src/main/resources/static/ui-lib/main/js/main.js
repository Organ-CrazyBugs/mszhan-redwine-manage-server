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
            console.log('执行了['+options.url+'] error');

            if (originError){
                try {
                    originError(jqXHR, textStatus, errorThrown);
                } catch (err) {
                    console.error(err);
                }
            }
        },
        success: function (data, textStatus, jqXHR) {
            console.log('执行了['+options.url+'] success');
            if (!$.ajaxIsSuccess(data)) {
                let error = $.ajaxGetError(data);
                let errorMsg = '未知错误, 请联系管理员!';
                if (error && error.message) {
                    errorMsg = error.message;
                }
                $.alertError('错误', errorMsg);

                if (error && error.trace) {
                    console.warn(error.trace);
                }
            }

            if (originSuccess){
                try {
                    originSuccess(data, textStatus, jqXHR);
                } catch (err) {
                    console.error(err);
                }
            }
        },
        complete: function ( jqXHR, textStatus ){
            console.log('执行了['+options.url+'] complete');
            if (options.targetBtn) {
                options.targetLoadingIcon.remove();
                options.targetBtn.removeAttr('disabled').text(options.targetBtnText);
            }

            if (originComplete) {
                try {
                    originComplete(jqXHR, textStatus);
                } catch (err) {
                    console.error(err);
                }
            }
        },
        beforeSend: function (jqXHR, settings ) {
            console.log('执行了['+options.url+'] beforeSend');
            if (options.targetBtn) {
                options.targetLoadingIcon = $('<span><li class="fa fa-spinner fa-spin"></li>&nbsp;处理中...</span>');
                options.targetBtnText = options.targetBtn.text();
                options.targetBtn.attr('disabled', true).text('').prepend(options.targetLoadingIcon);
            }

            if (originBeforeSend) {
                try {
                    originBeforeSend(jqXHR, settings);
                } catch (err) {
                    console.error(err);
                }
            }
        }
    };
    var _options = $.extend(options, fn);
    _ajax(_options);
};

/**
 * 设置Bootstrap 默认行为
 */
if ($.fn.bootstrapTable) {
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
    },
    confirm: function (title, text, confirmEvent, cancelEvent) {
        (new PNotify({
            title: title,
            text: text,
            icon: 'fa fa-question-circle fa-2x',
            hide: false,
            addclass: 'stack-modal confirm-bg-white',
            stack: {'dir1': 'down', 'dir2': 'right', 'modal': true},
            confirm: {
                confirm: true,
                buttons: [{
                    text: '确认',
                    addClass: 'confirm-ok-btn',
                    click: function(notice) {
                        notice.remove();

                        if (confirmEvent) {
                            confirmEvent();
                        }
                    }
                }, {
                    text: '取消',
                    addClass: 'confirm-cancel-btn',
                    click: function(notice) {
                        notice.remove();

                        if (cancelEvent) {
                            cancelEvent();
                        }
                    }
                }]
            },
            buttons: {
                closer: false,
                sticker: false
            },
            history: {
                history: false
            }
        }));/*.get().on('pnotify.confirm', function() {
            if (confirmEvent) {
                confirmEvent();
            }
        }).on('pnotify.cancel', function() {
            if (cancelEvent) {
                cancelEvent();
            }
        });*/
    }
});

/**
 * 扩展JQuery
 */
$.extend({
    // 字符串操作函数
    isBlank: function (str) {
        let type = typeof str;
        if (type === 'boolean' || type === 'number') {
            return false;
        }
        return !str;
    },
    formatString: function () {
        var args = [].slice.call(arguments);
        var pattern = new RegExp('{([1-' + args.length + '])}','g');
        return String(args[0]).replace(pattern, function(match, index) { return args[index]; });
    },
    ajaxIsSuccess: function (data) {
        return data && data.success;
    },
    ajaxIsFailure: function (data) {
        return !$.ajaxIsSuccess(data);
    },
    ajaxGetError: function (data) {
        if (data && data.error) {
            return data.error;
        }
        return null;
    },
    ajaxGetData: function (data) {
        if (data && data.data) {
            return data.data;
        }
        return null;
    }
});

/**
 * 扩展JQuery serialize函数
 */
$.fn.extend({
    serializeObject: function () {
        let result = {};
        let entrys = this.serializeArray();
        entrys.forEach((item) => {
            if (item['name']) {
                result[item['name']] = item['value'];
            }
        });
        return result;
    },
    reset: function () {
        this.find('input,select,textarea').not(':button, :submit, :reset, :hidden')
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
    },
    bindData: function (data, fields) {
        if (!data || !fields || fields.length == 0) {
            return;
        }
        let _this = this;
        fields.forEach(key => {
            let val = '';
            if (data[key] !== undefined && data[key] !== null) {
                val = data[key];
            }
            let selector = $.formatString('input[name="{1}"],textarea[name="{1}"],select[name="{1}"]', key);
            _this.find(selector).val(val);
        });
    }
});


$(function () {
    $('[data-toggle="offcanvas"]').on('click', function () {
        $('.offcanvas-collapse').toggleClass('open');
    });
});
