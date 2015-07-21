import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RegularVertex extends Vertex {

	private Map<Integer, ColorVertex> colorVertexes;
	private int id;
	private Set<Integer> lv;
	private Set<Integer> mailBox;		//this is juse a tmp way to send messages.

	
	public RegularVertex() {
		this.id = -1;
		this.lv = new HashSet<Integer>();
		this.colorVertexes = new HashMap<Integer, ColorVertex>();
	}
	
	public RegularVertex(int id) {
		this.id = id;
		this.lv = new HashSet<Integer>();
		this.colorVertexes = new HashMap<Integer, ColorVertex>();
		this.mailBox = new HashSet<Integer>();
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

	public void addToLv(int color) {
		this.lv.add(color);
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
			colorVertexes.put(i, new ColorVertex(id, i));
		}
	}

	public void setParent(RegularVertex parent, int i) {
		colorVertexes.get(i).setParent(parent);		
	}
	
	public void addChild(RegularVertex child, int i) {
		colorVertexes.get(i).addChild(child);
	}
	
	public void send(int color) {
		this.mailBox.add(color);
	}
	
	public void readMailBox() {
		for (int color : mailBox) {
			lv.add(color);
		}
	}
	
	@Override
	public String toString() {
		return id + "";
	}
	
}
