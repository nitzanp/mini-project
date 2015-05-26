import java.util.HashSet;
import java.util.Set;


public class ColorVertex {
	
	private int id;
	private Set<Integer> lv;
	private int color;
	
	public ColorVertex() {
		this.id = -1;
		this.lv = new HashSet<Integer>();
		this.color = 0;
	}
	
	public ColorVertex(int id) {
		this.id = id;
		this.lv = new HashSet<Integer>();
		this.color = 0;
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
	
	public boolean isSource(ColorVertex v) {
		return this.id < v.getId();
	}
	
	@Override
	public String toString() {
		return id + "";
	}

}
