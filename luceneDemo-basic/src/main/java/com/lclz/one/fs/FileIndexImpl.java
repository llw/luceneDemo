package com.lclz.one.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class FileIndexImpl implements FileIndex {
	private final static String SUCCESS = "buildSuccess";
	private final static String ERROR = "buildError";

	private IndexWriter indexWriter;

	private final static String filePath = "/Users/llw/tmp/lucene/Data";
	private final static String indexPath = "/Users/llw/tmp/lucene/index";

	public static void main(String[] args) throws Exception {
		FileIndex fileIndex = new FileIndexImpl();
		fileIndex.createFileIndex(indexPath);
	}

	public void close() throws Exception {
		indexWriter.close();
	}

	@Override
	public String createFileIndex(String indexPath) throws Exception {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		config.setOpenMode(OpenMode.CREATE);
		indexWriter = new IndexWriter(FSDirectory.open(new File(indexPath)), config);
		writeToIndex(filePath);
		close();
		return SUCCESS;
	}

	private Document getDocument(File f) throws IOException {
		Document document = new Document();
		FileInputStream is = new FileInputStream(f);
		Reader reader = new BufferedReader(new InputStreamReader(is));
		document.add(new TextField("context", reader));
		document.add(new TextField("path", f.getAbsolutePath(), Store.YES));
		return document;
	}

	public void writeToIndex(String path) throws Exception {
		File f = new File(path);
		if (f.exists()) {
			if (f.isFile() && f.getName().endsWith(".txt")) {
				Document d = getDocument(f);
				indexWriter.addDocument(d);
			} else {
				File[] files = f.listFiles();
				for (File sonFile : files) {
					Document document = getDocument(sonFile);
					indexWriter.addDocument(document);
				}
			}
		} else {
			throw new Exception("文件不存在");
		}
	}

	public String createUrlIndex(String url) {
		return null;
	}

}
