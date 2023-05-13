package projects.aSIOTmm.models.connectivityModels;

import projects.aSIOTmm.nodes.nodeImplementations.HumanNode;
import projects.aSIOTmm.nodes.nodeImplementations.ObjectNode;
import sinalgo.models.ConnectivityModelHelper;
import sinalgo.nodes.Node;

public class SiotConnModel extends ConnectivityModelHelper {

	@Override
	protected boolean isConnected(Node from, Node to) {
		
		if((to instanceof ObjectNode) && (from instanceof HumanNode)) {
//			System.out.println("HumanNode does not connect with ObjectNode");
			return false;
		}
		
		if(from instanceof ObjectNode && to instanceof ObjectNode) {
//			System.out.println("ObjectNode does conn with ObjectNode");
			return true;
		}
		
		return false;
	}

}
