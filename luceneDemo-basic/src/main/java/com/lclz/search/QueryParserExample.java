package com.lclz.search;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class QueryParserExample {

	private Analyzer analyzer;
	private Directory directory;
	private IndexWriter indexWriter;

	public QueryParserExample() {
		analyzer = new StandardAnalyzer();
		directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
				analyzer);
		try {
			indexWriter = new IndexWriter(directory, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDocument() throws IOException {
		Document doc = new Document();
		StringField stringField = new StringField("name", "", Field.Store.YES);
		TextField textField = new TextField("content", "", Field.Store.YES);
		IntField intField = new IntField("num", 0, Field.Store.YES);

		doc.removeField("name");
		doc.removeField("content");
		doc.removeField("num");
		stringField.setStringValue("First");
		textField.setStringValue("Humpty Dumpty sat on a wall,");
		intField.setIntValue(100);
		doc.add(stringField);
		doc.add(textField);
		doc.add(intField);
		indexWriter.addDocument(doc);

		doc.removeField("name");
		doc.removeField("content");
		doc.removeField("num");
		stringField.setStringValue("Second");
		textField.setStringValue("Humpty Dumpty had a great fall.");
		intField.setIntValue(200);
		doc.add(stringField);
		doc.add(textField);
		doc.add(intField);
		indexWriter.addDocument(doc);

		doc.removeField("name");
		doc.removeField("content");
		doc.removeField("num");
		stringField.setStringValue("Third");
		textField.setStringValue("All the king's horses and all the king's men");
		intField.setIntValue(300);
		doc.add(stringField);
		doc.add(textField);
		doc.add(intField);
		indexWriter.addDocument(doc);

		doc.removeField("name");
		doc.removeField("content");
		doc.removeField("num");
		stringField.setStringValue("Fourth");
		textField.setStringValue("Couldn't put Humpty together again.");
		intField.setIntValue(400);
		doc.add(stringField);
		doc.add(textField);
		doc.add(intField);
		indexWriter.addDocument(doc);

		indexWriter.commit();
		indexWriter.close();

	}

	public void baseSearch() throws IOException, ParseException {
		System.out.println("baseSearch=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		Query query = parser.parse("humpty");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}

	}

	/**
	 * 通配符查询：*,?
	 * @throws IOException
	 * @throws ParseException
	 */
	public void wildcardSearch() throws IOException, ParseException {
		System.out.println("wildcardSearch=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setAllowLeadingWildcard(true);// ?什么作用

		// this query will produce either a PrefixQuery or WildcardQuery
		Query query = parser.parse("humpty*");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 范围
	 * [start TO end] inclusive
	 * {start TO end} – exclusive
	 * @throws IOException
	 * @throws ParseException
	 */
	public void termRangeSearch() throws IOException, ParseException {
		System.out.println("termRangeSearch=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setAnalyzeRangeTerms(true);

		// this search will produce a TermRangeQuery by using TO in a search
		// string
		Query query = parser.parse("[aa TO c]");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 生成查询短语
	 * @throws IOException
	 * @throws ParseException
	 */
	public void autoGeneratedPhraseQuery() throws IOException, ParseException {
		System.out.println("autoGeneratedPhraseQuery=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setAutoGeneratePhraseQueries(true);

		// This search will generate a PhraseQuery on the phrase humpty dumpty
		// sat and will return the rst sentence.
		Query query = parser.parse("humpty+dumpty+sat");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void defaultOperator() throws IOException, ParseException {
		System.out.println("defaultOperator=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		// 默认为QueryParser.Operator.OR
		parser.setDefaultOperator(QueryParser.Operator.AND);

		// contain both humpty and dumpty.
		Query query = parser.parse("humpty dumpty");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 模糊查询: ~
	 * @throws IOException
	 * @throws ParseException
	 */
	public void fuzzyQuery() throws IOException, ParseException {
		System.out.println("fuzzyQuery=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setFuzzyMinSim(2);
		parser.setFuzzyPrefixLength(3);

		Query query = parser.parse("hump~");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 *  whether to automatically lowercase multiterm queries
	 * @throws IOException
	 * @throws ParseException
	 */
	public void lowercaseExpandedTerm() throws IOException, ParseException {
		System.out.println("lowercaseExpandedTerm=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setLowercaseExpandedTerms(true);

		Query query = parser.parse("\"Humpty Dumpty\"");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 短语的模糊匹配
	 * @throws IOException
	 * @throws ParseException
	 */
	public void phraseSlop() throws IOException, ParseException {
		System.out.println("phraseSlop=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setPhraseSlop(3);

		Query query = parser.parse("\"Humpty Dumpty wall\"");

		TopDocs topDocs = searcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	/**
	 * 日期查询
	 */
	public void date() throws IOException, ParseException {
		System.out.println("date=========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("content", analyzer);

		parser.setDateResolution("date", DateTools.Resolution.DAY);
		parser.setLocale(Locale.US);
		parser.setTimeZone(TimeZone.getTimeZone("Am erica/New_York"));

	}

	public static void main(String args[]) throws IOException, ParseException {
		QueryParserExample ex = new QueryParserExample();
		ex.addDocument();
		ex.baseSearch();
		ex.wildcardSearch();
		ex.termRangeSearch();
		ex.autoGeneratedPhraseQuery();
		ex.defaultOperator();
		ex.fuzzyQuery();
		ex.lowercaseExpandedTerm();
		ex.phraseSlop();
	}
}
