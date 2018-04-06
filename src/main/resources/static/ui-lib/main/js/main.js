/**
 * 扩展JQuery Ajax
 **/
var _ajax = $.ajax;
$.ajax = function (options) {
    var fn = {
        error: function (jqXHR, textStatus, errorThrown){
            console.log('执行了 error');

            if (options.error){
                options.error(jqXHR, textStatus, errorThrown);
            }
        },
        success: function (data, textStatus, jqXHR) {
            console.log('执行了 success');

            if (options.success){
                options.success(data, textStatus, jqXHR);
            }
        },
        complete: function ( jqXHR, textStatus ){
            console.log('执行了 complete');

            if (options.complete){
                options.complete(jqXHR, textStatus);
            }
        },
        beforeSend: function (jqXHR, settings ) {
            console.log('执行了 beforeSend');

            if (options.beforeSend){
                options.beforeSend(jqXHR, settings);
            }
        }
    };
    var _options = $.extend(options, fn);
    _ajax(_options);
};