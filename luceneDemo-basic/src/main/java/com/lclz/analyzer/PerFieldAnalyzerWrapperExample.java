package com.lclz.analyzer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * PerFieldAnalyzerWrapper可以实现不同的field使用不同的Analyzer
 * PerFieldAnalyzerWrapper constructor accepts two arguments: 
 *  a default analyzer and a Map of field to analyzer mapping. 
 *  
 *  During the analysis process, if a  eld is found in the Map, the associated Analyzer will be used. 
 *  Otherwise, the process will use the default analyzer
 * @author llw
 *
 */
public class PerFieldAnalyzerWrapperExample {

	public static void main(String args[]) {

		Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
		analyzerPerField.put("myfield", new WhitespaceAnalyzer());

		PerFieldAnalyzerWrapper defanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);

		TokenStream ts = null;
		OffsetAttribute offsetAtt = null;
		CharTermAttribute termAtt = null;

		try {
			ts = defanalyzer.tokenStream("myfield", new StringReader("lucene.apache.org AB-978"));
			offsetAtt = ts.addAttribute(OffsetAttribute.class);
			termAtt = ts.addAttribute(CharTermAttribute.class);

			ts.reset();
			System.out.println("== Processing field 'myfield' using WhitespaceAnalyzer (per field) ==");
			while (ts.incrementToken()) {
				String token = termAtt.toString();
				System.out.println("[" + token + "]");
				System.out.println("Token starting offset: " + offsetAtt.
						startOffset());
				System.out.println(" Token ending offset: " + offsetAtt.
						endOffset());
			}
			ts.end();

			ts = defanalyzer.tokenStream("content", new StringReader("lucene.apache.org AB-978"));
			offsetAtt = ts.addAttribute(OffsetAttribute.class);
			termAtt = ts.addAttribute(CharTermAttribute.class);

			ts.reset();
			System.out.println("== Processing field 'content' using StandardAnalyzer==");
			while (ts.incrementToken()) {
				String token = termAtt.toString();
				System.out.println("[" + token + "]");
				System.out.println("Token starting offset: " + offsetAtt.
						startOffset());
				System.out.println(" Token ending offset: " + offsetAtt.
						endOffset());
			}
			ts.end();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
