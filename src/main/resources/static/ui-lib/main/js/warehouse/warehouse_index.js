$(function(){
    let $table = $('#table');
    let $warehouseInputSubmitBtn = $('#warehouse-input-submit-btn');
    let $warehouseInputForm = $('#warehouse-input-form');

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

    // 点击入库按钮事件
    $warehouseInputSubmitBtn.on('click', function (event) {
        event.preventDefault();
        let data = $warehouseInputForm.serializeObject();

        if ($.isBlank(data['warehouseId'])) {
            $.alertError('缺少参数', '请选择入库的目标仓库');
            return;
        }

        if ($.isBlank(data['sku'])) {
            $.alertError('缺少参数', '请输入产品SKU');
            return;
        }

        if ($.isBlank(data['sku'])) {
            $.alertError('缺少参数', '请输入产品SKU');
            return;
        }

        if ($.isBlank(data['inputType'])) {
            $.alertError('缺少参数', '请选择入库类型');
            return;
        }

        let boxQty = parseInt(data['boxQty']);
        let bottleQty = parseInt(data['bottleQty']);
        let boxQtyValid = isNaN(boxQty) || boxQty <= 0;
        let bottleQtyValid = isNaN(bottleQty) || bottleQty <= 0;

        if (boxQtyValid && bottleQtyValid) {
            $.alertError('缺少参数', '请输入正确的入库数量');
            return;
        }

        let confirmMsg = $.formatString('产品SKU：<b>{1}</b> 箱数量：<b>{2}</b> 支数量：<b>{3}</b> 确认入库吗？', data['sku'], boxQty, bottleQty);
        $.confirm('确认您的操作', confirmMsg, function () {
           alert('调用后端处理');
        });

    });

});

