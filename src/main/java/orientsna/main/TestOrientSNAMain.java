package orientsna.main;

import java.time.Duration;
import java.time.Instant;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class TestOrientSNAMain {
	public static void main(String[] args) {
		OrientGraphFactory factory = new OrientGraphFactory("memory:test").setupPool(1, 10);
		Int2ObjectMap<Vertex> nodeOrder = new Int2ObjectOpenHashMap<>();
		
//		OrientGraph graph = factory.getTx(); // 73 s
		OrientGraphNoTx graph = factory.getNoTx(); // 64s single core
		
		for(int i=0; i<10; i++) {
			graph.addVertex(null);
		}
		
		Instant globalStart = Instant.now();
		for(int i=0; i<10_000; i++) {
			Vertex v = graph.addVertex(0);
			nodeOrder.put(i, v);
			
//			graph.commit();
			for(int j=0; j<i && j<10; j++) {
				v.addEdge("friend", nodeOrder.get(j));
			}
//			graph.commit();
			
		}
		Instant globalEnd = Instant.now();
		Duration duration = Duration.between(globalStart, globalEnd);
		System.out.println("Duration: "+duration.getSeconds()+"s");
		
		
//		Vertex luca = graph.addVertex(1);
//		luca.setProperty("name", "Luca");
//		Vertex marko = graph.addVertex(1);
//		marko.setProperty("name", "Marko");
//		graph.addEdge(null, luca, marko, "knows");
//		
//		System.out.println("There are " + graph.countVertices() + " vertices");
//		System.out.println("There are " + graph.countEdges() + " edges");
//		graph.commit();
//		System.out.println("----- commit -----");
//		System.out.println("There are " + graph.countVertices() + " vertices");
//		System.out.println("There are " + graph.countEdges() + " edges");
		
		graph.shutdown();
	}
}
