function itemPriceUpdateSaveBtnClick (btn, orderId) {
    let items = [];
    let hasError = false;
    $('input[data-order-id="'+orderId+'"]').each(function(){
        let $input = $(this);
        if (isNaN(parseFloat($input.val())) || parseFloat($input.val()) < 0) {
            hasError = true;
        }
        items.push({orderItemId: $input.attr('data-order-item-id'), price: $input.val()})
    });
    if (hasError) {
        $.alertWarning('提示', '请输入正确的商品价格');
        return;
    }
    let obj = {items, orderId: orderId};
    $.confirm('确认', `确认修改订单商品的价格吗？`, function(){
        $.ajax({
            url: '/api/order/update_item_price',
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(obj),
            targetBtn: $(btn),
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '订单商品价格修改成功!');
                $('#table').bootstrapTable('refresh');
            }
        });
    });
}

$(function () {
    let $table = $('#table');
    let $orderMarkPaymentBtn = $('#order-mark-payment-btn');
    let $orderPrintOutputBtn = $('#order-print-output-btn');
    let $orderPaymentPopupModal = $('#order-payment-popup-modal');
    let $orderMarkPaymentForm = $('#order-mark-payment-form');
    let $orderCreatePopupModal = $('#order-create-popup-modal');
    let $orderCreatePopupForm = $('#order-create-popup-form');
    let $orderCancelBtn = $('#order-cancel-btn');

    // 绑定创建仓库Modal隐藏事件， 隐藏时候清空表单内容
    $orderCreatePopupModal.on('hide.bs.modal', function (e) {
        $orderCreatePopupForm.reset();       // 清空模态框内表单数据
        resetCreateOrderProductList();
    });

    $table.bootstrapTable({
        url: '/api/order/list',
        tableQueryForm: '#table-query-form',
        detailView: true,
        detailFormatter: function (index, record) {
            let rowTmpl = function(){
                let orderItems = record.orderItems;
                let html = '';
                orderItems.forEach((oi, index) => {
                    html += `
                    <tr>
                      <th scope="row" class="text-center">${index+1}</th>
                      <td class="text-center">${oi.sku}</td>
                      <td class="text-right">${oi.quantity}</td>
                      <td class="text-right" style="width: 180px"><input class="form-control" style="text-align: right;" data-order-id="${record.orderId}" data-order-item-id="${oi.id}" value="${parseFloat(oi.unitPrice)}" /></td>
                      <td class="text-right">￥${$.formatMoney(parseFloat(oi.packagingFee).toFixed(2))}</td>
                      <td class="text-center">${oi.warehouseName}</td>
                    </tr>
                    `;
                });
                return html;
            };
            let tmpl = `
                <div class="container border-top border-left border-right bg-white">
                    <div class="row border-bottom">
                        <div class="col-2 py-2 font-weight-bold">收件人：</div>
                        <div class="col-3 py-2 text-left border-right">${record.clientName}</div>
                        <div class="col-2 py-2 font-weight-bold">联系电话：</div>
                        <div class="col-3 py-2 text-left">${record.phoneNumber}</div>
                    </div>
                    <div class="row">
                        <div class="col-2 py-2 font-weight-bold">详细地址：</div>
                        <div class="col-10 py-2 text-left">${record.address}</div>
                    </div>
                </div>
                <table class="table table-bordered bg-white">
                  <thead>
                    <tr>
                      <th class="text-center py-2">#</th>
                      <th class="text-center py-2">产品条码</th>
                      <th class="text-center py-2">数量(支)</th>
                      <th class="text-center py-2">售价（单价）</th>
                      <th class="text-center py-2">包装费</th>
                      <th class="text-center py-2">发货仓</th>
                    </tr>
                  </thead>
                  <tbody>
                    ${rowTmpl()}
                    <tr>
                        <td colspan="6" style="text-align: right;"><button type="button" class="btn btn-primary" onclick="itemPriceUpdateSaveBtnClick(this, '${record.orderId}')">保存</button></td>
                    </tr>
                  </tbody>
                </table>
            `;
            return tmpl;
        },
        columns: [
            {checkbox: true},
            {field: 'orderId', title: '订单编号'},
            {field: 'agentName', title: '所属代理'},
            {field: 'clientName', title: '客户名称'},
            {field: 'shippingFee', title: '订单运费', align: 'right', formatter: function (val) {
                    return '￥' + $.formatMoney(parseFloat(val).toFixed(2));
                }},
            {field: 'totalAmount', align: 'right', title: '订单总额', formatter: function (val) {
                    return '￥' + $.formatMoney(parseFloat(val).toFixed(2));
                }},
            {field: 'status', title: '订单状态', formatter: function (val) {
                    //(WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
                if (val === 'SHIPPED') {
                    return '<span class="badge badge-success">已发货</span>'
                } else if (val == 'WAIT_DEAL') {
                    return '<span class="badge badge-danger">待处理</span>'
                } else if (val == 'REMOVED') {
                    return '<span class="badge badge-secondary">已删除</span>'
                }
                return '-';
            }},
            {field: 'paymentStatus', title: '付款状态', formatter: function (val) {
                if (val === 'PAID') {
                    return '<span class="badge badge-success">已付款</span>'
                } else {
                    return '<span class="badge badge-danger">未付款</span>'
                }
            }},
            {field: 'remark', title: '备注'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '最近更新时间'}
        ]
    });

    $orderMarkPaymentBtn.on('click', function(btn) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0) {
            $.alertWarning('提示', '请选择需要进行标账的记录项');
            return;
        }
        if (rows.length > 1) {
            $.alertWarning('提示', '不能同时对多个订单标账，请选择单条记录。');
            return;
        }
        let data = rows[0];
        if (data['paymentStatus'] != 'UNPAID') {
            $.alertWarning('提示', '只能对未付款的订单进行标帐操作');
            return;
        }
        if (data['status'] == 'REMOVED') {
            $.alertWarning('提示', '订单已经被删除无法进行标帐');
            return;
        }
        // 标帐操作
        let orderId = data['orderId'];
        let agentName = data['agentName'];
        let personName = data['clientName'];
        let totalAmount = data['totalAmount'];
        let paymentAmount = totalAmount;
        $orderMarkPaymentForm.reset();
        $orderMarkPaymentForm.bindData({orderId, agentName, personName,
            totalAmount, paymentAmount}, ['agentName', 'orderId', 'personName', 'totalAmount', 'paymentAmount']);

        $orderPaymentPopupModal.modal('show');
    });

    $orderMarkPaymentForm.on('submit', function (event) {
        event.preventDefault();
        let params = $orderMarkPaymentForm.serializeObject();
        console.log(params);
        let totalAmount = parseFloat(params['totalAmount']);
        let paymentAmount = parseFloat(params['paymentAmount']);
        if ($.isBlank(params['paymentTypeId'])) {
            $.alertWarning('提示', '请选择支付方式');
            return false;
        }
        if (isNaN(paymentAmount) || paymentAmount <= 0) {
            $.alertWarning('提示', '请输入正确的支付金额');
            return false;
        }
        if (paymentAmount != totalAmount) {
            $.alertWarning('提示', '支付金额必须等于应付总额');
            return false;
        }
        $.confirm('确认', `确认对订单：<b>${params['orderId']}</b>，支付金额：<b>￥${paymentAmount.toFixed(2)}</b> 确认进行标帐操作吗？`, function(){
            $.ajax({
                url: '/api/order/mark_payment',
                method: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(params),
                targetBtn: $('#order-mark-payment-submit-btn'),
                success: function (data) {
                    if ($.ajaxIsFailure(data)) {
                        return;
                    }
                    $.alertSuccess('提示', '订单标帐成功!');
                    $orderPaymentPopupModal.modal('hide');
                    // 刷新订单列表
                    $table.bootstrapTable('refresh');
                }
            });
        });
        return false;
    });

    $orderPrintOutputBtn.on('click', function(btn) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0) {
            $.alertWarning('提示', '请选择需要进行打印出库单的记录项');
            return;
        }
        let orderIds = rows.map(record => record.orderId);
        $.confirm('确认', `确认打印<b>${rows.length}张订单</b>的出库单吗？<b>首次打印出库单的订单将扣减相应产品的库存数</b>。`, function(){
            // 标记订单已发货
            $.ajax({
                url: '/api/order/mark_shipped',
                method: 'POST',
                dataType: 'json',
                data: {orderIds: orderIds.join(',')},
                targetBtn: $orderPrintOutputBtn,
                success: function (data) {
                    if ($.ajaxIsFailure(data)) {
                        return;
                    }
                    $.alertSuccess('提示', '订单出库成功!');
                    // 刷新订单列表
                    $('#table').bootstrapTable('refresh');

                    // 提交支打印界面
                    let $printForm = $('#print-output-warehouse-form');
                    $printForm.find('input[name="orderIds"]').val(orderIds.join(','));
                    $printForm.submit();
                }
            });
        });
    });

    $orderCancelBtn.on('click', function(btn) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0) {
            $.alertWarning('提示', '请选择需要进行消单的记录项');
            return;
        }
        if (rows.length > 1) {
            $.alertWarning('提示', '每次只能同时消一张订单，请重新选择');
            return;
        }
        let orderId = rows[0].orderId;
        $.confirm('确认', `确认对订单号：<b>${orderId}</b> 做消单操作吗？<b>已发货订单将回滚库存数量</b>。`, function(){
            $.ajax({
                url: '/api/order/cancel',
                method: 'POST',
                dataType: 'json',
                data: {orderId},
                targetBtn: $orderCancelBtn,
                success: function (data) {
                    if ($.ajaxIsFailure(data)) {
                        return;
                    }
                    $.alertSuccess('提示', '订单消单成功!');
                    // 刷新订单列表
                    $('#table').bootstrapTable('refresh');
                }
            });
        });
    });

});