let productList = [];

let agentType;
$(function () {
    let $createOrderProductTable = $('#create-order-product-table');
    let $selectProductPopupModal = $('#select-product-popup-modal');
    let $createOrderAddSkuBtn = $('#create-order-add-sku-btn');
    let $createOrderSubmitBtn = $('#create-order-submit-btn');
    let $orderCreatePopupForm = $('#order-create-popup-form');

    agentType = $('#contextAgentType').val();

    /*
        {1} field, 如： quantity， unitPrice， packagePrice
        {2} record id,
        {3} value
        {4} placeholder
     */
    let editTempl = '<div>' +
        '<div id="ocp_{1}_{2}" onclick="javascript: $(\'#ocp_{1}_{2}\').hide(); $(\'#ocp_edit_{1}_{2}\').show(); $(\'#ocp_val_{1}_{2}\').focus();">{3}</div>' +
        '<div id="ocp_edit_{1}_{2}" class="input-group input-group-sm" style="display: none;">' +
        '    <input id="ocp_val_{1}_{2}" type="text" value="{3}" class="form-control" placeholder="{4}" />' +
        '    <div class="input-group-prepend">' +
        '        <button onclick="createOrderProductListChange(\'{1}\', \'{4}\', \'{2}\', $(\'#ocp_val_{1}_{2}\'), $(\'#ocp_edit_{1}_{2}\'), $(\'#ocp_{1}_{2}\'))" class="btn btn-outline-secondary" type="button">确定</button>' +
        '    </div>' +
        '</div>' +
    '</div>';

    $createOrderProductTable.bootstrapTable({
        pagination: false,
        sidePagination: 'local',
        striped: false,
        formatNoMatches: function (){
            return '没有添加任何产品，请点击右上角的“添加产品”按钮添加产品。';
        },
        detailView: true,
        detailFormatter: function (index, record) {
            let tmpl = '<div class="text-right">' +
                '<a href="#" class="badge badge-info ml-2">总代理价：￥{1}</a>' +
                '<a href="#" class="badge badge-info ml-2">代理价：￥{2}</a>' +
                '<a href="#" class="badge badge-info ml-2">批发价：￥{3}</a>' +
                '<a href="#" class="badge badge-info ml-2">零售价：￥{4}</a>' +
            '</div>';
            let generalGentPrice = $.formatMoney(record['generalGentPrice']);
            let gentPrice = $.formatMoney(record['gentPrice']);
            let wholesalePrice = $.formatMoney(record['wholesalePrice']);
            let retailPrice = $.formatMoney(record['retailPrice']);

            switch (agentType) {
                case 'ADMIN':
                    return $.formatString(tmpl, generalGentPrice, gentPrice, wholesalePrice, retailPrice);
                    break;
                case 'GENERAL_AGENT':
                    return $.formatString(tmpl, '-', gentPrice, wholesalePrice, retailPrice);
                    break;
                case 'AGENT':
                    return $.formatString(tmpl, '-', '-', wholesalePrice, retailPrice);
                    break;
                default:
                    return $.formatString(tmpl, '-', '-', wholesalePrice, retailPrice);
            }
        },
        columns: [
            {field: 'sku', title: 'SKU'},
            {field: 'quantity', title: '数量', align: 'left', width: 110, formatter: function (val, record) {
                return $.formatString(editTempl, 'quantity', record.productId, val, '数量');
            }},
            {field: 'unitPrice', title: '单价', align: 'left', width: 130, formatter: function (val, record) {
                return $.formatString(editTempl, 'unitPrice', record.productId, val, '单价');
            }},
            {field: 'packagePrice', title: '包装费', align: 'left', width: 110, formatter: function (val, record) {
                return $.formatString(editTempl, 'packagePrice', record.productId, val, '包装费');
            }},
            {field: 'itemTotal', title: '小计'},
            {field: 'productName', title: '产品名称'},
            {field: 'productId', width: 50, align: 'center', title: '操作', formatter: function (val, record) {
                return '<button type="button" class="btn btn-outline-secondary btn-sm" onclick="createOrderProductRemoveItem(\''+val+'\')">删除</button>';
            }}
        ],
        data: productList
    });

    $createOrderAddSkuBtn.on('click', function () {
        $selectProductPopupModal.modal('show');
    });

    $('#order-create-popup-form').find('input[name="shipAmount"]').on('blur', function () {
        refreshAmountTotalInfo();
    });

    $createOrderSubmitBtn.on('click', function (event) {
        event.preventDefault();

        //TODO: 提交创建订单
        let params = $orderCreatePopupForm.serializeObject();
        if ($.isBlank(params['agentId'])) {
            $.alertWarning('提示', '请选择代理');
            return false;
        }

        if ($.isBlank(params['customerName'])) {
            $.alertWarning('提示', '请输入收件人');
            return false;
        }

        if ($.isBlank(params['phoneNumber'])) {
            $.alertWarning('提示', '请输入联系电话');
            return false;
        }

        if ($.isBlank(params['postalCode'])) {
            $.alertWarning('提示', '请输入邮政编码');
            return false;
        }

        if ($.isBlank(params['address'])) {
            $.alertWarning('提示', '请输入详细地址');
            return false;
        }

        if ($.isBlank(params['address'])) {
            $.alertWarning('提示', '请输入详细地址');
            return false;
        }

        if (productList.length == 0) {
            $.alertWarning('提示', '请加入产品项');
            return false;
        }

        if (!$.isBlank(params['paymentTypeId']) || !$.isBlank(params['paymentAmount'])) {
            if ($.isBlank(params['paymentTypeId'])) {
                $.alertWarning('提示', '请选择支付方式');
                return false;
            }

            if ($.isBlank(params['paymentAmount']) || isNaN(parseFloat(params['paymentAmount']))) {
                $.alertWarning('提示', '请输入正确的支付金额');
                return false;
            }
        }

        let msg = $.formatString('<b>产品个数：{1}  应付总额：{2}</b> 确认提交订单吗？', productList.length, $('#paymentTotalLabel').text());
        $.confirm('确认', msg, function (){
            params['productList'] = productList;

            $.ajax({
                url: '/api/order/create',
                method: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(params),
                targetBtn: $createOrderSubmitBtn,
                success: function (data) {
                    if ($.ajaxIsFailure(data)) {
                        return;
                    }
                    $.alertSuccess('提示', '订单创建成功!');
                    // $table.bootstrapTable('refresh');
                    $('#order-create-popup-modal').modal('hide');
                }
            });
        });
    });
});

function refreshAmountTotalInfo() {
    // 刷新各种小计部分金额
    let productAmount = 0.0;
    let packageAmount = 0.0;
    let $shipAmount = $('#order-create-popup-form').find('input[name="shipAmount"]');
    let shipAmount = parseFloat($shipAmount.val());
    if (isNaN(shipAmount)) {
        shipAmount = 0.00;
    }
    $shipAmount.val(shipAmount);

    productList.forEach((item, index) => {
        productAmount += item.quantity * item.unitPrice;
        packageAmount += item.packagePrice;
    });
    let paymentTotal = productAmount + packageAmount + shipAmount;

    $('#productAmountLabel').text($.formatMoney(productAmount.toFixed(2)));
    $('#packageAmountLabel').text($.formatMoney(packageAmount.toFixed(2)));
    $('#shipAmountLabel').text($.formatMoney(shipAmount.toFixed(2)));

    $('#paymentTotalLabel').text($.formatMoney(paymentTotal.toFixed(2)));
}

function createOrderProductRemoveItem(productId) {
    $.confirm('确认', '确认删除该产品项吗？', function () {
        for (let i = 0; i < productList.length; i++) {
            if (productList[i].productId == productId) {
                productList.splice(i, 1);
                break;
            }
        }
        console.log(productList);
        $('#create-order-product-table').bootstrapTable('load', productList);
        refreshAmountTotalInfo();
    });
}

function createOrderProductListChange(field, fieldName, recordId, valueInput, editBox, displayBox) {
    let value;
    let newValue = valueInput.val();
    if ('quantity' === field) {
        value = parseInt(newValue);
        if (isNaN(value) || value <= 0) {
            $.alertWarning('提示', '请输入正确的' + fieldName);
            return;
        }
    } else {
        value = parseFloat(newValue);
        if (isNaN(value) || value < 0) {
            $.alertWarning('提示', '请输入正确的' + fieldName);
            return;
        }
    }

    productList.forEach(function (item) {
        if (item.productId == recordId) {
            item[field] = value;

            item.itemTotal = item.quantity * item.unitPrice + item.packagePrice;
        }
    });

    $('#create-order-product-table').bootstrapTable('load', productList);
    refreshAmountTotalInfo();
    // editBox.hide();
    // displayBox.text(value).show();
    // valueInput.val(value);
}

function createOrderSelectProductCallback(products) {
    console.log(products);
    if (products) {
        products.forEach(function (item) {
            let exists = productList.filter(fItem => fItem.productId == item.productId);
            if (exists.length != 0) {
                exists.forEach(eItem => eItem.quantity++);
            } else {
                item.quantity = 1;

                // TODO: 获取初始化的价格信息
                item.packagePrice = 0;
                item.unitPrice = item.retailPrice;

                item.itemTotal = item.quantity * item.unitPrice + item.packagePrice;

                productList.push(item);
            }
        });
    }
    $('#create-order-product-table').bootstrapTable('load', productList);
    refreshAmountTotalInfo();
}