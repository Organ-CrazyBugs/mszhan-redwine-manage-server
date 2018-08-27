$(function () {
    function textShow(value,row,index){
        var a = "<div class='hiddenOverFlowCoverShow' title='"+value+"'> "+ value +"</div>";
        return a;
    }
    let $table = $('#select-client-popup-table');
    $table.bootstrapTable({
        height: 450,
        url: '/client/search',
        tableQueryForm: '#select-client-popup-query-form',
        columns: [
            {checkbox: true},
            {field: 'name', title: '客户名称', formatter:textShow},
            {field: 'phoneNumber', title: '联系电话', formatter:textShow},
            {field: 'postalCode', title: '邮政编码', formatter:textShow},
            {field: 'address', title: '地址', formatter:textShow}
        ]
    });
});




function selectClientPopupSubmitBtnEvent(btn, callbackEvent){
    if (callbackEvent) {
        // 获取已选择的产品项
        let $table = $('#select-client-popup-table');
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length == 0) {
            $.alertWarning('提示', '请选择客户项');
            return;
        }
        if (rows.length > 1) {
            $.alertWarning('提示', '只能选择一个客户');
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