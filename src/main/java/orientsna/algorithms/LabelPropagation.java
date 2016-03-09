package orientsna.algorithms;

import java.util.Map.Entry;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import orientsna.engine.algorithms.VertexAlgorithm;

public class LabelPropagation implements VertexAlgorithm {
//	private Logger log = Logger.getLogger(LabelPropagation.class.getName());
	
	public static String LP_COMMUNITY_KEY = "lpCommunity";
	public static String LP_COMPUTED_COMMUNITY_KEY = "lpCommunity_computed";
	private final Object2ObjectMap<String, String> result;
	
	public LabelPropagation() {
		this.result = new Object2ObjectOpenHashMap<>();
	}
	
	@Override
	public String getName() {
		return "LabelPropagation";
	}

	@Override
	public Object2ObjectMap<String, String> getResult() {
		return result;
	}

	@Override
	public void init(Vertex v) {
		v.setProperty(LP_COMMUNITY_KEY, v.getId().toString());
	}

	@Override
	public void apply(Vertex v) {
		String newLabel = v.getProperty(LP_COMPUTED_COMMUNITY_KEY);
		// v.removeProperty(LP_COMPUTED_COMMUNITY_KEY);
		v.setProperty(LP_COMMUNITY_KEY, newLabel);
	}
	
	@Override
	public void compute(Vertex v) {
		String mostFrequentLabel = this.getMostFrequentLabel(v);
		v.setProperty(LP_COMPUTED_COMMUNITY_KEY, mostFrequentLabel);
	}
	
	protected String getMostFrequentLabel(Vertex node) {
		Object2LongMap<String> commMap = new Object2LongOpenHashMap<>();

		Iterable<Vertex> neighbours = node.getVertices(Direction.IN);
		for(Vertex other : neighbours) {
			String otherCommunity = other.getProperty(LP_COMMUNITY_KEY);
			long count = commMap.getOrDefault(otherCommunity, 0L);
			commMap.put(otherCommunity, count+1);
		}
		
		for(Vertex other : node.getVertices(Direction.OUT)) {
			String otherCommunity = other.getProperty(LP_COMMUNITY_KEY);
			long count = commMap.getOrDefault(otherCommunity, 0L);
			commMap.put(otherCommunity, count+1);
		}
		
		String mostFrequentLabel = node.getId().toString();
		long mostFrequentLabelCount = -1;
		
		for( Entry<String, Long> e : commMap.entrySet() ) {
			if( e.getValue() > mostFrequentLabelCount || (e.getValue() == mostFrequentLabelCount && Math.random() > 0.5) ) {
				mostFrequentLabelCount = e.getValue();
				mostFrequentLabel = e.getKey();
			}
		}
		
		return mostFrequentLabel;
	}

	@Override
	public void collectResult(Vertex v) {
		String id = v.getId().toString();
		String community = v.getProperty(LP_COMMUNITY_KEY);
		this.result.put(id, community);
	}
	
	@Override
	public void clean(Vertex v) {
		v.removeProperty(LP_COMMUNITY_KEY);
		v.removeProperty(LP_COMPUTED_COMMUNITY_KEY);
	}

	@Override
	public int getNumIterations() {
		return 20;
	}

}
