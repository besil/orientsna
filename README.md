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
// Prepare your database
OrientGraphFactory factory = new OrientGraphFactory("memory:test").setupPool(1, 10);

// Prepare graph data
String nodeFileName = "zachary/nodes";
String edgeFileName = "zachary/edges";
ClassLoader classLoader = OrientSNAMain.class.getClassLoader();
File nodeFile = new File(classLoader.getResource(nodeFileName).getFile());
File edgeFile = new File(classLoader.getResource(edgeFileName).getFile());

// GraphLoader is an example utility class for loading grpah data 
GraphLoader loader = new GraphLoader();
loader.load(factory, nodeFile, edgeFile);

// Print the graph, if you want
//		GraphUtils.printGraph(factory);

// Prepare the graph engine
// There are two implementations: SingleGraphEngine and ParallelGraphEngine
// Parallel graph engine uses all available cores 
GraphAlgoEngine engine = new ParallelGraphEngine(factory);
LabelPropagation lp = new LabelPropagation();
engine.execute(lp);

Object2ObjectMap<String,String> node2community = lp.getResult();
// clean actually removes data the algorithm has written into the database
engine.clean(lp);

// Get the size of communities
Map<String, Long> communitySizes = node2community
    .values()
    .stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()) );

communitySizes
    .entrySet()
    .stream()
    .map(e -> e.getKey()+" "+e.getValue())
    .forEach(System.out::println);

engine.shutdown();
factory.close();
```
