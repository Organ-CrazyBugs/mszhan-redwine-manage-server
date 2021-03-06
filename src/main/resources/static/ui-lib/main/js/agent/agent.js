$(function () {
    let $table = $('#table');

    let $createSubmitBtn = $('#create-submit-btn');
    let $createForm = $('#create-form');
    let $createModal = $('#create-modal');

    let $enabledBtn = $('#enabled-btn');
    let $disabledBtn = $('#disabled-btn');
    let $editbalanceBtn = $('#show-editBalance-btn');
    let $showEditBtn = $('#show-edit-btn');
    let $deleteBtn = $('#delete-btn');
    let $editModal = $('#edit-modal');
    let $editBalanceModal = $('#edit-balance-modal');
    let $editForm = $('#edit-form');
    let $editBalanceForm = $('#edit-balance-form');
    let $editBalanceSubmitBtn = $("#edit-balance-submit-btn");
    let $editSubmitBtn = $('#edit-submit-btn');

    let $printUnpayBtn = $('#print-unpay-btn');
    let $printUnpayForm = $('#print-unpay-form');
    let $tableQueryForm = $('#table-query-form');

    $table.bootstrapTable({
        url: '/agent/search',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '代理名称', width:160, formatter:textShow},
            {field: 'type', title: '代理类型', formatter: function(val, record){
                    if (val === 'ADMIN') {
                        return '<span class="badge badge-success">管理员</span>'
                    } else if (val == 'GENERAL_AGENT') {
                        return '<span class="badge badge-success">总代理</span>'
                    } else if (val == 'AGENT'){
                        return '<span class="badge badge-success">代理</span>'
                    }
                },width:100},
            {field: 'tel', title: '座机号码', width:160, formatter:textShow},
            {field: 'phone', title: '联系电话',width:160,formatter:textShow},
            // {field: 'balance', title: '余额',width:100},
            {field: 'address', title: '地址',width:160, formatter:textShow},
            {field: 'creatorName', title: '创建人',width:100},
            {field: 'createDate', title: '创建时间',width:160},
            {field: 'updatorName', title: '更新人',width:160},
            {field: 'updateDate', title: '更新时间',width:160}
        ]
    });

    function textShow(value,row,index){
        var a = "<div class='hiddenOverFlowCoverShow' title='"+value+"'> "+ value +"</div>";
        return a;
    }

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $createModal.on('hide.bs.modal', function (e) {
        $createForm.reset();       // 清空模态框内表单数据
    });
    $editModal.on('hide.bs.modal', function (e) {
        $editForm.reset();       // 清空模态框内表单数据
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
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        // 绑定表单数据
        $editForm.bindData(rows[0], ["id", "name","type", "phone", "tel", "address"])

        $editModal.modal('show');
    });

    // 打印未付款订单
    $printUnpayBtn.on('click', function (event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要进行打印未付款单的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个进行打印未付款单');
            return;
        }
        var createStartDate = $tableQueryForm.find("input[name='createStartDate']").val();
        var createEndDate = $tableQueryForm.find("input[name='createEndDate']").val();

        let agentId = rows[0].id;
        $printUnpayForm.find('input[name="createStartDate"]').val(createStartDate);
        $printUnpayForm.find('input[name="createEndDate"]').val(createEndDate);
        $printUnpayForm.find('input[name="agentId"]').val(agentId);
        $printUnpayForm.submit();
    });

   $editbalanceBtn.on('click', function(event){
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        $editBalanceForm.bindData(rows[0], ["id"])

        $editBalanceModal.modal('show');
    });

    $editBalanceSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $editBalanceForm.serializeObject();
        if ($.isBlank($.trim(params['operationType']))) {
            $.alertError('缺少参数', '操作类型为必选项');
            return;
        }
        if ($.isBlank($.trim(params['paymentType']))) {
            $.alertError('缺少参数', '付款方式为必选项');
            return;
        }
        if ($.isBlank($.trim(params['balance']))) {
            $.alertError('缺少参数', '价格为必填项');
            return;
        }
        $.ajax({
            url: '/agent/update_balance',
            method: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $editBalanceSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '修改成功!');

                $table.bootstrapTable('refresh');
                $editBalanceModal.modal('hide');    // 关闭模态框
            }
        });
    });

    // 修改仓库信息 提交按钮单击事件
    $editSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $editForm.serializeObject();
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

        $.ajax({
            url: '/agent/update_agent',
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