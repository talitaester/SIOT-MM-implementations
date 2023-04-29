package projects.aSIOTmm.nodes.nodeImplementations;

import sinalgo.configuration.WrongConfigurationException;

import sinalgo.gui.transformation.PositionTransformation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import projects.defaultProject.models.mobilityModels.NoMobility;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.messages.Inbox;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;

public class NodeHouse extends Node {
	  Vector<ObjectNode> objects_at_home = null;
	  int owner_id = -1;
	  HumanNode owner = null;
	  Position home_position = null;
	  

	  public NodeHouse(int owner_id, HumanNode owner){
	    super();
	    this.owner = owner;
	    this.home_position.assign(owner.getPosition()); // it`s not supposed to change bc houses usually don`t move!
	    this.setMobilityModel(new NoMobility());
		this.objects_at_home = new Vector<ObjectNode>();
		this.setConnectivityModel(owner.getConnectivityModel());
		this.setInterferenceModel(owner.getInterferenceModel());
		this.setReliabilityModel(owner.getReliabilityModel());
	    
	  }

	   public void takeObject(ObjectNode obj) {
		 if(this.objects_at_home.contains(obj)) {
				this.objects_at_home.remove(obj);
			}
	  }

	  public void leftObject(ObjectNode object) {
	    this.objects_at_home.add(object);
	  }
	  
	  public Vector<ObjectNode> getObjects() {
		  return this.objects_at_home;
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
	

	@Override
	public String toString() {
		return "HouseNode [my_owner_id= " + owner_id + "| owner_node= " + owner + "| objects_at_home" + objects_at_home + "]";
	}
	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// set the color of this node
		this.setColor(new Color(106, 33, 189));	
		String text = this.objects_at_home.toString();
		super.drawNodeAsSquareWithText(g, pt, highlight, text, 4, Color.BLACK);
	}


}
