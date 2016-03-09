package orientsna.algorithms;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import orientsna.engine.algorithms.VertexAlgorithm;

public class TriangleCount implements VertexAlgorithm {
	private Object2LongMap<String> result;

	public TriangleCount() {
		this.result = new Object2LongOpenHashMap<>();
	}

	@Override
	public String getName() {
		return "Triangle Count";
	}

	@Override
	public Object2LongMap<String> getResult() {
		return result;
	}

	@Override
	public void init(Vertex v) {
		// Do nothing here
	}

	@Override
	public void compute(Vertex v) {
		for (Vertex neigh : v.getVertices(Direction.BOTH)) {
			for (Vertex nneigh : neigh.getVertices(Direction.BOTH)) {
				for (Vertex last : nneigh.getVertices(Direction.BOTH)) {
					if (last.equals(v)) {
						long count = result.getOrDefault(v.getId().toString(), 0L);
						result.put(v.getId().toString(), count + 1);
					}
				}
			}
		}

		// Object vid = v.getId();
		// String vidLabel = v.getId().toString().replace(":", "");
		// for(Vertex neigh : v.getVertices(Direction.BOTH)) {
		// neigh.setProperty(vidLabel, vid);
		// }
		// for(Vertex neigh : v.getVertices(Direction.BOTH)) {
		// for( Vertex last : neigh.getVertices(Direction.BOTH) ) {
		// if( ! last.getId().equals(vid) ) {
		// if( last.getPropertyKeys().contains(vidLabel) ) {
		// long count = result.getOrDefault(v.getId().toString(), 0L);
		// result.put(v.getId().toString(), count+1);
		// }
		// }
		// }
		// }
		// for(Vertex neigh : v.getVertices(Direction.BOTH)) {
		// neigh.removeProperty(vidLabel);
		// }
	}

	@Override
	public void apply(Vertex v) {
		// do nothing here
	}

	@Override
	public void collectResult(Vertex v) {
		// Do nothing here
	}

	@Override
	public void clean(Vertex v) {
		// Do nothing here
	}

	@Override
	public int getNumIterations() {
		return 1;
	}

}
