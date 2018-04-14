$(function () {
    let $createOrderProductTable = $('#create-order-product-table');
    let $selectProductPopupModal = $('#select-product-popup-modal');
    let $createOrderAddSkuBtn = $('#create-order-add-sku-btn');
    let productList = [];

    $createOrderProductTable.bootstrapTable({
        pagination: false,
        sidePagination: 'local',
        formatNoMatches: function (){
            return '没有添加任何产品，请点击右上角的“添加产品”按钮添加产品。';
        },
        columns: [
            {checkbox: true},
            {field: 'sku', title: 'SKU'},
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

    $createOrderAddSkuBtn.on('click', function () {
        $selectProductPopupModal.modal('show');
    });
});

function createOrderSelectProductCallback(products) {

    alert(1);
}