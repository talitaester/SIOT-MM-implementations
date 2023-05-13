package projects.aSIOTmm.models.mobilityModels;

import projects.aSIOTmm.CustomGlobal;
import projects.aSIOTmm.nodes.nodeImplementations.HumanNode;
import sinalgo.models.MobilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.tools.Tools;

public class FollowOwner extends MobilityModel {

	int owner_id = -1;
	Node owner = null;

	public FollowOwner(int owner_id, Node owner) {
		super();
		this.owner_id = owner_id;
		this.owner = owner;
	}

	@Override
	public Position getNextPos(Node n) {

		CustomGlobal.pos_trace.logln("" + n.ID + ", " + n.getPosition().xCoord + ", "
				+ n.getPosition().yCoord + ", " + Tools.getGlobalTime());

		return this.owner.getPosition();
	}

}
