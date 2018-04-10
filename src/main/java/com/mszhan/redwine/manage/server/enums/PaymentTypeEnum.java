package com.mszhan.redwine.manage.server.enums;

/**
 * Created by xb on 2017/11/1.
 */
public enum PaymentTypeEnum {
	BANK_PAYMENT("银行支付"),
	WECHAT_PAYMENT("微信支付"),
	CASH_PAYMENT("现金支付"),
	ALIPAY_PAYMENT("支付宝支付"),
	OTHER("其他");

	private String value;



	private PaymentTypeEnum(String value){
	 this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
