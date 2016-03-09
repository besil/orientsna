package orientsna.engine.algorithms;

import com.tinkerpop.blueprints.Vertex;

public interface VertexAlgorithm extends Algorithm {
	/**
	 * Initialize the node
	 * @param v
	 */
	void init(Vertex v);

	/**
	 * Compute the new value for the node
	 * @param v
	 */
	void compute(Vertex v);
	/**
	 * Apply the computed value for the node
	 * @param v
	 */
	void apply(Vertex v);

	/**
	 * Collect the result for this node
	 * @param v
	 */
	void collectResult(Vertex v);

	void clean(Vertex v);
	
	/**
	 * Specify the number of iterations
	 * @return
	 */
	int getNumIterations();
}
