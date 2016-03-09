package orientsna.algorithms;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import orientsna.OrientGraphTest;
import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.impl.ParallelGraphEngine;

public class LabelPropagationTest extends OrientGraphTest {
	@Test
	public void mostFrequentLabel() {
		LabelPropagation lp = new LabelPropagation();

		Vertex a = getGraph().addVertex(null);
		Vertex b = getGraph().addVertex(null);
		Vertex c = getGraph().addVertex(null);
		Vertex d = getGraph().addVertex(null);

		a.addEdge("KNOWS", d);
		b.addEdge("KNOWS", d);
		c.addEdge("KNOWS", d);

		a.setProperty(LabelPropagation.LP_COMMUNITY_KEY, "a");
		b.setProperty(LabelPropagation.LP_COMMUNITY_KEY, "a");
		c.setProperty(LabelPropagation.LP_COMMUNITY_KEY, "c");

		String mostFrequentLabel = lp.getMostFrequentLabel(d);
		Assert.assertEquals("a", mostFrequentLabel);
	}

	@Test
	public void testDisconnectedComponents() {
		Vertex a = getGraph().addVertex(null), b = getGraph().addVertex(null), c = getGraph().addVertex(null);
		Vertex d = getGraph().addVertex(null), e = getGraph().addVertex(null), f = getGraph().addVertex(null);
		
		a.addEdge("KNOWS", b);
		b.addEdge("KNOWS", c);
		c.addEdge("KNOWS", a);
		
		d.addEdge("KNOWS", e);
		e.addEdge("KNOWS", f);
		f.addEdge("KNOWS", d);
		getGraph().commit();
		
		LabelPropagation lp = new LabelPropagation();
		GraphAlgoEngine engine = new ParallelGraphEngine(getFactory());
		engine.execute(lp);
		
		Object2ObjectMap<String, String> vertex2community = lp.getResult();
		List<String> communities = vertex2community.values().stream().distinct().collect(Collectors.toList());
		Assert.assertEquals(2, communities.size());

		getGraph().getVertices().forEach(v -> Assert.assertTrue(v.getProperty(LabelPropagation.LP_COMMUNITY_KEY) != null));
		getGraph().getVertices().forEach(v -> Assert.assertTrue(v.getProperty(LabelPropagation.LP_COMPUTED_COMMUNITY_KEY) != null));
		
		engine.clean(lp);
		
		getGraph().getVertices().forEach(v -> Assert.assertFalse(v.getProperty(LabelPropagation.LP_COMMUNITY_KEY) != null));
		getGraph().getVertices().forEach(v -> Assert.assertFalse(v.getProperty(LabelPropagation.LP_COMPUTED_COMMUNITY_KEY) != null));
	}

}
