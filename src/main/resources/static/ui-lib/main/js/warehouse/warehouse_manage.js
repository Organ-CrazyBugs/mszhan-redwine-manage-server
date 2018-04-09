$(function () {
    let $table = $('#table');

    let $warehouseCreateSubmitBtn = $('#warehouse-create-submit-btn');
    let $warehouseCreateForm = $('#warehouse-create-form');
    let $warehouseCreateModal = $('#warehouse-create-modal');

    let $enabledWarehouseBtn = $('#enabled-warehouse-btn');
    let $disabledWarehouseBtn = $('#disabled-warehouse-btn');

    let $showEditWarehouseBtn = $('#show-edit-warehouse-btn');
    let $warehouseEditModal = $('#warehouse-edit-modal');
    let $warehouseEditForm = $('#warehouse-edit-form');
    let $warehouseEditSubmitBtn = $('#warehouse-edit-submit-btn');

    $table.bootstrapTable({
        url: '/api/warehouse/manage/list',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '仓库名称'},
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
            {field: 'address', title: '仓库地址'},
            {field: 'createDate', title: '创建时间'},
            {field: 'remark', title: '备注'}
        ]
    });

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $warehouseCreateModal.on('hide.bs.modal', function (e) {
        $warehouseCreateForm.reset();       // 清空模态框内表单数据
    });
    $warehouseEditModal.on('hide.bs.modal', function (e) {
        $warehouseEditForm.reset();       // 清空模态框内表单数据
    });

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

                $table.bootstrapTable('refresh');                       // 刷新列表
                $warehouseCreateModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 修改仓库信息 按钮单击事件
    $showEditWarehouseBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        // 绑定表单数据
        $warehouseEditForm.bindData(rows[0], ["id", "name", "principal", "phone", "tel", "address", "remark"])

        $warehouseEditModal.modal('show');
    });

    // 修改仓库信息 提交按钮单击事件
    $warehouseEditSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $warehouseEditForm.serializeObject();
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
            url: '/api/warehouse/manage/update',
            method: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $warehouseEditSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '仓库更新成功!');

                $table.bootstrapTable('refresh');       // 刷新列表
                $warehouseEditModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 禁用/启用 仓库
    function changeWarehouseStatusEvent(status){
        let statusName = status === 'ENABLED' ? '启用' : '禁用';
        return function () {
            let rows = $table.bootstrapTable('getSelections');
            if (rows.length <= 0) {
                $.alertWarning('提示', '请选择需要操作的记录项');
                return;
            }
            let ids = [];
            rows.forEach(item => ids.push(item.id));
            let warehouseIds = ids.join(',');

            let text = $.formatString('确认{1}已选择的{2}个仓库吗？', statusName, rows.length);
            $.confirm('请确认您的操作', text, function (){

                $.ajax({
                    url: '/api/warehouse/manage/change_status',
                    method: 'PUT',
                    // contentType: 'application/json',
                    dataType: 'json',
                    data: { warehouseIds, status },
                    targetBtn: status === 'ENABLED' ? $enabledWarehouseBtn : $disabledWarehouseBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
                    success: function (data) {
                        if ($.ajaxIsFailure(data)) {
                            return;
                        }
                        $.alertSuccess('提示', '仓库状态修改成功!');

                        $table.bootstrapTable('refresh');                       // 刷新列表
                    }
                });
            });
        }
    };

    $enabledWarehouseBtn.on('click', changeWarehouseStatusEvent('ENABLED'));
    $disabledWarehouseBtn.on('click', changeWarehouseStatusEvent('DISABLED'));
});