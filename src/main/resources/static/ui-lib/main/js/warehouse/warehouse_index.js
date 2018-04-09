$(function(){
    let $table = $('#table');

    $table.bootstrapTable({
        url: '/api/inventory/list',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'productName', title: '产品名称'},
            {field: 'warehouseName', title: '所属仓库'},
            {field: 'quantity', title: '库存数量'},
            {field: 'brandName', title: '产品品牌'},
            {field: 'sku', title: '产品条码'},
            {field: 'updateDate', title: '最近更新时间'}
        ]
    });

});

