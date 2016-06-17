package com.lclz.index;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 配置Field的权重（boost，norms）
 * 另一种方式是使用 similarity ，参考MySimilarity
 * @author llw
 *
 */
public class FieldBoostExample {

	public static void main(String args[]) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		Document doc = new Document();
		TextField textField = new TextField("name", "", Field.Store.YES);

		float boost = 1f;
		String[] names = { "John R Smith", "Mary Smith", "Peter Smith" };
		for (String name : names) {
			boost *= 1.1;
			textField.setStringValue(name);
			// 设置字段的权重
			textField.setBoost(boost);
			doc.removeField("name");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();

		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new TermQuery(new Term("name", "smith"));
		TopDocs topDocs = searcher.search(query, 100);
		System.out.println("Searching 'smith");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("name").stringValue());
		}
	}
}
