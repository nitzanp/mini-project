import java.util.HashSet;
import java.util.Set;


public class ColorVertex extends Vertex {
	
	private int id;
	private Set<Integer> lv;
	private int color;
	private int nextColor;	//temp
	private ColorVertex parent;
	
	public ColorVertex() {
		this.id = -1;
		this.lv = new HashSet<Integer>();
		this.color = id;
		this.parent = null;
	}
	
	public ColorVertex(int id) {
		this.id = id;
		this.lv = new HashSet<Integer>();
		this.color = id;
		this.parent = null;
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public ColorVertex getParent() {
		return parent;
	}

	public void setParent(ColorVertex parent) {
		this.parent = parent;
	}
	
	public void setNextColor(int color) {
		this.nextColor = color;
	}
	
	public void switchToNextColor() {
		this.color = nextColor;
	}
	
	public void vertexForestEdgeColor() {
		for (int i = 0; i < 3; i++) {
			
		}
		
	}
	
	@Override
	public String toString() {
//		String str = id + "";
//		str += parent != null ? "(" + parent.getId() + ")" : "";
		return id + ":" + color;
	}

}
