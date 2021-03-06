import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;


public class Main {
	
	private final static int MASK = 0x1;
	private static int DELTA;
	
	public static void main(String[] args) {
		DirectedGraph<RegularVertex, ColorEdge> undirect =
	            new SimpleDirectedGraph<RegularVertex, ColorEdge>(ColorEdge.class);
		buildGraph(undirect);
		System.out.println(undirect.toString());
		
		DirectedGraph<RegularVertex, ColorEdge> graph = 
				new SimpleDirectedGraph<RegularVertex, ColorEdge>(ColorEdge.class);
		undirectToDirect(undirect, graph);
		
		Map<Integer, Set<ColorEdge>> forests = forestDecomposition(graph);
		
		DELTA = forests.size();
			
		printByForests(forests, false);
		
		for (RegularVertex v : graph.vertexSet()) {
			System.out.println(v.getId() + ": " + v.getColorVertexes().toString());
		}
		
		threeVertexColoring(graph);
		
		printByForests(forests, true);
			
		for (Entry<Integer, Set<ColorEdge>> forest : forests.entrySet()) {
			forestEdgeColor(forest.getKey(), forest.getValue());
		}
		
		//buildGraphFromForests();
		        
//        System.out.println(graph.toString());
	}
	
	private static void forestEdgeColor(Integer forest, Set<ColorEdge> edges) {
		
		//get the vertexes, is there another way?
		Set<RegularVertex> vertexes = new HashSet<RegularVertex>();
		for (ColorEdge edge : edges) {
			RegularVertex source = (RegularVertex) edge.getSource();
			RegularVertex target = (RegularVertex) edge.getTarget();
			vertexes.add(source);
			vertexes.add(target);
		}
		
		for (RegularVertex vertex : vertexes) {
			vertexForestEdgeColor(vertex, forest);
		}
		
	}
	
	public static void vertexForestEdgeColor(RegularVertex v, int forest) {
		//vertex is the colorVertex of v in this forest.
		ColorVertex vertex = v.getColorVertexAt(forest);
		for (int i = 0; i < 3; i++) {		//maybe move this loop out from this method.
			if (vertex.getColor() == i) {
				for (RegularVertex w : vertex.getChilds()) {
					int nextColor = findFreeColor(v, w);
					v.addToLv(nextColor);
					//TODO - should mark nextColor as the color of the edge (v,w)
					
					//SEND phi(v,w)=nextColor to w
					w.send(nextColor);
				}
			}
			
			//RECIVE messages
			v.readMailBox();
			
			//SEND lv to all neighbors		
		}
		
	}

	private static int findFreeColor(RegularVertex v, RegularVertex w) {
		for (int i = 0; i < 2 * DELTA - 1; i++) {
			if (!v.getLv().contains(i) && !w.getLv().contains(i)) {
				return i;
			}
		}
		return -1;
	}

	private static void threeVertexColoring(DirectedGraph<RegularVertex, ColorEdge> graph) {
		firstStep(graph);
		shiftDown(graph);
		elimination(graph);	
	}

	private static void elimination(DirectedGraph<RegularVertex, ColorEdge> graph) {
		for (int i = 3; i < 6; i++) {
			elimination(graph, i);
		}
		
	}

	private static void elimination(DirectedGraph<RegularVertex, ColorEdge> graph, int alpha) {
				
		for (RegularVertex vertex : graph.vertexSet()) {
			for (ColorVertex v : vertex.getColorVertexes().values()) {
				int forest = v.getForest();
				// SEND my color to all of my neighbors.

				if (v.getColor() == alpha) {
					
					//RECIVE messages from my neighbors
					//temp way to get the messages
					Set<Integer> messages = new HashSet<Integer>();
					messages.add(v.getParent().getColorVertexAt(forest).getColor());
					for (RegularVertex child : v.getChilds()) {
						messages.add(child.getColorVertexAt(forest).getColor());
					}
					
					int nextColor = findFreeColor(messages);
					v.setColor(nextColor);
				}			
			}		
		}		
	}

	private static int findFreeColor(Set<Integer> messages) {
		for (int i = 0; i < DELTA + 1; i++) {
			if (!messages.contains(i)) {
				return i;
			}
		}
		return -1;
	}

	private static void shiftDown(DirectedGraph<RegularVertex, ColorEdge> graph) {
		int nextColor;
		for (RegularVertex vertex : graph.vertexSet()) {
			for (ColorVertex v : vertex.getColorVertexes().values()) {
				int forest = v.getForest();
				ColorVertex parent = v.getParent().getColorVertexAt(forest);
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
					ColorVertex parent = v.getParent().getColorVertexAt(v.getForest());
					if (parent == null) {
						nextColor = getRandomBit(v);
					}
					else {
						nextColor = findRigtmostBit(v, parent);
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
	    while (n >= 1) {		//check if it 2
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
		
		for (Entry<Integer, Set<ColorEdge>> forest : forests.entrySet()) {
			int i = forest.getKey();
			for (ColorEdge edge : forest.getValue()) {
				RegularVertex source = (RegularVertex) edge.getSource();
				RegularVertex target = (RegularVertex) edge.getTarget();
				target.setParent(source, i);
				source.addChild(target, i);
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
