package com.lclz.index;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 删除策略,IndexDeletionPolicy决定了删除策略。可以决定是否保留之前的commit版本。
 * 包括：
 * 	KeepOnlyLastCommitDeletionPolicy：默认
 * 	NoDeletionPolicy
 * 	SnapshotDeletionPolicy
 * 	PersistentSnapshotDeletionPolicy
 * @author llw
 *
 */
public class IndexDeletionPolicyExample1 {

	public static void main(String args[]) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

		SnapshotDeletionPolicy policy = new SnapshotDeletionPolicy(NoDeletionPolicy.INSTANCE);
		config.setIndexDeletionPolicy(policy);

		IndexWriter indexWriter = new IndexWriter(directory, config);

		IndexCommit lastSnapshot;

		Document document = new Document();
		indexWriter.addDocument(document);
		indexWriter.commit();

		IndexCommit lastSnapshot1 = policy.snapshot();

		document = new Document();
		indexWriter.addDocument(document);
		indexWriter.commit();

		IndexCommit lastSnapshot2 = policy.snapshot();

		document = new Document();
		indexWriter.addDocument(document);
		indexWriter.rollback();
		indexWriter.close();

		List<IndexCommit> commits = DirectoryReader.listCommits(directory);
		System.out.println("Commits count:" + commits.size());
		for (IndexCommit commit : commits) {
			IndexReader reader = DirectoryReader.open(commit);
			System.out.println("Commit " + commit.getSegmentCount());
			System.out.println("Number of docs:" + reader.numDocs());
		}

		System.out.println("\nSnapshots count: " + policy.getSnapshotCount());
		List<IndexCommit> snapshots = policy.getSnapshots();
		for (IndexCommit snapshot : snapshots) {
			IndexReader reader = DirectoryReader.open(snapshot);
			System.out.println("Snapshot " + snapshot.getSegmentCount());
			System.out.println("Number of docs:" + reader.numDocs());
		}
		policy.release(lastSnapshot1);
		policy.release(lastSnapshot2);
		System.out.println("\nSnapshots count: " + policy.getSnapshotCount());
		System.out.println("Commits count:" + commits.size());
	}
}
