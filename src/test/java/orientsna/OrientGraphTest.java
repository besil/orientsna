package orientsna;

import org.junit.After;
import org.junit.Before;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

public abstract class OrientGraphTest {
	private final OrientGraphFactory factory = new OrientGraphFactory("memory:test").setupPool(1, 10);
	private OrientGraphNoTx graph;

	@Before
	public void init() {
		graph = factory.getNoTx();
	}

	@After
	public void tearDown() {
		graph.shutdown();
	}
	
	protected OrientGraphFactory getFactory() {
		return factory;
	}
	
	public OrientGraphNoTx getGraph() {
		return graph;
	}
	
}
