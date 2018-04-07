$(function(){

    $('#warehouseInputSubmitBtn').on('click', warehouseInputSubmitEvent);


    $('#warehouseInputForm').bootstrapValidator({
        excluded: [':disabled', ':hidden', ':not(:visible)'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        /**
         * 生效规则（三选一）
         * enabled 字段值有变化就触发验证
         * disabled,submitted 当点击提交时验证并展示错误信息
         */
        live: 'enabled',
        /**
         * 为每个字段指定通用错误提示语
         */
        message: '请输入正确的值',
        /**
         * 指定提交的按钮，例如：'.submitBtn' '#submitBtn'
         * 当表单验证不通过时，该按钮为disabled
         */
        submitButtons: 'button[type="submit"]',
        submitHandler: function(validator, form, submitButton) {
            console.log(form.serialize());
            alert(1);
        },
        /**
         * 为每个字段设置统一触发验证方式（也可在fields中为每个字段单独定义），默认是live配置的方式，数据改变就改变
         * 也可以指定一个或多个（多个空格隔开） 'focus blur keyup'
         */
        trigger: null,
        /**
         * Number类型  为每个字段设置统一的开始验证情况，当输入字符大于等于设置的数值后才实时触发验证
         */
        threshold: null,
        /**
         * 表单域配置
         */
        fields: {
            warehouseId: {
                enabled: true,
                validators: {
                    notEmpty: {
                        message: 'The username is required'
                    }
                }
            }
            /*//多个重复
            <fieldName>: {
            //隐藏或显示 该字段的验证
            enabled: true,
                //错误提示信息
                message: 'This value is not valid',
                /!**
                 * 定义错误提示位置  值为CSS选择器设置方式
                 * 例如：'#firstNameMeg' '.lastNameMeg' '[data-stripe="exp-month"]'
                 *!/
                container: null,
                /!**
                 * 定义验证的节点，CSS选择器设置方式，可不必须是name值。
                 * 若是id，class, name属性，<fieldName>为该属性值
                 * 若是其他属性值且有中划线链接，<fieldName>转换为驼峰格式  selector: '[data-stripe="exp-month"]' =>  expMonth
                 *!/
                selector: null,
                /!**
                 * 定义触发验证方式（也可在fields中为每个字段单独定义），默认是live配置的方式，数据改变就改变
                 * 也可以指定一个或多个（多个空格隔开） 'focus blur keyup'
                 *!/
                trigger: null,
                // 定义每个验证规则
                validators: {
                //多个重复
                //官方默认验证参照  http://bv.doc.javake.cn/validators/
                // 注：使用默认前提是引入了bootstrapValidator-all.js
                // 若引入bootstrapValidator.js没有提供常用验证规则，需自定义验证规则哦
            <validatorName>: <validatorOptions>
            }*/
        }
    });
});

function warehouseInputSubmitEvent(){
    $.ajax({

    });
}