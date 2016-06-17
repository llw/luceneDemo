package com.lclz.index;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 * DocValues Field与普通的Field（TextField）区别：
 * 	The  common field's storage is row-oriented（横向）, whereas DocValue's storage is column-oriented（纵向）
 * DocValue types: 
 * 	BinaryDocValues
 * 	NumericDocValues
 * 	SortedDocValues
 * 	SortedNumericDocValues
 * 	SortedSetDocValues
 * @author llw
 *
 */
public class DocValuesFieldExample {

	public static void main(String args[]) throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		// BytesRef:string -> byte[]
		Document doc = new Document();
		doc.add(new SortedDocValuesField("sorted_string", new BytesRef("world")));
		indexWriter.addDocument(doc);

		doc = new Document();
		doc.add(new SortedDocValuesField("sorted_string", new BytesRef("hello")));
		indexWriter.addDocument(doc);

		indexWriter.commit();
		indexWriter.close();

		IndexReader reader = DirectoryReader.open(directory);
		doc = reader.document(0);
		System.out.println("doc 0:" + doc.toString());
		doc = reader.document(1);
		System.out.println("doc 1:" + doc.toString());

		// the DocValues reader can only leverage AtomicReader
		for (AtomicReaderContext context : reader.leaves()) {
			AtomicReader atomicReader = context.reader();
			SortedDocValues sortedDocValues = DocValues.getSorted(atomicReader, "sorted_string");
			System.out.println("Value count:" + sortedDocValues.getValueCount());
			System.out.println("doc 0 sorted_String:" + sortedDocValues.get(0).utf8ToString());
			System.out.println("doc 1 sorted_String:" + sortedDocValues.get(1).utf8ToString());
		}
		reader.close();
	}
}
