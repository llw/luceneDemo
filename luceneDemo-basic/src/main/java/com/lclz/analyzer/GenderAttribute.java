package com.lclz.analyzer;

import org.apache.lucene.util.Attribute;

interface GenderAttribute extends Attribute {
	public static enum Gender {
		Male, Female, Undefined
	};

	public void setGender(Gender gender);

	public Gender getGender();
}
