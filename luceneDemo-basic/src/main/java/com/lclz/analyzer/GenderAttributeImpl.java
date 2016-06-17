package com.lclz.analyzer;

import org.apache.lucene.util.AttributeImpl;

public class GenderAttributeImpl extends AttributeImpl implements GenderAttribute {

	private Gender gender = Gender.Undefined;

	@Override
	public void setGender(Gender gender) {
		this.gender = gender;

	}

	@Override
	public Gender getGender() {
		// TODO Auto-generated method stub
		return gender;
	}

	@Override
	public void clear() {
		gender = Gender.Undefined;

	}

	@Override
	public void copyTo(AttributeImpl target) {
		((GenderAttribute) target).setGender(gender);

	}

}
