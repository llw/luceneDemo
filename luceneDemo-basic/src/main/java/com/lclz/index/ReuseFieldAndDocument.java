package com.lclz.index;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 最佳实践：重复利用Field 和 Document 对象
 * @author llw
 *
 */
public class ReuseFieldAndDocument {

	public static void main(String args[]) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		Document doc = new Document();
		StringField stringField = new StringField("name", "", Field.Store.YES);

		String[] names = { "John", "Mary", "Peter" };
		for (String name : names) {
			stringField.setStringValue(name);
			doc.removeField("name");
			doc.add(stringField);
			indexWriter.addDocument(doc);
		}

		indexWriter.commit();
		indexWriter.close();
		IndexReader reader = DirectoryReader.open(directory);
		for (int i = 0; i < 3; i++) {
			doc = reader.document(i);
			System.out.println("DoctId:" + i + ",name:" + doc.getField("name").stringValue());
		}
	}
}
