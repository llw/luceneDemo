package com.lclz.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 * 自定义Analyzer
 * @author llw
 *
 */
public class CourtesyTitleAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer letterTokenizer = new LetterTokenizer(reader);
		TokenStream filter = new CourtesyTitleFilter(letterTokenizer);

		// return a new TokenStreamComponents instance initialized by the
		// instantiated Tokenizer and TokenFilter.
		return new TokenStreamComponents(letterTokenizer, filter);
	}

}
