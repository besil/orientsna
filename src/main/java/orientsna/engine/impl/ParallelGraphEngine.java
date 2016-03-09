package orientsna.engine.impl;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.algorithms.VertexAlgorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ParallelGraphEngine extends GraphAlgoEngine {
	private ExecutorService executor;

	public ParallelGraphEngine(OrientGraphFactory f) {
		super(f);
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	private void submit(OrientGraphFactory factory, Function<Vertex, Void> f) {
		List<Future<?>> futures = new LinkedList<>();
		
		Graph graph = factory.getNoTx();
		for (Vertex v : graph.getVertices()) {
			Future<?> future = executor.submit(new VertexRunnable(factory, v, f));
			futures.add(future);
		}
		for(Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		graph.shutdown();
	}

	@Override
	protected void init(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		Function<Vertex, Void> f = (v) -> {
			algorithm.init(v);
			return null;
		};
		submit(factory, f);
	}
	
	@Override
	protected void compute(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		Function<Vertex, Void> f = (v) -> {
			algorithm.compute(v);
			return null;
		};
		this.submit(factory, f);
	}

	@Override
	protected void apply(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		Function<Vertex, Void> f = (v) -> {
			algorithm.apply(v);
			return null;
		};
		this.submit(factory, f);
	}

	@Override
	protected void collectResult(OrientGraphFactory factory, VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> {
			algorithm.collectResult(v);
		});
		graph.shutdown();
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

	class VertexRunnable implements Runnable {
		private Vertex v;
		private Function<Vertex, Void> f;
		private OrientGraphFactory factory;

		public VertexRunnable(OrientGraphFactory factory, Vertex v, Function<Vertex, Void> f) {
			this.factory = factory;
			this.v = v;
			this.f = f;
		}

		@Override
		public void run() {
			OrientGraphNoTx tx = factory.getNoTx();
			this.f.apply(v);
			tx.shutdown();
		}
	}

}
