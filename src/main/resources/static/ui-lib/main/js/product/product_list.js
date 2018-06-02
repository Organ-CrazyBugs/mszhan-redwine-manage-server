$(function () {
    let $table = $('#table');

    let $createSubmitBtn = $('#create-submit-btn');
    let $createForm = $('#create-form');
    let $showEditBtn = $('#show-edit-btn');
    let $deleteBtn = $('#delete-btn');
    let $showInfoBtn = $("#show-info-btn");
    let $editForm = $('#edit-form');
    let $editSubmitBtn = $('#edit-submit-btn');
    let $createBtn = $('#create_btn');
    let $showIntrBtn = $('#show_intr_btn');
    let $showPriceBtn = $('#show_price_btn');
    let $showPriceSubmitBtn = $('#show-price-submit-btn');
    let $showPriceModal = $('#show-price-modal');

    $table.bootstrapTable({
        url: '/product/search',
        tableQueryForm: '#table-query-form',
        columns: [
            {checkbox: true},
            {field: '',
                title: '产品图片',
                formatter: img,
                width:220
            },
            {field: 'productName', title: '产品名称',  width:160, formatter : textShow},
            {field: 'sku', title: 'SKU', width:100},
            {field: 'generalGentPrice', title: '总代理价',width:100},
            {field: 'gentPrice', title: '代理价',width:100},
            {field: 'wholesalePrice', title: '批发价',width:100},
            {field: 'retailPrice', title: '零售价',width:100},
            {field: 'cost', title: '成本',width:100},
            {field: 'unit', title: '单位',width:100},
            {field: 'specification', title: '规格',width:100},
            {field: 'level', title: '级别',width:100},
            {field: 'productionArea', title: '产区',width:160, formatter : textShow},
            {field: 'brandName', title: '品牌',width:100},
            {field: 'alcoholContent', title: '酒精度',width:100},
            {field: 'originCountry', title: '原产国',width:160, formatter : textShow},
            {field: 'treeAge', title: '树龄', width:100},
            {field: 'wineType', title: '葡萄酒类型', width:150},
            {field: 'storageMethod', title: '贮藏方式', width:150},
            {field: 'withFood', title: '美食搭配', width:150, formatter : textShow},
            {field: 'age', title: '年份', width:100},
            {field: 'incubationPeriod', title: '酿造方法', width:100},
            {field: 'netWeight', title: '净含量', width:100},
            {field: 'makingTime', title: '酿酒时间', width:100},
            {field: 'tastingRecords', title: '品鉴记录', width:150, formatter : textShow},
            {field: 'recommendedReason', title: '推荐理由', width:150, formatter : textShow},
            {field: 'brandBackgroud', title: '品牌背景', width:150, formatter : textShow},
            {field: 'creatorName', title: '创建人', width:100},
            {field: 'updatorName', title: '更新人', width:100},
            {field: 'createDate', title: '创建时间', width:150},
            {field: 'updateDate', title: '更新时间', width:150},


        ]
    });
    function textShow(value,row,index){
        var a = "<div class='hiddenOverFlowCoverShow' title='"+value+"'> "+ value +"</div>";
        return a;
    }


    function img(e, v){
        var str = "/product_img/basic/" + v.productFileName;
        return "<img style='height:60px;width:200px;' src='"+str+"' />";
    }


    $deleteBtn.on('click', function (e){

        var data = $table.bootstrapTable('getSelections');
        if (data == null || data.length == 0){
            $.alertError('未勾选', '请先勾选数据');
        }
        var idList = new Array();
        $.each(data, function(index, val){
            idList.push(val.id);
        });

        $.confirm('确认您的操作', '是否确认删除', function(){
            $.ajax({
                url: '/product/delete_by_ids/' + idList.join(","),
                method: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                targetBtn: $deleteBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
                success: function (data) {

                    if ($.ajaxIsFailure(data)) {

                        return;
                    }
                    $.alertSuccess('提示', '删除成功!');

                    $table.bootstrapTable('refresh');                       // 刷新列表
                    $createModal.modal('hide');    // 关闭模态框
                }
            });

        });
    });

    // 点击创建仓库按钮事件
    $createSubmitBtn.on('click', function(event){
        event.preventDefault();
        let params = $createForm.serializeObject();
        if ($.isBlank($.trim(params['productName']))) {
            $.alertError('缺少参数', '产品名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['sku']))) {
            $.alertError('缺少参数', 'SKU为必填项');
            return;
        }
        if ($.isBlank($.trim(params['generalGentPrice']))) {
            $.alertError('缺少参数', '总代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['gentPrice']))) {
            $.alertError('缺少参数', '代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['wholesalePrice']))) {
            $.alertError('缺少参数', '批发价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['retailPrice']))) {
            $.alertError('缺少参数', '零售价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['cost']))) {
            $.alertError('缺少参数', '成本价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['unit']))) {
            $.alertError('缺少参数', '单位为必填项');
            return;
        }
        $.ajax({
            url: '/product/add_product',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $createSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {

                if ($.ajaxIsFailure(data)) {

                    return;
                }
                $.alertSuccess('提示', '产品创建成功!');

                $table.bootstrapTable('refresh');                       // 刷新列表
                $createModal.modal('hide');    // 关闭模态框
            }
        });
    });
    // 修改仓库信息 按钮单击事件
    $showPriceSubmitBtn.on('click', function(event) {

        var showGeneralGentPrice = $("#showGeneralGentPrice").val();
        var showGentPrice = $("#showGentPrice").val();
        var showWholesalePrice = $("#showWholesalePrice").val();
        var showRetailPrice = $("#showRetailPrice").val();

        window.open("/page/product/show_product_price_index?showGeneralGentPrice=" + showGeneralGentPrice + "&showGentPrice=" + showGentPrice + "&showWholesalePrice=" + showWholesalePrice + "&showRetailPrice=" + showRetailPrice);
        $showPriceModal.modal('hide');
    });

    // 修改仓库信息 按钮单击事件
    $showEditBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        // 绑定表单数据
        var id = rows[0].id;
        window.open("/page/product/edit_index?id=" + id);
    });
    // 修改仓库信息 按钮单击事件
    $showInfoBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        // 绑定表单数据
        var id = rows[0].id;
        window.location.href="/page/product/product_detail_index?id=" + id;
    });

    $showIntrBtn.on('click', function(event) {
        let rows = $table.bootstrapTable('getSelections');
        if (rows.length <= 0 ) {
            $.alertWarning('提示', '请选择需要修改的记录项');
            return;
        }
        if (rows.length > 1){
            $.alertWarning('提示', '只能勾选一个修改项');
            return;
        }
        // 绑定表单数据
        var id = rows[0].id;
        window.open("/page/product/product_introduce_index?id=" + id);
    });
    $showPriceBtn.on('click', function(event) {
        $("#show-price-form").reset();
        $showPriceModal.modal('show');
        // window.open("/page/product/show_product_price_index");
    });

    // 修改仓库信息 提交按钮单击事件
    $editSubmitBtn.on('click', function(event) {
        event.preventDefault();
        let params = $editForm.serializeObject();
        if ($.isBlank($.trim(params['productName']))) {
            $.alertError('缺少参数', '产品名称为必填项');
            return;
        }
        if ($.isBlank($.trim(params['sku']))) {
            $.alertError('缺少参数', 'SKU为必填项');
            return;
        }
        if ($.isBlank($.trim(params['generalGentPrice']))) {
            $.alertError('缺少参数', '总代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['gentPrice']))) {
            $.alertError('缺少参数', '代理价格为必填项');
            return;
        }
        if ($.isBlank($.trim(params['wholesalePrice']))) {
            $.alertError('缺少参数', '批发价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['retailPrice']))) {
            $.alertError('缺少参数', '零售价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['cost']))) {
            $.alertError('缺少参数', '成本价为必填项');
            return;
        }
        if ($.isBlank($.trim(params['unit']))) {
            $.alertError('缺少参数', '单位为必填项');
            return;
        }


        $.ajax({
            url: '/product/update_product',
            method: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(params),
            targetBtn: $editSubmitBtn,   // 指定发送ajax请求后在请求过程中禁用的按钮对象
            success: function (data) {
                if ($.ajaxIsFailure(data)) {
                    return;
                }
                $.alertSuccess('提示', '修改成功!');

                $table.bootstrapTable('refresh');       // 刷新列表
                $editModal.modal('hide');    // 关闭模态框
            }
        });
    });

    $createBtn.on('click', function (){
        window.location.href="/page/product/create_index";
    });
});