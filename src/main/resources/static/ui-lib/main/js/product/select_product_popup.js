$(function () {
    let $table = $('#select-product-popup-table');
    $table.bootstrapTable({
        height: 450,
        url: '/api/product/selectPopupList',
        tableQueryForm: '#select-product-popup-query-form',
        columns: [
            {checkbox: true},
            {field: 'sku', title: 'SKU'},
            {field: 'productName', title: '产品名称'}
        ]
    });
});

function selectProductPopupSubmitBtnEvent(btn, callbackEvent){
    if (callbackEvent) {
        // 获取已选择的产品项
        let $table = $('#select-product-popup-table');
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length == 0) {
            $.alertWarning('提示', '请选择产品项');
            return;
        }
        try {
            callbackEvent(rows);
        } catch (e) {
            console.error(e);
        }
    }

    let modalId = $(btn).attr('data-modal-id');
    $('#'+ modalId).modal('hide');
}