package com.lclz.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

/**
 * 自定义tokenizer
 * @author llw
 *
 */
public class MyTokenizer extends CharTokenizer {

	public MyTokenizer(AttributeFactory factory, Reader input) {
		super(factory, input);
	}

	@Override
	protected boolean isTokenChar(int c) {
		return !Character.isSpaceChar(c);
	}

}
