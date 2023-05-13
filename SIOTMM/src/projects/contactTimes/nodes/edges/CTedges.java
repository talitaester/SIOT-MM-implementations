package projects.contactTimes.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.Tools;

public class CTedges extends BidirectionalEdge {
	private double start_time, end_time;
	
	
	@Override
	public void initializeEdge() {
		// TODO Auto-generated method stub
		super.initializeEdge();
		
		start_time = Tools.getGlobalTime();
		System.out.println("SNode=" + this.startNode.ID  + 
				" ENode=" + this.endNode.ID +
				" Stime= " + start_time);
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		super.cleanUp();
		
		end_time = Tools.getGlobalTime();
		System.out.println("SNode=" + this.startNode.ID  + 
				" ENode=" + this.endNode.ID +
				" Stime= " + start_time +
				" Etime= " + end_time);
	}
 
}
