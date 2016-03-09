package orientsna.engine.impl;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.algorithms.VertexAlgorithm;

public class SingleGraphEngine extends GraphAlgoEngine {
//	OrientGraphNoTx graph = factory.getNoTx();
//	// Init phase
//	graph.getVertices().forEach(v -> algorithm.init(v));
//	// Main phase
//	for(int it=0; it<algorithm.getNumIterations(); it++) {
////		log.info("Iteration: "+it);
//		graph.getVertices().forEach(v -> algorithm.compute(v));
//		graph.getVertices().forEach(v -> algorithm.apply(v));
//	}
//	// Get results
//	graph.getVertices().forEach(v -> algorithm.collectResult(v));
//	graph.shutdown();

	public SingleGraphEngine(OrientGraphFactory factory) {
		super(factory);
	}

	@Override
	protected void init(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> algorithm.init(v));
		graph.shutdown();
	}

	@Override
	protected void compute(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> algorithm.compute(v));
		graph.shutdown();
	}

	@Override
	protected void apply(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> algorithm.apply(v));
		graph.shutdown();
	}

	@Override
	protected void collectResult(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> algorithm.collectResult(v));
		graph.shutdown();
	}

	@Override
	public void shutdown() {
		// nothing to do here
	}

}
