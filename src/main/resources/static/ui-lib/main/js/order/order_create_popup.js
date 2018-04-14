$(function () {
    let $createOrderProductTable = $('#create-order-product-table');
    let productList = [];

    $createOrderProductTable.bootstrapTable({
        pagination: false,
        sidePagination: 'local',
        columns: [
            {checkbox: true},
            {field: 'sku', title: '产品SKU'},
            {field: 'quantity', title: '数量'},
            {field: 'unitPrice', title: '单价'},
            {field: 'packagePrice', title: '包装费'},
            {field: 'itemTotal', title: '小计'},
            {field: 'productName', title: '产品名称'},
            {field: 'sku', title: '操作', formatter: function (val, record) {
                return '编辑';
            }}
        ],
        data: productList
    });
});