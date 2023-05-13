package projects.aSIOTmm.models.mobilityModels;

import sinalgo.nodes.Position;

public class Cell extends Position {
	
	
	//The cell position x,y,z represent the center of the cell.
	
	public int noOfNodesPresent;

	public Cell(int noOfNodesPresent) {
		super();
		this.noOfNodesPresent = noOfNodesPresent;
	}

	public Cell() {
		super();
		this.noOfNodesPresent = 0;
	}

	@Override
	public String toString() {
		return "Cell [noOfNodesPresent=" + noOfNodesPresent + ", xCoord=" + xCoord + ", yCoord=" + yCoord + ", zCoord="
				+ zCoord + "]";
	}
	
	/*public double x_center;
	public double y_center;
	public double z_center;
	
	public int noOfNodesPresent;

	public Cell(double x_center, double y_center, double z_center, int noOfNodesPresent) {
		super();
		this.x_center = x_center;
		this.y_center = y_center;
		this.z_center = z_center;
		this.noOfNodesPresent = noOfNodesPresent;
	}

	public Cell() {
		super();
		this.x_center = 0;
		this.y_center = 0;
		this.z_center = 0;
		this.noOfNodesPresent = 0;
	}*/
	
	
}
