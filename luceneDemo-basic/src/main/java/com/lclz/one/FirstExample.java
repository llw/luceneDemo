package com.lclz.one;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class FirstExample {

	private Analyzer analyzer;
	private Directory directory;
	private IndexWriter indexWriter;

	public FirstExample() throws IOException {
		// Creating an analyzer
		analyzer = new WhitespaceAnalyzer();// 用空格做为分界符
		directory = new RAMDirectory();// index存入内存中
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		indexWriter = new IndexWriter(directory, config);
	}

	void write() throws IOException {

		// createing fields
		Document doc = new Document();
		String text = "Lucene is an Information Retrieval library written in Java.";
		doc.add(new TextField("Content", text, Field.Store.YES));
		String id = "1";
		doc.add(new TextField("id", id, Field.Store.YES));

		// Creating and writing documents to an index
		indexWriter.addDocument(doc);
		indexWriter.close();// commit all changes and close the index.
	}

	void deleteDocument() throws IOException {
		// f deleteDocuments(Term)
		// f deleteDocuments(Term... terms)
		// f deleteDocuments(Query)
		// f deleteDocuments(Query... queries)
		// f deleteAll( )
		//
		// delete all the documents that contain the term id where the value
		// equals 1
		indexWriter.deleteDocuments(new Term("id", "1"));
		indexWriter.close();
	}

	void search() throws IOException, ParseException {
		// 1. Obtaining an IndexSearcher
		IndexReader indexReader = DirectoryReader.open(directory);// opens an
																	// index to
																	// read
		// The IndexSearcher class is the gateway to search an index
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// 2. Creating queries with the Lucene QueryParser
		// 能使用模糊查询和通配符匹配
		QueryParser parser = new QueryParser("Content", analyzer);
		Query query = parser.parse("Lucene");

		// 3. Performing a search
		int hitsPerPage = 10;
		// TopDocs, we only get back an array of ranked pointers.
		// not actual documents,but a list of references (DocId)
		TopDocs docs = indexSearcher.search(query, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		int end = Math.min(docs.totalHits, hitsPerPage);
		System.out.println("Total Hits: " + docs.totalHits);
		System.out.print("Results: ");
		for (int i = 0; i < end; i++) {
			Document d = indexSearcher.doc(hits[i].doc);
			System.out.println("Content: " + d.get("Content"));
		}

	}

	public static void main(String args[]) throws IOException, ParseException {
		FirstExample fe = new FirstExample();
		fe.write();
		fe.search();

	}
}
