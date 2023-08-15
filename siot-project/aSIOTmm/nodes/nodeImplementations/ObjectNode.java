package projects.aSIOTmm.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.aSIOTmm.CustomGlobal;
import projects.defaultProject.models.mobilityModels.NoMobility;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.models.MobilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;

public class ObjectNode extends Node {

	ObjType type = null;
	int my_owner_id = -1;
	Node owner_node = null;
	ObjMobilityProb mob_prob = null;

	public ObjectNode(ObjType type, Node owner, int owner_id, MobilityModel mm, ObjMobilityProb p) {
		super();
		this.type = type;

		this.my_owner_id = owner_id;
		this.owner_node = owner;

		this.mobilityModel = mm;
		this.mob_prob = p;
		CustomGlobal.info.logln("" + this.ID + ", " + my_owner_id + ", " + this.type);
		CustomGlobal.home_positions.logln("" + this.ID + ", " + this.owner_node.getPosition().xCoord + ", " + this.owner_node.getPosition().yCoord );

	}

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub

	}
	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// set the color of this node
		this.setColor(new Color(52, 219, 235));	
		String text = Integer.toString(this.ID) + "|" + Integer.toString(this.my_owner_id) + "|" + this.type.toString();
		super.drawNodeAsSquareWithText(g, pt, highlight, text, 4, Color.pink);
	}

	@Override
	public String toString() {
		return "ObjectNode [type=" + type + ", my_owner_id=" + my_owner_id + ", owner_node=" + owner_node
				+ ", mob_prob=" + mob_prob + ", ID=" + ID + "]";
	}

	

}
