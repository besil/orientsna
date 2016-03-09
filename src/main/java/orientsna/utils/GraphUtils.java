package orientsna.utils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class GraphUtils {
	public static void printGraph(OrientGraphFactory factory) {
		OrientGraph graph = factory.getTx();
		System.out.println("Tot nodes: "+graph.countVertices());
		System.out.println("Tot edges: "+graph.countEdges());

		graph.getVertices().forEach(v -> {
			StringBuilder sb = new StringBuilder();
			sb.append(v.getId().toString()+" (");
			for(String property : v.getPropertyKeys()) {
				sb.append(property+": "+v.getProperty(property));
			}
			sb.append(")\n");
			// sb.append(v.getId().toString()+" ("+v.getProperty("nodeid")+")\n");
			v.getVertices(Direction.OUT).forEach(other -> {
				sb.append("  --> "+other.getId().toString()+"\n");				
			});
			v.getVertices(Direction.IN).forEach(other -> {
				sb.append("  <-- "+other.getId().toString()+"\n");
			});
			System.out.println(sb.toString());
		});
		graph.shutdown();
	}
}
