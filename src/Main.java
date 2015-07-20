import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;


public class Main {
	
	public static void main(String[] args) {
		DirectedGraph<ColorVertex, ColorEdge> graph =
	            new SimpleDirectedGraph<ColorVertex, ColorEdge>(ColorEdge.class);
		buildGraph(graph);
		System.out.println(graph.toString());
		
		undirectToDirect(graph);
		
		Map<Integer, DirectedGraph<ColorVertex, ColorEdge>> forests= forestDecomposition(graph);
		
		System.out.println(forests.toString());
		
		int i = 1;
		for (DirectedGraph<ColorVertex, ColorEdge> forest : forests.values()) {
			System.out.println("F" + i + ": " + forest.toString());
			i++;
		}
		
		for(DirectedGraph<ColorVertex, ColorEdge> forest : forests.values()) {
			threeVertexColoring(forest);
		}
//		
//		for(DirectedGraph<ColorVertex, ColorEdge> forest : forests) {
//			treeEdgeColor(forest);
//		}
		
		//buildGraphFromForests();
		        
        System.out.println(graph.toString());
	}
	
	private static void threeVertexColoring(DirectedGraph<ColorVertex, ColorEdge> forest) {
		firstStep(forest);
		
	}

	private static void firstStep(DirectedGraph<ColorVertex, ColorEdge> forest) {
		
		
	}

	private static void addEdge(ColorVertex source, ColorVertex target,
			DirectedGraph<ColorVertex, ColorEdge> g) {
		ColorEdge edge = new ColorEdge();
		edge.setSource(source);
		edge.setTarget(target);
		g.addEdge(source, target, edge);
	}
		
	private static void buildGraph(DirectedGraph<ColorVertex, ColorEdge> g) {
		ColorVertex v1 = new ColorVertex(1);
		ColorVertex v2 = new ColorVertex(2);
		ColorVertex v3 = new ColorVertex(3);
		ColorVertex v4 = new ColorVertex(4);
		
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);

        addEdge(v1, v2, g);
        addEdge(v2, v3, g);
        addEdge(v3, v4, g);
        addEdge(v4, v1, g);
        
	}
	
	private static void undirectToDirect(DirectedGraph<ColorVertex, ColorEdge> graph) {
		Set<ColorEdge> sourceEdges = graph.edgeSet();
		for (ColorEdge edge : sourceEdges) {
			ColorVertex v1 = (ColorVertex) edge.getSource();		
			ColorVertex v2 = (ColorVertex) edge.getTarget();		
			
			if (v2.isSource(v1)) {
				graph.removeEdge(edge);
				addEdge(v2, v1, graph);
				v1.setParent(v2);
			}
			else {
				v2.setParent(v1);
			}
			
		}
	}
	
	
	private static Map<Integer, DirectedGraph<ColorVertex, ColorEdge>> forestDecomposition(
			DirectedGraph<ColorVertex, ColorEdge> graph) {
		Map<Integer, DirectedGraph<ColorVertex, ColorEdge>> forests = 
				new HashMap<Integer, DirectedGraph<ColorVertex,ColorEdge>>();
		//forests.add(new SimpleDirectedGraph<ColorVertex, ColorEdge>(ColorEdge.class));
		
		Set<ColorVertex> vertexs = graph.vertexSet();
		for (ColorVertex vertex : vertexs) {
			int i = 1;
			Set<ColorEdge> edges = graph.edgesOf(vertex);
			for (ColorEdge edge : edges) {
				if (edge.getSource() == vertex) {
					edge.setForestId(i);
					DirectedGraph<ColorVertex, ColorEdge> forest;
					if (forests.containsKey(i)) {
						forest = forests.get(i);
					}
					else {
						forest = new SimpleDirectedGraph<ColorVertex, ColorEdge>(ColorEdge.class);
					}
					ColorVertex target = (ColorVertex)edge.getTarget();
					if (!forest.containsVertex(vertex)) {
						forest.addVertex(vertex);
					}
					if (!forest.containsVertex(target)) {
						forest.addVertex(target);
					}
					forest.addEdge(vertex, target, edge);
					forests.put(i, forest);					
					
					i++;
				}
			}
		}
		
		System.out.println(forests.toString());
		return forests;
	}




	

}
