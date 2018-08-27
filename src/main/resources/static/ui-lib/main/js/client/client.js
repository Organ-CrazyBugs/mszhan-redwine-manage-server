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
        url: '/client/search',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '客户名称', width:160, formatter:textShow},
            {field: 'phoneNumber', title: '客户联系电话',width:100, formatter:textShow},
            {field: 'postalCode', title: '邮政编码',width:100},
            {field: 'address', title: '地址',width:160, formatter:textShow},
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
                url: '/client/delete_by_ids/' + idList.join(","),
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
        if ($.isBlank($.trim(params['name']))) {
            $.alertError('缺少参数', '代理类型为必填项');
            return;
        }
        if ($.isBlank($.trim(params['phoneNumber']))) {
            $.alertError('缺少参数', '代理名称为必填项');
            return;
        }
        $.ajax({
            url: '/client/add_client',
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
        $editForm.bindData(rows[0], ["id", "name","phoneNumber", "postalCode","address"])

        $editModal.modal('show');
    });




    // 修改仓库信息 提交按钮单击事件
    $editSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $editForm.serializeObject();
        if ($.isBlank($.trim(params['name']))) {
            $.alertError('缺少参数', '客户名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['phoneNumber']))) {
            $.alertError('缺少参数', '电话为必填项');
            return;
        }

        $.ajax({
            url: '/client/update_client',
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


});