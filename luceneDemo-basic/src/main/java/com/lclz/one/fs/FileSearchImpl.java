package com.lclz.one.fs;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FileSearchImpl implements FileSearcher {
	private final static String indexPath = "/Users/llw/tmp/lucene/index";

	@Override
	public void search(String keyWord) throws Exception {
		Directory directory = FSDirectory.open(new File(indexPath));

		IndexReader indexReader = DirectoryReader.open(directory);

		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		Query query = new TermQuery(new Term("context", keyWord));

		TopDocs docs = indexSearcher.search(query, 10);

		ScoreDoc[] sds = docs.scoreDocs;

		System.out.println(docs.totalHits + "命中数目");
		for (ScoreDoc doc : sds) {
			Document d = indexSearcher.doc(doc.doc);
			System.out.println("path:" + d.get("path"));
		}
		indexReader.close();
	}

	public static void main(String[] args) throws Exception {
		FileSearcher searcher = new FileSearchImpl();
		searcher.search("lucene");
	}
}
