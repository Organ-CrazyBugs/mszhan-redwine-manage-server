$(function () {
    let $table = $('#table');
    let $tableQuerySubmitBtn = $('#table-query-submit-btn');
    let $warehouseCreateSubmitBtn = $('#warehouse-create-submit-btn');
    let $warehouseCreateForm = $('#warehouse-create-form');
    let $warehouseCreateModal = $('#warehouse-create-modal');

    $table.bootstrapTable({
        url: '/api/warehouse/manage/list',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '仓库名称'},
            {field: 'address', title: '仓库地址'},
            {field: 'principal', title: '仓库负责人'},
            {field: 'phone', title: '联系方式'},
            {field: 'tel', title: '电话'},
            {field: 'status', title: '仓库状态', formatter: function(val, record){
                if (val === 'ENABLED') {
                    return '<span class="badge badge-success">启用</span>'
                } else {
                    return '<span class="badge badge-danger">禁用</span>'
                }
            }},
            {field: 'createDate', title: '创建时间'},
            {field: 'remark', title: '备注'}
        ]
    });

    // 查询按钮点击事件
    $tableQuerySubmitBtn.on('click', () => $table.bootstrapTable('refresh'));

    // 点击创建仓库按钮事件
    $warehouseCreateSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $warehouseCreateForm.serializeObject();
        if ($.isBlank($.trim(params['name']))) {
            $.alertError('缺少参数', '仓库名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['principal']))) {
            $.alertError('缺少参数', '仓库负责人为必填项');
            return;
        }
        if ($.isBlank($.trim(params['phone']))) {
            $.alertError('缺少参数', '联系电话为必填项');
            return;
        }

        $.ajax({
            url: '/api/warehouse/manage/create',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $warehouseCreateSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '仓库创建成功!');

                $table.bootstrapTable('refresh');       // 刷新列表
                $warehouseCreateModal.modal('hide');    // 关闭模态框
                $warehouseCreateForm.reset();       // 清空模态框内表单数据
            }
        });
    });

});