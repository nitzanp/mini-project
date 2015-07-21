import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RegularVertex extends Vertex {

	private Map<Integer, ColorVertex> colorVertexes;
	private int id;
	private Set<Integer> lv;
	
	public RegularVertex() {
		this.id = -1;
		this.lv = new HashSet<Integer>();
		this.colorVertexes = new HashMap<Integer, ColorVertex>();
	}
	
	public RegularVertex(int id) {
		this.id = id;
		this.lv = new HashSet<Integer>();
		this.colorVertexes = new HashMap<Integer, ColorVertex>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Integer> getLv() {
		return lv;
	}

	public void setLv(Set<Integer> lv) {
		this.lv = lv;
	}

	public Map<Integer, ColorVertex> getColorVertexes() {
		return colorVertexes;
	}

	public void setColorVertexes(Map<Integer, ColorVertex> colorVertexes) {
		this.colorVertexes = colorVertexes;
	}
	
	public ColorVertex getColorVertexAt(int i) {
		return colorVertexes.get(i);
	}
	
	public boolean isSource(RegularVertex v1) {
		return this.id < v1.getId();
	}

	public void addToForest(int i) {
		if (!colorVertexes.containsKey(i)) {
			colorVertexes.put(i, new ColorVertex(id));
		}
	}

	public void setParent(RegularVertex parent, int i) {
		colorVertexes.get(i).setParent(parent.getColorVertexAt(i));		
	}
	
	public void addChild(RegularVertex child, int i) {
		colorVertexes.get(i).addChild(child.getColorVertexAt(i));
	}
	
	@Override
	public String toString() {
		return id + "";
	}
	
}
