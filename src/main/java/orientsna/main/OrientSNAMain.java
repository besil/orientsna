package orientsna.main;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import orientsna.algorithms.LabelPropagation;
import orientsna.engine.GraphAlgoEngine;
import orientsna.engine.impl.ParallelGraphEngine;
import orientsna.loader.GraphLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrientSNAMain {
    public static void init(OrientGraphFactory factory, String nodeFileName, String edgeFileName) throws IOException {
        // Prepare graph data
        ClassLoader classLoader = OrientSNAMain.class.getClassLoader();
        File nodeFile = new File(classLoader.getResource(nodeFileName).getFile());
        File edgeFile = new File(classLoader.getResource(edgeFileName).getFile());
//
        GraphLoader loader = new GraphLoader();
        loader.load(factory, nodeFile, edgeFile);
    }

    public static void compute(OrientGraphFactory factory) {

//		GraphUtils.printGraph(factory);
        GraphAlgoEngine engine = new ParallelGraphEngine(factory);
        LabelPropagation lp = new LabelPropagation();
        engine.execute(lp);

        Object2ObjectMap<String, String> node2community = lp.getResult();
        engine.clean(lp);

//		node2community.entrySet().forEach(e -> {
//			System.out.println(e.getKey()+" -> "+e.getValue());
//		});

        Map<String, Long> frequencies = node2community.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        frequencies.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).forEach(System.out::println);

        engine.shutdown();
    }

    public static void main(String[] args) throws Exception {
//		OrientGraphFactory factory = new OrientGraphFactory("remote:localhost/karate", "root", "root");
//		OrientGraphFactory factory = new OrientGraphFactory("memory:test").setupPool(1, 10);
        OrientGraphFactory factory = new OrientGraphFactory("plocal:db").setupPool(1, 10);
        String nodeFileName = "nodes";
        String edgeFileName = "edges";

//		init(factory, nodeFileName, edgeFileName);
        compute(factory);

		factory.close();
	}
}
