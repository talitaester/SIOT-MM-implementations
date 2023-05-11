package projects.aSIOTmm.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.Tools;


/**
 * 
 * @author bruno
 * This edge class measures the contact time.
 */
public class CTedges extends BidirectionalEdge {
	private double start_time, end_time;
	
	
	@Override
	public void initializeEdge() {
		super.initializeEdge();
		
		start_time = Tools.getGlobalTime();
		System.out.println("SNode=" + this.startNode.ID  + 
				" ENode=" + this.endNode.ID +
				" Stime= " + start_time);
		//append the output in a dataframe
		startCsvPrinter.printRecord(this.startNode.ID, this.endNode.ID, start_time);
	
	}

	@Override
	public void cleanUp() {
		super.cleanUp();
		
		end_time = Tools.getGlobalTime();
		System.out.println("SNode=" + this.startNode.ID  + 
				" ENode=" + this.endNode.ID +
				" Stime= " + start_time +
				" Etime= " + end_time);
		//append the output in a dataframe
		endCsvPrinter.printRecord(this.startNode.ID, this.endNode.ID, end_time);
		
	}
	
	
 
}
