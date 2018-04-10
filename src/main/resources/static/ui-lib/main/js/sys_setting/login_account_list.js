$(function(){
    let $table = $('#table');
    let $loginAccountCreateModal = $('#login-account-create-modal');
    let $userLoginCreateForm = $('#user-login-create-form');
    let $userLoginCreateSubmitBtn = $('#user-login-create-submit-btn');

    $table.bootstrapTable({
        url: '/api/user_login/list',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'userName', title: '登陆账号', formatter: function (val, record) {
                    if (record['sysUser'] == 'Y') {
                        return val + '&nbsp;<span class="badge badge-primary">超级管理员</span>';
                    }
                    return val;
                }},
            {field: 'personName', title: '人员名称'},
            {field: 'agentName', title: '所属代理'},
            {field: 'remark', title: '备注'},
            {field: 'status', title: '状态', formatter: function (val) {
                if (val === 'ENABLED') {
                    return '<span class="badge badge-success">启用</span>'
                } else {
                    return '<span class="badge badge-danger">禁用</span>'
                }
            }},
            {field: 'createDate', title: '创建时间'}
        ]
    });

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $loginAccountCreateModal.on('hide.bs.modal', function (e) {
        $userLoginCreateForm.reset();       // 清空模态框内表单数据
    });

    // 点击创建账号按钮事件
    $userLoginCreateSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $userLoginCreateForm.serializeObject();
        if ($.isBlank($.trim(params['agentId']))) {
            $.alertError('缺少参数', '请选择所属代理');
            return;
        }
        if ($.isBlank($.trim(params['personName']))) {
            $.alertError('缺少参数', '人员名称不能为空');
            return;
        }
        if ($.isBlank($.trim(params['userName']))) {
            $.alertError('缺少参数', '登陆账号不能为空');
            return;
        }
        if ($.isBlank($.trim(params['password']))) {
            $.alertError('缺少参数', '登陆密码不能为空');
            return;
        }

        $.ajax({
            url: '/api/user_login/create',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $userLoginCreateSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '登陆账号创建成功!');

                $table.bootstrapTable('refresh');                       // 刷新列表
                $loginAccountCreateModal.modal('hide');    // 关闭模态框
            }
        });
    });


});

