package orientsna.algorithms;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import orientsna.engine.algorithms.VertexAlgorithm;

public class ConnectedComponents implements VertexAlgorithm {
	public static final String connectedComponentsKey = "CC_label";
	private Object2ObjectMap<String, String> result;
	
	public ConnectedComponents() {
		this.result = new Object2ObjectOpenHashMap<>();
	}
	
	@Override
	public String getName() {
		return "Connected Components";
	}

	@Override
	public Object2ObjectMap<String, String> getResult() {
		return result;
	}

	@Override
	public void init(Vertex v) {
		 v.setProperty(connectedComponentsKey, v.getId().toString());
	}

	@Override
	public void compute(Vertex v) {
		String minLabel = v.getProperty(connectedComponentsKey);
		String neighLabel = null;
		
		for(Vertex neigh : v.getVertices(Direction.BOTH)) {
			neighLabel = neigh.getProperty(connectedComponentsKey);
			minLabel = minLabel.compareTo(neighLabel) <= 0 ? minLabel : neighLabel;
		}
		
		if( ! minLabel.equals(v.getProperty(connectedComponentsKey)) )
			v.setProperty(connectedComponentsKey, minLabel);
	}

	@Override
	public void apply(Vertex v) {
		// Nothing to do here: eveything is done in compute
	}

	@Override
	public void collectResult(Vertex v) {
		this.result.put(v.getId().toString(), v.getProperty(connectedComponentsKey));
	}

	@Override
	public void clean(Vertex v) {
		v.removeProperty(connectedComponentsKey);
	}

	@Override
	public int getNumIterations() {
		return 50;
	}

}
