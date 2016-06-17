package com.lclz.analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 自定义TokenFilter
 * 功能：缩写转换为全称 如：Dr -> doctor
 * @author llw
 *
 */
public class CourtesyTitleFilter extends TokenFilter {

	Map<String, String> courtesyTitleMap = new HashMap<String, String>();
	private CharTermAttribute termAtt;

	protected CourtesyTitleFilter(TokenStream input) {
		super(input);
		termAtt = addAttribute(CharTermAttribute.class);
		courtesyTitleMap.put("Dr", "doctor");
		courtesyTitleMap.put("Mr", "mister");
		courtesyTitleMap.put("Mrs", "miss");
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!input.incrementToken())
			return false;

		String small = termAtt.toString();
		if (courtesyTitleMap.containsKey(small)) {
			termAtt.setEmpty().append(courtesyTitleMap.get(small));
		}
		return true;
	}

}
