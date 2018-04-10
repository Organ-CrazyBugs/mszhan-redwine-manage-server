$(function(){
    let $table = $('#table');
    let $warehouseInputSubmitBtn = $('#warehouse-input-submit-btn');
    let $warehouseInputForm = $('#warehouse-input-form');
    let $warehouseInputPopupModal = $('#warehouse-input-popup-modal');

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

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $warehouseInputPopupModal.on('hide.bs.modal', function (e) {
        $warehouseInputForm.reset();       // 清空模态框内表单数据
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

        let confirmMsg = $.formatString('产品SKU：<b>{1}</b> 箱数量：<b>{2}</b> 支数量：<b>{3}</b> 确认入库吗？',
            data['sku'], isNaN(boxQty) ? 0 : boxQty, isNaN(bottleQty) ? 0 : bottleQty);

        $.confirm('确认您的操作', confirmMsg, function () {
           $.ajax({
               url: '/api/inventory/input',
               method: 'POST',
               contentType: 'application/json',
               dataType: 'json',
               data: JSON.stringify(data),
               targetBtn: $warehouseInputSubmitBtn,
               success: function (data) {
                   if ($.ajaxIsFailure(data)) {
                       return;
                   }
                   $.alertSuccess('提示', '仓库创建成功!');

                   $table.bootstrapTable('refresh');
                   $warehouseInputPopupModal.modal('hide');
               }
           });
        });

    });

});

