package orientsna.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import orientsna.engine.algorithms.VertexAlgorithm;
import orientsna.utils.Timer;

public abstract class GraphAlgoEngine {
	private OrientGraphFactory factory;
	private Logger log = Logger.getLogger(GraphAlgoEngine.class.getName());

	public GraphAlgoEngine(OrientGraphFactory f) {
		this.factory = f;
	}

	public void execute(VertexAlgorithm algorithm) {
		Timer timer = Timer.newTimer();
		timer.start();

		// Init phase
		this.init(factory, algorithm);
		// Main phase
		for(int it=0; it<algorithm.getNumIterations(); it++) {
			this.compute(factory, algorithm);
			this.apply(factory, algorithm);
		}
		// factory.getTx().traverse().predicate(new OCommandPredicateandpre)
		
		this.collectResult(factory, algorithm);

		timer.stop();
		log.info("Execute"+ algorithm.getName()+": " + timer.totalTime());
	}

	protected abstract void init(OrientGraphFactory factory, VertexAlgorithm algorithm);
	protected abstract void compute(OrientGraphFactory factory, VertexAlgorithm algorithm);
	protected abstract void apply(OrientGraphFactory factory, VertexAlgorithm algorithm);
	protected abstract void collectResult(OrientGraphFactory factory, VertexAlgorithm algorithm);

	public void disableLogging() {
		this.log.setLevel(Level.OFF);
	}
	
	public void clean(VertexAlgorithm algorithm) {
		OrientGraphNoTx graph = factory.getNoTx();
		graph.getVertices().forEach(v -> algorithm.clean(v));
		graph.shutdown();
	}
	
	public abstract void shutdown();

}
