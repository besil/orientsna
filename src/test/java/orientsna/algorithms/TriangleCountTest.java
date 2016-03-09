package orientsna.algorithms;

import org.junit.Assert;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import orientsna.OrientGraphTest;
import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.impl.ParallelGraphEngine;

public class TriangleCountTest extends OrientGraphTest {
	public final String EDGE_LABEL = "example";
	@Test
	public void testTriangles() {
		OrientGraphNoTx graph = getGraph();
		
		Vertex a = graph.addVertex(null), b = graph.addVertex(null), c = graph.addVertex(null);
		Vertex d = graph.addVertex(null), e = graph.addVertex(null), f = graph.addVertex(null);
		Vertex g = graph.addVertex(null), h = graph.addVertex(null), i = graph.addVertex(null), j = graph.addVertex(null);
		
		a.addEdge(EDGE_LABEL, b);
		b.addEdge(EDGE_LABEL, c);
		c.addEdge(EDGE_LABEL, a);
		
		d.addEdge(EDGE_LABEL, e);
		d.addEdge(EDGE_LABEL, f);
		
		g.addEdge(EDGE_LABEL, h);
		h.addEdge(EDGE_LABEL, i);
		i.addEdge(EDGE_LABEL, j);
		j.addEdge(EDGE_LABEL, g);
		h.addEdge(EDGE_LABEL, j);
		
		graph.commit();
		graph.shutdown();
		
		TriangleCount tc = new TriangleCount();
		GraphAlgoEngine engine = new ParallelGraphEngine(getFactory());
		engine.execute(tc);
		Object2LongMap<String> node2tc = tc.getResult();
		
		Assert.assertEquals(2L, node2tc.getLong(a.getId().toString()));
		Assert.assertEquals(2L, node2tc.getLong(b.getId().toString()));
		Assert.assertEquals(2L, node2tc.getLong(c.getId().toString()));
		
		Assert.assertEquals(0L, node2tc.getLong(d.getId().toString()));
		Assert.assertEquals(0L, node2tc.getLong(e.getId().toString()));
		Assert.assertEquals(0L, node2tc.getLong(f.getId().toString()));
		
		Assert.assertEquals(2L, node2tc.getLong(g.getId().toString()));
		Assert.assertEquals(2L, node2tc.getLong(i.getId().toString()));
		Assert.assertEquals(4L, node2tc.getLong(h.getId().toString()));
		Assert.assertEquals(4L, node2tc.getLong(j.getId().toString()));
	}
}
