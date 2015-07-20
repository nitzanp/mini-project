import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;


public class Main {
	
	private final static int MASK = 0x1;
	
	public static void main(String[] args) {
		DirectedGraph<RegularVertex, ColorEdge> undirect =
	            new SimpleDirectedGraph<RegularVertex, ColorEdge>(ColorEdge.class);
		buildGraph(undirect);
		System.out.println(undirect.toString());
		
		DirectedGraph<RegularVertex, ColorEdge> graph = 
				new SimpleDirectedGraph<RegularVertex, ColorEdge>(ColorEdge.class);
		undirectToDirect(undirect, graph);
		
		Map<Integer, Set<ColorEdge>> forests = forestDecomposition(graph);
			
		printByForests(forests, false);
		
		for (RegularVertex v : graph.vertexSet()) {
			System.out.println(v.getId() + ": " + v.getColorVertexes().toString());
		}
		
		threeVertexColoring(graph);
		
		printByForests(forests, true);
		
//		for(DirectedGraph<ColorVertex, ColorEdge> forest : forests.values()) {
//			threeVertexColoring(forest);
//		}
//		
//		for(DirectedGraph<ColorVertex, ColorEdge> forest : forests) {
//			treeEdgeColor(forest);
//		}
		
		//buildGraphFromForests();
		        
//        System.out.println(graph.toString());
	}
	
	private static void threeVertexColoring(DirectedGraph<RegularVertex, ColorEdge> graph) {
		firstStep(graph);
		shiftDown(graph);
		
	}

	private static void shiftDown(DirectedGraph<RegularVertex, ColorEdge> graph) {
		int nextColor;
		for (RegularVertex vertex : graph.vertexSet()) {
			for (ColorVertex v : vertex.getColorVertexes().values()) {
				ColorVertex parent = v.getParent();
				if (parent == null) {
					nextColor = (v.getColor() == 0) ? 1 : 0;
				}
				else {
					nextColor = parent.getColor();
				}
				v.setNextColor(nextColor);
			}
		}
		
		//This is just because this is not in parallel 
		for (RegularVertex vertex : graph.vertexSet()) {
			for (ColorVertex v : vertex.getColorVertexes().values()) {
				v.switchToNextColor();
			}
		}
	}
	

	private static void firstStep(DirectedGraph<RegularVertex, ColorEdge> graph) {
		int i = 0;
		int iters = calcIters(graph.vertexSet().size());
		
		while (i < iters) {	
			for (RegularVertex vertex : graph.vertexSet()) {
				for (ColorVertex v : vertex.getColorVertexes().values()) {
					int nextColor;
					if (v.getParent() == null) {
						nextColor = getRandomBit(v);
					}
					else {
						nextColor = findRigtmostBit(v, v.getParent());
					}
					v.setNextColor(nextColor);
				}
			}
			
			//This is just because this is not in parallel 
			for (RegularVertex vertex : graph.vertexSet()) {
				for (ColorVertex v : vertex.getColorVertexes().values()) {
					v.switchToNextColor();
				}
			}
			
			i++;
		}
		
		System.out.println(graph.toString());
		
	}

	private static int calcIters(double n) {
		int count = 0;
	    while (n >= 1) {
	        n = Math.log(n) / Math.log(2);		//logb(n) = log(n) / log(b)
	        count++;
	    }
	    return count;
	}

	private static int getRandomBit(ColorVertex v) {
		// TODO make it random;
		int vMask = v.getColor() & MASK;
		return vMask;		//TODO - i = 0
	}

	private static int findRigtmostBit(ColorVertex vertex, ColorVertex parent) {
		int v = vertex.getColor();
		int u = parent.getColor();
		int i = 0;
		int uMask, vMask;
		
//		String str = "v:" + v + " u:" + u;
		
		while (true) {
			vMask = v & MASK;
			uMask = u & MASK;
			if (uMask != vMask) {
				int ans = i;
				ans = ans << 1;
				ans += vMask;
				
//				System.out.println(str + " => " + ans);
				return ans;				
			}
			i++;
			v = v >> 1;
			u = u >> 1;			
		}
		
		
	}

	private static void addEdge(RegularVertex source, RegularVertex target,
			DirectedGraph<RegularVertex, ColorEdge> g) {
		ColorEdge edge = new ColorEdge();
		edge.setSource(source);
		edge.setTarget(target);
		g.addEdge(source, target, edge);
	}
	
	private static void addEdge(ColorVertex source, ColorVertex target,
			DirectedGraph<ColorVertex, ColorEdge> g) {
		ColorEdge edge = new ColorEdge();
		edge.setSource(source);
		edge.setTarget(target);
		g.addEdge(source, target, edge);
	}
		
	private static void buildGraph(DirectedGraph<RegularVertex, ColorEdge> g) {
		RegularVertex v1 = new RegularVertex(1);
		RegularVertex v2 = new RegularVertex(2);
		RegularVertex v3 = new RegularVertex(3);
		RegularVertex v4 = new RegularVertex(4);
		RegularVertex v5 = new RegularVertex(5);
		RegularVertex v6 = new RegularVertex(6);
		RegularVertex v7 = new RegularVertex(7);
		RegularVertex v8 = new RegularVertex(8);
		
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addVertex(v6);
		g.addVertex(v7);
		g.addVertex(v8);

        addEdge(v1, v2, g);
        addEdge(v1, v3, g);
        addEdge(v2, v4, g);
        addEdge(v5, v4, g);
        addEdge(v5, v8, g);
        addEdge(v7, v5, g);
        addEdge(v2, v6, g);
        addEdge(v3, v5, g);
        addEdge(v4, v1, g);
        
	}
	
	private static void undirectToDirect(DirectedGraph<RegularVertex, ColorEdge> undirect, DirectedGraph<RegularVertex, ColorEdge> direct) {
		Set<ColorEdge> sourceEdges = undirect.edgeSet();
		
		for (RegularVertex v : undirect.vertexSet()) {
			direct.addVertex(v);
		}
		
		for (ColorEdge edge : sourceEdges) {
			RegularVertex v1 = (RegularVertex) edge.getSource();		
			RegularVertex v2 = (RegularVertex) edge.getTarget();		
			
			if (v2.isSource(v1)) {
				addEdge(v2, v1, direct);
			}
			else {
				addEdge(v1, v2, direct);
			}
			
		}
	}
	
	
	private static Map<Integer, Set<ColorEdge>> forestDecomposition(DirectedGraph<RegularVertex, ColorEdge> graph) {
		Map<Integer, Set<ColorEdge>> forests = 
				new HashMap<Integer, Set<ColorEdge>>();
		
		for (RegularVertex vertex : graph.vertexSet()) {
			int i = 1;
			Set<ColorEdge> edges = graph.edgesOf(vertex);
			for (ColorEdge edge : edges) {
				if (edge.getSource() == vertex) {
					edge.setForestId(i);
					Set<ColorEdge> forest;
					if (forests.containsKey(i)) {
						forest = forests.get(i);
					}
					else {
						forest = new HashSet<ColorEdge>();
					}
					RegularVertex target = (RegularVertex)edge.getTarget();
					vertex.addToForest(i);
					target.addToForest(i);
					forest.add(edge);
					forests.put(i, forest);	
										
					i++;
				}
			}
		}
		
		for (java.util.Map.Entry<Integer, Set<ColorEdge>> forest : forests.entrySet()) {
			int i = forest.getKey();
			for (ColorEdge edge : forest.getValue()) {
				RegularVertex source = (RegularVertex) edge.getSource();
				RegularVertex target = (RegularVertex) edge.getTarget();
				target.setParent(source, i);
			}			
		}
		
		return forests;
	}
	
	public static void printByForests(Map<Integer, Set<ColorEdge>> forests, boolean color) {
		System.out.println();
		for (java.util.Map.Entry<Integer, Set<ColorEdge>> forest : forests.entrySet()) {
			int i = forest.getKey();
			String out = "F" + i + ": ";
			for (ColorEdge edge : forest.getValue()) {
				RegularVertex source = (RegularVertex) edge.getSource();
				RegularVertex target = (RegularVertex) edge.getTarget();
				
				if (color) {
					out += "(" + source.getColorVertexAt(i);
					out += "," + target.getColorVertexAt(i) + ")";
				}
				else {
					out += "(" + source.toString() + "," + target.toString() + ")";
				}
			}
			System.out.println(out);
		}
		System.out.println();
	}




	

}
