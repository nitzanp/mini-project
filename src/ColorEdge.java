import org.jgraph.graph.DefaultEdge;


public class ColorEdge extends DefaultEdge {

	private int color;
	private int forestId;
	
	public ColorEdge() {
		super();
		this.color = 0;
		this.forestId = 0;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public int getForestId() {
		return forestId;
	}

	public void setForestId(int forestId) {
		this.forestId = forestId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "color: " + color;
	}

}
