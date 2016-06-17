package com.lclz.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * description:
 * 	Field: A Field is composed of three parts: name, type, and value. Values can be text, binary, or numeric.
 * 		
 * @author llw
 *
 */
public class IndexExample1 {

	private FSDirectory directory;
	private Analyzer analyzer;
	private IndexWriter indexWriter;

	public IndexExample1() throws IOException {

		// store index files in the file system
		String indexPath = "/Users/llw/tmp/index";
		directory = FSDirectory.open(new File(indexPath));

		analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

		// OpenMode:open index mode,provide three OpenMode options:
		// APPEND,CREATE,CREATE_OR_APPEND
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		// allows you to use the amount of RAM to use for buffering changes
		// before being flushed to the directory.
		// default is 16 MB.
		config.setRAMBufferSizeMB(64);
		// it lets you set the minimum number of documents required before the
		// buffered documents are ushed as a new segment
		// default is 1000
		config.setMaxBufferedDocs(4000);
		indexWriter = new IndexWriter(directory, config);
	}

	/**
	 *  StringField. Any value stored in this Field can be indexed, but not tokenized. 
	 *  The entire string is treated as a single token.
	 * @throws IOException 
	 */
	public void addStringField() throws IOException {
		Document doc = new Document();
		doc.add(new StringField("telephone_number", "04735264927", Field.Store.YES));
		doc.add(new StringField("area_code", "0484", Field.Store.YES));
		indexWriter.addDocument(doc);
	}

	/**
	 * TextField is tokenized
	 * @throws IOException
	 */
	public void addTextField() throws IOException {
		Document doc = new Document();
		String text = "Lucene is an Information Retrieval library written in Java.";
		doc.add(new TextField("text", text, Field.Store.YES));
		indexWriter.addDocument(doc);
	}

	/**
	 * include four Class:IntField, FloatField, LongField, and DoubleField
	 * ???
	 */
	public void addNumericField() {

		IntField intField = new IntField("int_value", 100, Field.Store.YES);
		LongField longField = new LongField("long_value", 100L, Field.Store.YES);
		FloatField floatField = new FloatField("float_value", 100.0F, Field.Store.YES);
		DoubleField doubleField = new DoubleField("double_value", 100.0D, Field.Store.YES);

		// creating a single-valued IntField to sort purposes
		FieldType sortedIntField = new FieldType();
		sortedIntField.setNumericType(FieldType.NumericType.INT);
		sortedIntField.setNumericPrecisionStep(Integer.MAX_VALUE);
		sortedIntField.setStored(false);
		sortedIntField.setIndexed(true);
		IntField intFieldSorted = new IntField("int_value_sort", 100,
				sortedIntField);

		Document document = new Document();
		document.add(intField);
		document.add(longField);
		document.add(floatField);
		document.add(doubleField);
		document.add(intFieldSorted);
	}

	public static void main(String args[]) throws IOException {

	}

}
