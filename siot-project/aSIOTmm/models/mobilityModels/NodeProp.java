package projects.aSIOTmm.models.mobilityModels;

import java.util.Vector;

import sinalgo.nodes.Position;

public class NodeProp implements Comparable {

	
	public int seen = 0;
	public double weight = 0;
	public int cell_map_id = 0;
	public CellType cell_type = CellType.UNDEFINED;
	public int num_picks; // number of times that this loc was picked
	public double distance; // distance to home position

	public NodeProp() {
		super();
		this.seen = 0;
		this.weight = 0;
		this.cell_type = CellType.UNDEFINED;
	}

	public NodeProp(int seen, double weight, int cell_map_id, double distance, CellType cell_type) {
		super();
		this.seen = seen;
		this.weight = weight;
		this.cell_map_id = cell_map_id;
		this.cell_type = cell_type;
		this.num_picks = 0;
		this.distance = distance;
	}


	@Override
	public int compareTo(Object arg0) {
		NodeProp b = (NodeProp) arg0;

		if (this.weight < b.weight) {
			return 1;
		}
		if (this.weight > b.weight) {
			return -1;
		}
		return 0;
	}

	public void increaseLocationPicks() {
		this.num_picks++;
	}

	@Override
	public String toString() {
		return "NodeProp [seen=" + seen + ", distance=" + distance +  ", weight=" + weight + ", cell_map_id=" + cell_map_id + ", cell_type="
				+ cell_type + ", num_picks=" + num_picks +"]";
	}

}
