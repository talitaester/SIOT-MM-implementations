package projects.aSIOTmm.nodes.edges;

import projects.aSIOTmm.CustomGlobal;
import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.Tools;

/**
 * 
 * @author bruno This edge class measures the contact time.
 */
public class CTedges extends BidirectionalEdge {
	private double start_time, end_time;

	@Override
	public void initializeEdge() {
		super.initializeEdge();

		start_time = Tools.getGlobalTime();
//		System.out.println("SNode=" + this.startNode.ID  + 
//				" ENode=" + this.endNode.ID +
//				" Stime= " + start_time);

	}

	@Override
	public void cleanUp() {
		super.cleanUp();

		end_time = Tools.getGlobalTime();
//		  System.out.println("SNode=" + this.startNode.ID  + 
//				  System.out.println("SNode=" + this.startNode.ID  + 
//				" ENode=" + this.endNode.ID +
//				" Stime= " + start_time +
//				" Etime= " + end_time);

		if (end_time - start_time > 30) {
			CustomGlobal.contacts_trace.logln("" + this.startNode.ID + ", " + this.endNode.ID + ", " + start_time + ", "
					+ end_time + ", " + this.startNode.getPosition().xCoord + ", " + this.startNode.getPosition().yCoord
					+ ", " + this.endNode.getPosition().xCoord + ", " + this.endNode.getPosition().yCoord);
		}
	}

}
