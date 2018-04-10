package com.mszhan.redwine.manage.server.enums;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by xb on 2017/11/1.
 */
public enum AgentOperationTypeEnum {
	RECHARGE("充值"),
	OTHER_ADD("其他加"),
	OTHER_SUBTRACT("其他减"),
	ORDER_DEDUCTION("订单花费"),
	BALANCE("结余");

	private String value;

	private AgentOperationTypeEnum(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
