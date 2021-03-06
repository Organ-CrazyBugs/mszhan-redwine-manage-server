let productList = [];
let selectClient = [];
let productIndex = 1;

let agentType;
$(function () {
    let $createOrderProductTable = $('#create-order-product-table');
    let $selectProductPopupModal = $('#select-product-popup-modal');
    let $selectClientPopupModal = $('#select-client-popup-modal');
    let $createOrderAddSkuBtn = $('#create-order-add-sku-btn');
    let $createOrderAddClientBtn = $('#create-order-add-client-btn');
    let $createOrderSubmitBtn = $('#create-order-submit-btn');
    let $orderCreatePopupForm = $('#order-create-popup-form');
    let $warehouseTmplBox = $('#warehouse-tmpl-box');
    let $productUnitTmplBox = $('#product-unit-tmpl-box');
    let $productGiftTmplBox = $("#product-gift-tmpl-box");

    agentType = $('#contextAgentType').val();

    /*
        {1} field, 如： quantity， unitPrice， packagePrice
        {2} record id,
        {3} value
        {4} placeholder
     */
    let editTempl = '<div>' +
        '<div id="ocp_{1}_{2}_{5}" onclick="javascript: $(\'#ocp_{1}_{2}_{5}\').hide(); $(\'#ocp_edit_{1}_{2}_{5}\').show(); $(\'#ocp_val_{1}_{2}_{5}\').focus();">{3}</div>' +
        '<div id="ocp_edit_{1}_{2}_{5}" class="input-group input-group-sm" style="display: none;">' +
        '    <input id="ocp_val_{1}_{2}_{5}" type="text" value="{3}" class="form-control" placeholder="{4}" />' +
        '    <div class="input-group-prepend">' +
        '        <button onclick="createOrderProductListChange(\'{1}\', \'{4}\', \'{2}\',\'{5}\', $(\'#ocp_val_{1}_{2}_{5}\'), $(\'#ocp_edit_{1}_{2}_{5}\'), $(\'#ocp_{1}_{2}_{5}\'))" class="btn btn-outline-secondary" type="button">确定</button>' +
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
                    return $.formatString(tmpl, '-', gentPrice, wholesalePrice, retailPrice);
                    break;
                default:
                    return $.formatString(tmpl, '-', '-', wholesalePrice, retailPrice);
            }
        },
        columns: [
            {field: 'sku', width: 150, title: 'SKU', formatter: function(val, record) {

            return '<div name="sku-'+ record.index +'">'+ val +'</div>'

            } },
            {field: 'gift', width: 80, title: '是否赠品',  formatter: function(val, record) {
                let options = $productGiftTmplBox.find('select option');
                options.removeAttr('selected');
                if (val != '') {
                    options.each(function (index, option) {
                        if ($(option).attr('value') == val) {
                            $(option).attr('selected', 'selected');
                            return false;
                        }
                    });
                }
                $productGiftTmplBox.find('select').attr('data-sku', record['sku']);
                return $productGiftTmplBox.html();
            return '<select class="d-none" ><option value="N">否</option><option value="Y">是</option></select>';

            }},
            {field: 'quantity', title: '数量', align: 'left', width: 100, formatter: function (val, record) {
                return $.formatString(editTempl, 'quantity', record.productId, val, '数量', record.index);
            }},
            {field: 'unit', width: 80, title: '单位', formatter: function(val, record) {
                if (!record['wine']) {
                    return '<div class="text-center">支</div>'
                }
                let options = $productUnitTmplBox.find('select option');
                options.removeAttr('selected');
                if (val != '') {
                    options.each(function (index, option) {
                        if ($(option).attr('value') == val) {
                            $(option).attr('selected', 'selected');
                            return false;
                        }
                    });
                }
                $productUnitTmplBox.find('select').attr('data-sku', record['sku']);
                return $productUnitTmplBox.html();
            }},
            {field: 'warehouseId', title: '仓库', width: 120, formatter: function (val, record) {
                let options = $warehouseTmplBox.find('select option');
                options.removeAttr('selected');
                if (val != '') {
                    options.each(function (index, option) {
                        if ($(option).attr('value') == val) {
                            $(option).attr('selected', 'selected');
                            return false;
                        }
                    });
                }
                $warehouseTmplBox.find('select').attr('data-sku', record['sku']);
                return $warehouseTmplBox.html();
            }},
            {field: 'unitPrice', title: '单价(支)', align: 'left', width: 120, formatter: function (val, record) {
                return $.formatString(editTempl, 'unitPrice', record.productId, val, '单价', record.index);
            }},
            {field: 'packagePrice', title: '包装费', align: 'left', width: 110, formatter: function (val, record) {
                return $.formatString(editTempl, 'packagePrice', record.productId, val, '包装费', record.index);
            }},
            {field: 'itemTotal', width: 150, title: '小计'},
            {field: 'productName', width: 200, title: '产品名称'},
            {field: 'productId', width: 50, align: 'center', title: '操作', formatter: function (val, record) {
                return '<button type="button" class="btn btn-outline-secondary btn-sm" onclick="createOrderProductRemoveItem(\''+val+'\')">删除</button>';
            }}
        ],
        data: productList
    });

    $createOrderAddSkuBtn.on('click', function () {
        $selectProductPopupModal.modal('show');
    });
    $createOrderAddClientBtn.on('click', function () {
        $selectClientPopupModal.modal('show');
    });

    $('#order-create-popup-form').find('input[name="shipAmount"]').on('blur', function () {
        refreshAmountTotalInfo();
    });

    $createOrderSubmitBtn.on('click', function (event) {
        event.preventDefault();

        // 提交创建订单
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
        /*
        if ($.isBlank(params['postalCode'])) {
            $.alertWarning('提示', '请输入邮政编码');
            return false;
        }
        */
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
                    console.log(data);
                    $.alertModal('提示', `订单创建成功，订单号：<b>${data.data.orderId}</b>`, function () {
                        $('#order-create-popup-modal').modal('hide');
                        // 刷新订单列表
                        $('#table').bootstrapTable('refresh');
                    });
                }
            });
        });
    });
});

function refreshClientInfo(){
    $("#customerName").val(selectClient[0].name);
    $("#phoneNumber").val(selectClient[0].phoneNumber);
    $("#postalCode").val(selectClient[0].postalCode);
    $("#address").val(selectClient[0].address);
}


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
        if (item.unit == 'UNIT_BOX') {
            productAmount += item.quantity * item.unitPrice * 6;
        } else {
            productAmount += item.quantity * item.unitPrice;
        }
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
        // console.log(productList);
        $('#create-order-product-table').bootstrapTable('load', productList);
        refreshAmountTotalInfo();
    });
}

function createOrderProductListChange(field, fieldName, recordId, productIndex, valueInput, editBox, displayBox) {
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
        if (item.productId == recordId && item.index == productIndex) {
            item[field] = value;
            if (item.unit == 'UNIT_BOX') {
                item.itemTotal = item.quantity * item.unitPrice * 6 + item.packagePrice;
            } else {
                item.itemTotal = item.quantity * item.unitPrice + item.packagePrice;
            }
        }
    });

    $('#create-order-product-table').bootstrapTable('load', productList);
    refreshAmountTotalInfo();
    // editBox.hide();
    // displayBox.text(value).show();
    // valueInput.val(value);
}

function createOrderSelectProductCallback(products) {

    if (products) {
        var newObject = jQuery.extend(true, {}, products);
        $.each(newObject, function(i, item){
            var mIn = productIndex.toString();
            // let exists = productList.filter(fItem => fItem.productId == item.productId);
            // if (exists.length != 0) {
            //     exists.forEach(eItem => eItem.quantity++);
            // } else {
            //     item.quantity = 1;
            //
            //     // TODO: 获取初始化的价格信息
            //     item.packagePrice = 0;
            //     item.unitPrice = item.retailPrice;
            //
            //     item.itemTotal = item.quantity * item.unitPrice + item.packagePrice;
            //     item.warehouseId = '';
            //     item.unit = 'UNIT_PIECE';
            //     productList.push(item);
            // }
            item.quantity = 1;

            // TODO: 获取初始化的价格信息
            item.packagePrice = 0;
            item.unitPrice = item.retailPrice;

            item.itemTotal = item.quantity * item.unitPrice + item.packagePrice;
            item.warehouseId = '';
            item.unit = 'UNIT_PIECE';
            item.index = mIn;
            productIndex ++;
            productList.push(item);
        });
    }
    $('#create-order-product-table').bootstrapTable('load', productList);
    refreshAmountTotalInfo();
}


function createOrderSelectClientCallback(client) {

    if (client) {
        var newObject = jQuery.extend(true, {}, client);
        selectClient = newObject;
    }
    $('#create-order-client-table').bootstrapTable('load', client);
    refreshClientInfo();
}

function orderItemWarehouseOnChange(select) {
    let $warehouseSelect = $(select);
    let sku = $warehouseSelect.attr('data-sku');
    // console.log($warehouseSelect.val());
    var px = $(select.parentElement.parentElement).find("td:eq(1) div").attr("name").replace("sku-", "");
    productList.forEach(function (product) {
        if (product.index  == px) {
            console.log('sku:' + sku + '改变');
            product.warehouseId = $warehouseSelect.val();
            return false;
        }
    });
    // console.log(productList);
}

function orderItemGiftOnChange(select) {
    console.log(2222);
    let $warehouseSelect = $(select);
    var px = $(select.parentElement.parentElement).find("td:eq(1) div").attr("name").replace("sku-", "");
    let sku = $warehouseSelect.attr('data-sku');
    // console.log($warehouseSelect.val());
    productList.forEach(function (product) {
        if (product.index == px) {
            console.log('sku:' + sku + '改变');
            product.gift = $warehouseSelect.val();
            return false;
        }
    });
    // console.log(productList);
}

function orderItemUnitOnChange(select){
    let $select = $(select);
    let sku = $select.attr('data-sku');
    var px = $(select.parentElement.parentElement).find("td:eq(1) div").attr("name").replace("sku-", "");
    productList.forEach(function (product) {
        if (product.index == px) {
            product.unit = $select.val();

            if (product.unit == 'UNIT_BOX') {
                product.itemTotal = product.quantity * product.unitPrice * 6 + product.packagePrice;
            } else {
                product.itemTotal = product.quantity * product.unitPrice + product.packagePrice;
            }
            return false;
        }
    });
    $('#create-order-product-table').bootstrapTable('load', productList);

    refreshAmountTotalInfo();
}

function resetCreateOrderProductList() {
    productList = [];
    $('#create-order-product-table').bootstrapTable('load', productList);
    refreshAmountTotalInfo();
}