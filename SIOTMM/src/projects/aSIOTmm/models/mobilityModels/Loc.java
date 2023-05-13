package projects.aSIOTmm.models.mobilityModels;


import java.util.*;  

import sinalgo.nodes.Position;

public class Loc extends Position {
	public int no_of_nodes_present;
	public int cell_id;
	public CellType cell_type;
	public Set<Integer> seen_nodes_ids;

	public Loc() {
		super();
		this.no_of_nodes_present = 0;
	}

	public Loc(double x, double y, double z, int noOfNodesPresent, int cell_id, CellType cell_type) {
		super(x, y, z);
		this.no_of_nodes_present = noOfNodesPresent;
		this.cell_id = cell_id;
		this.cell_type = cell_type;
		this.seen_nodes_ids = new HashSet<Integer>();
	}

	public Loc(double x, double y, double z) {
		super(x, y, z);
		this.no_of_nodes_present = 0;
		this.cell_id = 0;
	}

	public int getNoOfNodesPresent() {
		return no_of_nodes_present;
	}

	public void setNoOfNodesPresent(int noOfNodesPresent) {
		this.no_of_nodes_present = noOfNodesPresent;
	}

	@Override
	public String toString() {
		return "Loc [no_of_nodes_present=" + no_of_nodes_present + ", cell_id=" + cell_id
				+ ", cell_type=" + cell_type + ", xCoord=" + xCoord + ", yCoord=" + yCoord + ", zCoord=" + zCoord + "]";
	}
	
}

