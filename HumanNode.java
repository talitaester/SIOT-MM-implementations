package projects.aSIOTmm.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import projects.aSIOTmm.CustomGlobal;
import projects.aSIOTmm.models.mobilityModels.FollowOwner;
import projects.aSIOTmm.models.mobilityModels.Loc;
import projects.aSIOTmm.models.mobilityModels.NodeProp;
import projects.aSIOTmm.models.mobilityModels.SWIM;
import projects.defaultProject.models.mobilityModels.NoMobility;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.models.ConnectivityModel;
import sinalgo.models.InterferenceModel;
import sinalgo.models.MobilityModel;
import sinalgo.models.Model;
import sinalgo.models.ReliabilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.messages.Inbox;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.UniformDistribution;

public class HumanNode extends Node {
	
	Vector<ObjectNode> my_objects = null;
	NodeHouse home = null;
	
	public HumanNode() {
		this.home = new NodeHouse(this.ID, this);
	}
	

	public void createObject() {
		ObjectNode tmp_obj;
		
		// baseado-se nas prob. crie objetos
		 // sm-tv 38, sm-phone 94
		
		this.my_objects = new Vector<ObjectNode>();
		
		//smartphone
		if(UniformDistribution.nextUniform(0, 1) <= 0.94) {
			System.out.println("Creating smartphone");

			tmp_obj = new ObjectNode(ObjType.SP, this, this.ID, new FollowOwner(this.ID, this), ObjMobilityProb.HIGH);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}
		
		//tablet
		if(UniformDistribution.nextUniform(0, 1) <= 0.94) {
			System.out.println("Creating tablet");

			tmp_obj = new ObjectNode(ObjType.TAB, this, this.ID, new FollowOwner(this.ID, this), ObjMobilityProb.MEDIUM);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}
		//smartwatch
		if(UniformDistribution.nextUniform(0, 1) <= 0.13) {
			System.out.println("Creating smartwatch");

			tmp_obj = new ObjectNode(ObjType.SW, this, this.ID, new FollowOwner(this.ID, this), ObjMobilityProb.HIGH);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}
		
		//laptop
		if(UniformDistribution.nextUniform(0, 1) <= 0.69) {
			System.out.println("Creating laptop");

			tmp_obj = new ObjectNode(ObjType.LT, this, this.ID, new FollowOwner(this.ID, this), ObjMobilityProb.MEDIUM);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}
		
		//smartTV
		if(UniformDistribution.nextUniform(0, 1) <= 0.38) {
			System.out.println("Creating TV");

			tmp_obj = new ObjectNode(ObjType.TV, this, this.ID, new NoMobility(), ObjMobilityProb.LOW);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}

		// games console 
		if(UniformDistribution.nextUniform(0, 1) <= 0.21) {
			System.out.println("Creating game console");

			tmp_obj = new ObjectNode(ObjType.CON, this, this.ID, new NoMobility(), ObjMobilityProb.LOW);
			
			tmp_obj.setPosition(this.getPosition());
			
			tmp_obj.setConnectivityModel(this.getConnectivityModel());
			tmp_obj.setInterferenceModel(this.getInterferenceModel());
			tmp_obj.setReliabilityModel(this.getReliabilityModel());
			
			Tools.getRuntime().addNode(tmp_obj);
			this.my_objects.add(tmp_obj);
			
		}
		
	}
	
	public void pickWhatIWillTakeWithMe() {
		//System.out.println("pickWhatIWillTakeWithMe()");
		double p = UniformDistribution.nextUniform(0, 1);
//		System.out.println("Sorted = "+p);
		for(ObjectNode obj : my_objects) {
			if(obj.mob_prob.getP() > p) {
//				System.out.print("I will take with me the: ");
//				System.out.println(obj);
//				System.out.println(obj.mob_prob.getP());

				obj.setMobilityModel(new FollowOwner(this.ID, this));
				this.home.takeObject(obj);
            }
			else {
				
//				System.out.print("If I wont take with me, it should stay at home: ");
//				System.out.println(obj);
//				System.out.println(obj.mob_prob.getP());
				obj.setMobilityModel(new NoMobility());
				home.leftObject(obj);
			}
		}
	}
	
//	private Node generateNode(Position pos) {
//		Node node = Node.createNodeByClassname("ObjectNode.java");
//		node.setPosition(pos);
//
////		InterferenceModel im = Model.getInterferenceModelInstance(interferenceSel);
////		im.setParamString(interferenceDefString);
////		node.setInterferenceModel(im);
//		
////		MobilityModel mm = Model.getMobilityModelInstance(mobilitySel);
////		mm.setParamString(mobilityDefString);
////		node.setMobilityModel(mm);
//		
////		ReliabilityModel rm = Model.getReliabilityModelInstance(reliabilitySel);
////		rm.setParamString(reliabilityDefString);
////		node.setReliabilityModel(rm);
//		
////		ConnectivityModel cm = Model.getConnectivityModelInstance(connectivitySel);
////		cm.setParamString(connectivityDefString);
////		node.setConnectivityModel(cm);
//		return node;
//	}
//	
//	/**
//	 * Creates a node with the default settings.
//	 * @param pos The position of the new node
//	 * @return the node.
//	 */
//	public Node generateDefaultNode(Position pos) {
//		Node n = generateNode(pos);
//		n.finishInitializationWithDefaultModels(true);
//		return n;
//	}
	
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
		
		createObject();
		
//		if(this.mobilityModel instanceof SWIM) {
//			SWIM mm = (SWIM) this.mobilityModel;
//			
//			
//			//mm.initialize(ID, this);
//			//mm.swimInit(this.getPosition());
//			System.out.println(mm.toString());
//		}
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
		this.setColor(new Color(245, 194, 66));
		String text = Integer.toString(this.ID) + "| HN";
		// draw the node as a circle with the text inside
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 10, Color.WHITE);
		//super.drawNodeAsSquareWithText(g, pt, highlight, text, 10, Color.YELLOW);
	}

	
	@NodePopupMethod(menuText="Reset home location")
	public void myPopupMethod() {
		
		if(this.mobilityModel instanceof SWIM) {
			SWIM mm = (SWIM) this.mobilityModel;
			
			//mm.initialize(ID, this);
			mm.swimInit(this);
		}
	}
	
	@NodePopupMethod(menuText="Show node prop")
	public void myPopupMethod2() {
		
		if(this.mobilityModel instanceof SWIM) {
			SWIM mm = (SWIM) this.mobilityModel;
			
			Collections.sort(mm.node_neighbourding_cells_properties);
			Collections.sort(mm.node_visiting_cells_properties);
			System.out.println();
			System.out.println();
			for(NodeProp p : mm.node_neighbourding_cells_properties) {
				System.out.println(p.toString());
			}
			for(NodeProp p : mm.node_visiting_cells_properties) {
				System.out.println(p.toString());
			}
			for(Loc l : mm.locations) {
				System.out.println(l.toString());
			}

		}
	}
	
	@NodePopupMethod(menuText="update seen")
	public void myPopupMethod3() {
		
		if(this.mobilityModel instanceof SWIM) {
			SWIM mm = (SWIM) this.mobilityModel;
			
			for(Loc l : mm.locations) {
				l.no_of_nodes_present++;
			}
			
			mm.seperateAndUpdateWeights();
		
			
			Collections.sort(mm.node_neighbourding_cells_properties);
			Collections.sort(mm.node_visiting_cells_properties);
			System.out.println();
			System.out.println();
			for(NodeProp p : mm.node_neighbourding_cells_properties) {
				System.out.println(p.toString());
			}
			for(NodeProp p : mm.node_visiting_cells_properties) {
				System.out.println(p.toString());
			}
			for(Loc l : mm.locations) {
				System.out.println(l.toString());
			}

		}
	}
	
	@NodePopupMethod(menuText="Create OBJ and set a home" )
	public void myPopupMethod4() {
		
		createObject();
		
	}
	

	@NodePopupMethod(menuText="Print objects")
	public void myPopupMethod5() {
		
		for(ObjectNode obj : my_objects) {
			System.out.println(obj);
		}
		
	}
	
}
