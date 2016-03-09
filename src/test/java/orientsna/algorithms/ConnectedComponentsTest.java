package orientsna.algorithms;

import org.junit.Assert;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import orientsna.OrientGraphTest;
import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.impl.ParallelGraphEngine;

public class ConnectedComponentsTest extends OrientGraphTest {
	
	@Test
	public void simpleTest() {
		OrientGraphNoTx graph = getGraph();
		
		Vertex a = graph.addVertex(null), b = graph.addVertex(null), c = graph.addVertex(null);
		Vertex d = graph.addVertex(null), e = graph.addVertex(null), f = graph.addVertex(null);
		
		a.addEdge("CC", b);
		b.addEdge("CC", c);
		c.addEdge("CC", a);
		
		d.addEdge("CC", e);
		e.addEdge("CC", f);
		f.addEdge("CC", d);
		
		ConnectedComponents cc = new ConnectedComponents();
		GraphAlgoEngine engine = new ParallelGraphEngine(getFactory());
		engine.execute(cc);
		Object2ObjectMap<String, String> node2components = cc.getResult();
		
		String labela = node2components.get(a.getId().toString());
		String labelb = node2components.get(b.getId().toString());
		String labelc = node2components.get(c.getId().toString());
		
		String labeld = node2components.get(d.getId().toString());
		String labele = node2components.get(e.getId().toString());
		String labelf = node2components.get(f.getId().toString());
		
//		GraphUtils.printGraph(getFactory());
		
		Assert.assertEquals(labela, labelb);
		Assert.assertEquals(labelb, labelc);
		
		Assert.assertEquals(labeld, labele);
		Assert.assertEquals(labele, labelf);

	}
}
