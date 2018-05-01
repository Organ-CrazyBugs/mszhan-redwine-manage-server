$(function () {
    let $table = $('#table');


    $table.bootstrapTable({
        url: '/api/order/list',
        tableQueryForm: '#table-query-form',
        detailView: true,
        detailFormatter: function (index, record) {
            console.log(record);

            let rowTmpl = function(){
                let orderItems = record.orderItems;
                let html = '';
                orderItems.forEach((oi, index) => {
                    html += `
                    <tr>
                      <th scope="row" class="text-center">${index+1}</th>
                      <td class="text-center">${oi.sku}</td>
                      <td class="text-right">${oi.quantity}</td>
                      <td class="text-right">￥${$.formatMoney(parseFloat(oi.unitPrice).toFixed(2))}</td>
                      <td class="text-right">￥${$.formatMoney(parseFloat(oi.packagingFee).toFixed(2))}</td>
                    </tr>
                    `;
                });
                return html;
            };
            let tmpl = `
                <table class="table table-bordered">
                  <thead>
                    <tr>
                      <th class="text-center py-2">#</th>
                      <th class="text-center py-2">产品条码</th>
                      <th class="text-center py-2">数量</th>
                      <th class="text-center py-2">售价（单价）</th>
                      <th class="text-center py-2">包装费</th>
                    </tr>
                  </thead>
                  <tbody>
                    ${rowTmpl()}
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
});