$(function () {
    let $table = $('#table');

    let $createSubmitBtn = $('#create-submit-btn');
    let $createForm = $('#create-form');
    let $createModal = $('#create-modal');

    let $enabledBtn = $('#enabled-btn');
    let $disabledBtn = $('#disabled-btn');

    let $showEditBtn = $('#show-edit-btn');
    let $editModal = $('#edit-modal');
    let $editForm = $('#edit-form');
    let $editSubmitBtn = $('#edit-submit-btn');

    $table.bootstrapTable({
        url: '/agent/search',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '代理名称'},
            {field: 'type', title: '代理类型', formatter: function(val, record){
                    if (val === 'ADMIN') {
                        return '<span class="badge badge-success">管理员</span>'
                    } else if (val == 'GENERAL_AGENT') {
                        return '<span class="badge badge-success">总代理</span>'
                    } else if (val == 'AGENT'){
                        return '<span class="badge badge-success">代理</span>'
                    }
                }},
            {field: 'tel', title: '座机号码'},
            {field: 'phone', title: '联系电话'},
            {field: 'balance', title: '余额'},
            {field: 'address', title: '地址'},
            {field: 'creatorName', title: '创建人'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updatorName', title: '更新人'},
            {field: 'updateDate', title: '更新时间'}
        ]
    });

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $createModal.on('hide.bs.modal', function (e) {
        $createForm.reset();       // 清空模态框内表单数据
    });
    $editModal.on('hide.bs.modal', function (e) {
        $editForm.reset();       // 清空模态框内表单数据
    });

    // 点击创建仓库按钮事件
    $createSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $createForm.serializeObject();
        if ($.isBlank($.trim(params['type']))) {
            $.alertError('缺少参数', '代理类型为必填项');
            return;
        }
        if ($.isBlank($.trim(params['name']))) {
            $.alertError('缺少参数', '代理名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['tel']))) {
            $.alertError('缺少参数', '座机号码为必填项');
            return;
        }
        if ($.isBlank($.trim(params['phone']))) {
            $.alertError('缺少参数', '联系电话为必填项');
            return;
        }
        if ($.isBlank($.trim(params['balance']))) {
            $.alertError('缺少参数', '余额为必填项');
            return;
        }

        $.ajax({
            url: '/agent/add_agent',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $createSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '仓库创建成功!');

                $table.bootstrapTable('refresh');                       // 刷新列表
                $createModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 修改仓库信息 按钮单击事件
    $showEditBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        // 绑定表单数据
        $editForm.bindData(rows[0], ["id", "name", "principal", "phone", "tel", "address", "remark"])

        $editModal.modal('show');
    });

    // 修改仓库信息 提交按钮单击事件
    $editSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $editForm.serializeObject();
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
            targetBtn: $editSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '仓库更新成功!');

                $table.bootstrapTable('refresh');       // 刷新列表
                $editModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 禁用/启用 仓库
    function changeStatusEvent(status){
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
                    targetBtn: status === 'ENABLED' ? $enabledBtn : $disabledBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
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

    $enabledBtn.on('click', changeStatusEvent('ENABLED'));
    $disabledBtn.on('click', changeStatusEvent('DISABLED'));
});