package projects.aSIOTmm.models.mobilityModels;

import sinalgo.nodes.Position;

public class Cell extends Position {
	
	
	//The cell position x,y,z represent the center of the cell.
	
	public int noOfNodesPresent;

	public Cell(int noOfNodesPresent) {
		super();
		this.noOfNodesPresent = noOfNodesPresent;
	}


	
}
