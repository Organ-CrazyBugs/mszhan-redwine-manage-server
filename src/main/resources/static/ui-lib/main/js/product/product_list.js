$(function () {
    let $table = $('#table');

    let $createSubmitBtn = $('#create-submit-btn');
    let $createForm = $('#create-form');
    let $showEditBtn = $('#show-edit-btn');
    let $deleteBtn = $('#delete-btn');
    let $editForm = $('#edit-form');
    let $editSubmitBtn = $('#edit-submit-btn');
    let $createBtn = $('#create_btn');


    $table.bootstrapTable({
        url: '/product/search',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'productName', title: '产品名称'},
            {field: 'sku', title: 'SKU'},
            {field: 'generalGentPrice', title: '总代理价'},
            {field: 'gentPrice', title: '代理价'},
            {field: 'wholesalePrice', title: '批发价'},
            {field: 'retailPrice', title: '零售价'},
            {field: 'cost', title: '成本'},
            {field: 'unit', title: '单位'},
            {field: 'specification', title: '规格'},
            {field: 'level', title: '级别'},
            {field: 'productionArea', title: '产区'},
            {field: 'brandName', title: '品牌'},
            {field: 'alcoholContent', title: '酒精度'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},
            {field: 'specification', title: '规格'},


        ]
    });


    $deleteBtn.on('click', function (e){

        var data = $table.bootstrapTable('getSelections');
        if (data == null || data.length == 0){
            $.alertError('未勾选', '请先勾选数据');
        }
        var idList = new Array();
        $.each(data, function(index, val){
            idList.push(val.id);
        });

        $.confirm('确认您的操作', '是否确认删除', function(){
            $.ajax({
                url: '/agent/delete_by_ids/' + idList.join(","),
                method: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                targetBtn: $deleteBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
                success: function (data) {

                    if ($.ajaxIsFailure(data)) {

                        return;
                    }
                    $.alertSuccess('提示', '删除成功!');

                    $table.bootstrapTable('refresh');                       // 刷新列表
                    $createModal.modal('hide');    // 关闭模态框
                }
            });

        });
    });

    // 点击创建仓库按钮事件
    $createSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $createForm.serializeObject();
        if ($.isBlank($.trim(params['productName']))) {
            $.alertError('缺少参数', '产品名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['sku']))) {
            $.alertError('缺少参数', 'SKU为必填项');
            return;
        }
        if ($.isBlank($.trim(params['generalGentPrice']))) {
            $.alertError('缺少参数', '总代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['gentPrice']))) {
            $.alertError('缺少参数', '代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['wholesalePrice']))) {
            $.alertError('缺少参数', '批发价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['retailPrice']))) {
            $.alertError('缺少参数', '零售价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['cost']))) {
            $.alertError('缺少参数', '成本价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['unit']))) {
            $.alertError('缺少参数', '单位为必填项');
            return;
        }
        $.ajax({
            url: '/product/add_product',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $createSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {

                if ($.ajaxIsFailure(data)) {

                    return;
                }
                $.alertSuccess('提示', '产品创建成功!');

                $table.bootstrapTable('refresh');                       // 刷新列表
                $createModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 修改仓库信息 按钮单击事件
    $showEditBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        // 绑定表单数据
        var id = rows[0].id;
        window.open("/page/product/edit_index?id=" + id);
    });


    // 修改仓库信息 提交按钮单击事件
    $editSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $editForm.serializeObject();
        if ($.isBlank($.trim(params['productName']))) {
            $.alertError('缺少参数', '产品名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['sku']))) {
            $.alertError('缺少参数', 'SKU为必填项');
            return;
        }
        if ($.isBlank($.trim(params['generalGentPrice']))) {
            $.alertError('缺少参数', '总代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['gentPrice']))) {
            $.alertError('缺少参数', '代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['wholesalePrice']))) {
            $.alertError('缺少参数', '批发价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['retailPrice']))) {
            $.alertError('缺少参数', '零售价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['cost']))) {
            $.alertError('缺少参数', '成本价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['unit']))) {
            $.alertError('缺少参数', '单位为必填项');
            return;
        }


        $.ajax({
            url: '/product/update_product',
            method: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $editSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '修改成功!');

                $table.bootstrapTable('refresh');       // 刷新列表
                $editModal.modal('hide');    // 关闭模态框
            }
        });
    });

    $createBtn.on('click', function (){
        window.open("/page/product/create_index");
    });
});