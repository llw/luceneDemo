package com.lclz.index;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

public class MySimilarity extends Similarity {

	private Similarity sim = null;

	public MySimilarity(Similarity sim) {
		this.sim = sim;
	}

	@Override
	public long computeNorm(FieldInvertState state) {
		// TODO Auto-generated method stub
		return sim.computeNorm(state);
	}

	@Override
	public SimWeight computeWeight(float queryBoost, CollectionStatistics collectionStats, TermStatistics... termStats) {
		// TODO Auto-generated method stub

		return sim.computeWeight(queryBoost, collectionStats, termStats);
	}

	@Override
	public SimScorer simScorer(SimWeight weight, AtomicReaderContext context) throws IOException {
		// TODO Auto-generated method stub
		final Similarity.SimScorer scorer = sim.simScorer(weight, context);
		final NumericDocValues values = context.reader().getNumericDocValues("ranking");
		return new SimScorer() {

			@Override
			public float score(int doc, float freq) {
				// TODO Auto-generated method stub
				return values.get(doc) * scorer.score(doc, freq);
			}

			@Override
			public float computeSlopFactor(int distance) {
				// TODO Auto-generated method stub
				return scorer.computeSlopFactor(distance);
			}

			@Override
			public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
				// TODO Auto-generated method stub
				return scorer.computePayloadFactor(doc, start, end, payload);
			}

		};
	}

}
