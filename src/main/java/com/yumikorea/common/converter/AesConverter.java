package com.yumikorea.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

import com.yumikorea.common.utils.AesUtil;

@Converter
public class AesConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if(!StringUtils.hasText(attribute)) {
			return attribute;
		}
		return AesUtil.encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return AesUtil.decrypt(dbData);
	}

}
