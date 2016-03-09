# orientsna

OrientSNA is a collection of social network algorithms for networks written in Java, based on OrientDB.

The project uses Maven for dependency management.

I hope to deliver more algorithms in a near future.
Any help will be appreciated.

The implemented algorithms are, for now:
- [x] Connected components
- [x] Triangle Count
- [x] Label Propagation
- [ ] PageRank

## Quickstart
```java
OrientGraphFactory factory = new OrientGraphFactory("memory:test").setupPool(1, 10);
String nodeFileName = "zachary/nodes";
String edgeFileName = "zachary/edges";

// Prepare graph data
ClassLoader classLoader = OrientSNAMain.class.getClassLoader();
File nodeFile = new File(classLoader.getResource(nodeFileName).getFile());
File edgeFile = new File(classLoader.getResource(edgeFileName).getFile());
//		
GraphLoader loader = new GraphLoader();
loader.load(factory, nodeFile, edgeFile);

//		GraphUtils.printGraph(factory);

GraphAlgoEngine engine = new ParallelGraphEngine(factory);
LabelPropagation lp = new LabelPropagation();
engine.execute(lp);

Object2ObjectMap<String,String> node2community = lp.getResult();
engine.clean(lp);

//		node2community.entrySet().forEach(e -> {
//			System.out.println(e.getKey()+" -> "+e.getValue());
//		});

Map<String, Long> frequencies = node2community.values().stream()
.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()) );

frequencies.entrySet().stream().map(e -> e.getKey()+" "+e.getValue()).forEach(System.out::println);

		engine.shutdown();
		factory.close();
```
