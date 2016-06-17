package com.lclz.search;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class TermQueryExample {

	private Analyzer analyzer;
	private static Directory directory;
	private IndexWriter indexWriter;

	public TermQueryExample() {
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

	public void termQuery() throws IOException {
		System.out.println("termQuery========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		Query query = new TermQuery(new Term("content", "humpty"));

		TopDocs topDocs = searcher.search(query, 100);

		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	public void termRangeQuery() throws IOException {
		System.out.println("termRangeQuery========");
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		Query query = new TermRangeQuery("content", new BytesRef("a"),
				new BytesRef("c"), true, true
				);

		TopDocs topDocs = searcher.search(query, 100);

		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}

	public static void main(String args[]) throws IOException {
		TermQueryExample ex = new TermQueryExample();
		ex.addDocument();
		ex.termQuery();
		ex.termRangeQuery();
	}
}
