package projects.aSIOTmm.nodes.edges;

import projects.aSIOTmm.nodes.nodeImplementations.ObjectNode;
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
		
		if(startNode.getClass() == endNode.getClass()){
			start_time = Tools.getGlobalTime();
			System.out.println("SNode=" + this.startNode.ID  + " ENode=" + this.endNode.ID + " Stime= " + start_time);
		}
	}

	@Override
	public void cleanUp() {
		super.cleanUp();
		
		end_time = Tools.getGlobalTime();
		System.out.println("SNode=" + this.startNode.ID  + 
				" ENode=" + this.endNode.ID +
				" Stime= " + start_time +
				" Etime= " + end_time);
	}
 
}
