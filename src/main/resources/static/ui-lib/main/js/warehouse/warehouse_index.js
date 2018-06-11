$(function(){
    let $table = $('#table');
    let $warehouseInputSubmitBtn = $('#warehouse-input-submit-btn');
    let $warehouseInputForm = $('#warehouse-input-form');
    let $warehouseInputPopupModal = $('#warehouse-input-popup-modal');

    let $warehouseOutputSubmitBtn = $('#warehouse-output-submit-btn');
    let $warehouseOutputForm = $('#warehouse-output-form');
    let $warehouseOutputPopupModal = $('#warehouse-output-popup-modal');

    $table.bootstrapTable({
        url: '/api/inventory/list',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: 'productName', title: '产品名称'},
            {field: 'warehouseName', title: '所属仓库'},
            {field: 'quantity', title: '库存数量', formatter: function(val, record) {
                if (record['wineType'] && val) {
                    if (val%6 == 0){
                        return val + ' (' + parseInt(val/6) + '箱)';
                    } else {
                        return val + ' (' + parseInt(val/6) + '箱' + val%6 + '支)';
                    }
                }
                return val;
            }},
            {field: 'brandName', title: '产品品牌'},
            {field: 'sku', title: '产品条码'},
            {field: 'updateDate', title: '最近更新时间'}
        ]
    });

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $warehouseInputPopupModal.on('hide.bs.modal', function (e) {
        $warehouseInputForm.reset();       // 清空模态框内表单数据
    });
    $warehouseOutputPopupModal.on('hide.bs.modal', function (e) {
        $warehouseOutputForm.reset();       // 清空模态框内表单数据
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

        if ($.isBlank(data['inputType'])) {
            $.alertError('缺少参数', '请选择入库类型');
            return;
        }

        // let boxQty = parseInt(data['boxQty']);
        let bottleQty = parseInt(data['bottleQty']);
        // let boxQtyValid = isNaN(boxQty) || boxQty <= 0;
        let bottleQtyValid = isNaN(bottleQty) || bottleQty <= 0;

        if (/*boxQtyValid &&*/ bottleQtyValid) {
            $.alertError('缺少参数', '请输入正确的入库数量');
            return;
        }

        let confirmMsg = $.formatString('产品SKU：<b>{1}</b> 箱数量：<b>{2}</b> 支数量：<b>{3}</b> 确认入库吗？',
            data['sku'], 0, isNaN(bottleQty) ? 0 : bottleQty);
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
                   $.alertSuccess('提示', '操作成功!');

                   $table.bootstrapTable('refresh');
                   $warehouseInputPopupModal.modal('hide');
               }
           });
        });

    });

    $warehouseOutputSubmitBtn.on('click', function (event) {
        event.preventDefault();
        let data = $warehouseOutputForm.serializeObject();

        if ($.isBlank(data['warehouseId'])) {
            $.alertError('缺少参数', '请选择出库的目标仓库');
            return;
        }

        if ($.isBlank(data['sku'])) {
            $.alertError('缺少参数', '请输入产品SKU');
            return;
        }

        if ($.isBlank(data['inputType'])) {
            $.alertError('缺少参数', '请选择出库类型');
            return;
        }

        // let boxQty = parseInt(data['boxQty']);
        let bottleQty = parseInt(data['bottleQty']);
        // let boxQtyValid = isNaN(boxQty) || boxQty <= 0;
        let bottleQtyValid = isNaN(bottleQty) || bottleQty <= 0;

        if (/*boxQtyValid &&*/ bottleQtyValid) {
            $.alertError('缺少参数', '请输入正确的出库数量');
            return;
        }

        let confirmMsg = $.formatString('产品SKU：<b>{1}</b> 箱数量：<b>{2}</b> 支数量：<b>{3}</b> 确认出库吗？',
            data['sku'], 0, isNaN(bottleQty) ? 0 : bottleQty);
        $.confirm('确认您的操作', confirmMsg, function () {
            $.ajax({
                url: '/api/inventory/output',
                method: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(data),
                targetBtn: $warehouseOutputSubmitBtn,
                success: function (data) {
                    if ($.ajaxIsFailure(data)) {
                        return;
                    }
                    $.alertSuccess('提示', '操作成功!');

                    $table.bootstrapTable('refresh');
                    $warehouseOutputPopupModal.modal('hide');
                }
            });
        });

    });

});

