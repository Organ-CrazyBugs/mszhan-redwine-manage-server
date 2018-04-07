$(function () {
    let $table = $('#table');
    let $tableQueryForm = $('#table-query-form');
    let $tableQuerySubmitBtn = $('#table-query-submit-btn');

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
            {field: 'createDate', title: '创建时间'}
        ]
    });

    $tableQuerySubmitBtn.on('click', () => $table.bootstrapTable('refresh'));
});