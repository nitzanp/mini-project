import java.util.HashSet;
import java.util.Set;


public class ColorVertex extends Vertex {
	
	private int id;
	private int forest;
	private int color;
	private int nextColor;	
	private RegularVertex parent;
	private Set<RegularVertex> childs;
	
	public ColorVertex() {
		this.id = -1;
		this.color = id;
		this.parent = null;
		this.childs = new HashSet<RegularVertex>();
	}
	
	public ColorVertex(int id, int forest) {
		this.id = id;
		this.color = id;
		this.forest = forest;
		this.parent = null;
		this.childs = new HashSet<RegularVertex>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getForest() {
		return forest;
	}

	public void setForest(int forest) {
		this.forest = forest;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public RegularVertex getParent() {
		return parent;
	}

	public void setParent(RegularVertex parent) {
		this.parent = parent;
	}
	
	public void setNextColor(int color) {
		this.nextColor = color;
	}
	
	public Set<RegularVertex> getChilds() {
		return this.childs;
	}
	
	public void addChild (RegularVertex child) {
		this.childs.add(child);
	}
	
	public void switchToNextColor() {
		this.color = nextColor;
	}
	
	
	@Override
	public String toString() {
//		String str = id + "";
//		str += parent != null ? "(" + parent.getId() + ")" : "";
		return id + ":" + color;
	}


}
