package orientsna.loader;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphLoader {
	private static final String NODE_ID_KEY = "nodeid";
	private static final String EDGE_LABEL = "KNOWS";
	
	private void addNodes(OrientGraphNoTx graph, File nodes) throws IOException {
		BufferedReader brd = new BufferedReader(new FileReader(nodes));
		LineReader reader = new LineReader(brd, ",");
		for(String[] split : reader) {
			Integer id = Integer.parseInt( split[0] );
			OrientVertex vertex = graph.addVertex(null);
			vertex.setProperty(NODE_ID_KEY, id);
		}
		brd.close();
	}
	
	private void addEdges(OrientGraphNoTx graph, File edges) throws IOException {
		BufferedReader brd = new BufferedReader(new FileReader(edges));
		LineReader reader = new LineReader(brd, ",");

        int c = 0;
        for(String[] split : reader) {
            if (++c % 100 == 0)
                System.out.println(c);
            Integer fromID = Integer.parseInt(split[0]);
            Integer toID = Integer.parseInt(split[1]);
			
			Vertex from = graph.getVertices(NODE_ID_KEY, fromID).iterator().next();
			Vertex to = graph.getVertices(NODE_ID_KEY, toID).iterator().next();
			
//			from.addEdge(EDGE_LABEL, to);
			graph.addEdge("class:"+EDGE_LABEL, from, to, null);
		}
		brd.close();
	}
	
	public void load(OrientGraphNoTx graph, File nodes, File edges) throws IOException {
		this.addNodes(graph, nodes);
		this.addEdges(graph, edges);
		
	}

	public void load(OrientGraphFactory factory, File nodes, File edges) throws IOException {
		OrientGraphNoTx g = factory.getNoTx();
		this.load(g, nodes, edges);
		g.shutdown();
	}
	
	
}
