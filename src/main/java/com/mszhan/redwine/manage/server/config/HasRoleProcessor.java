package com.mszhan.redwine.manage.server.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mszhan.redwine.manage.server.config.security.User;
import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HasRoleProcessor extends AbstractAttributeModifierAttrProcessor {
	private static final String ROLE_CLASS = "security_role_class";



	public HasRoleProcessor() {
		super("secRole");
	}

	public int getPrecedence() {
		return 12000;
	}

	 

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, String> getModifiedAttributeValues(
			final Arguments arguments, final Element element,
			final String attributeName) {

		final Configuration configuration = arguments.getConfiguration();


		final Map<String, String> values = new HashMap<String, String>();
		values.put("class", ROLE_CLASS);
		/*
		 * 获得属性值
		 */
		final String attributeValue = element.getAttributeValue(attributeName);

		if (StringUtils.isBlank(attributeValue)) {
			return values;
		}
		List<String> attrList = attrList(attributeValue);
//		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ObjectMapper objMapper = new ObjectMapper();
		User user = null;
		try{
			user = SecurityUtils.getAuthenticationUser();
		} catch (Exception ex){
          ex.printStackTrace();
          return values;
		}

		if (user.getAgentType() == null) {
			return values;
		}
		if (attrList == null){
			return values;
		}
		if (attrList.contains(user.getAgentType())) {
			values.clear();
			return values;
		}
		return values;
	}

	@Override
	protected ModificationType getModificationType(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {

		// 万一该元素已经设置了Class属性，我们将把我们的新值拼接到后面（用空格隔开），或者简单的取代他
		return ModificationType.SUBSTITUTION;
	}

	@Override
	protected boolean removeAttributeIfEmpty(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {

		// 如果算出来的class属性是空则根本不显示出来
		return true;
	}

	@Override
	protected boolean recomputeProcessorsAfterExecution(
			final Arguments arguments, final Element element,
			final String attributeName) {

		// 当这个元素被执行完成后不需要再重新计算
		return false;
	}

	private List<String> attrList(String attrValue) {
		List<String> resultList = new ArrayList<>();
		String[] str = attrValue.split("\\,");
		for (String id : str) {
			if (StringUtils.isNotBlank(id)) {
				resultList.add(StringUtils.trimToNull(id));
			}
		}
		return resultList;

	}
}
