package projects.aSIOTmm.models.mobilityModels;

import java.util.*;

import projects.aSIOTmm.CustomGlobal;
import projects.aSIOTmm.nodes.nodeImplementations.HumanNode;
import projects.aSIOTmm.nodes.nodeImplementations.ObjectNode;
import sinalgo.configuration.Configuration;
import sinalgo.models.MobilityModel;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;
import sinalgo.tools.statistics.UniformDistribution;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.Edge;
	
public class SWIM extends MobilityModel {
	
	/*
	 * Implementation from: https://arxiv.org/pdf/1609.05199.pdf
	 */
	
	
	private boolean swim_is_inited = false;
	
	// we assume that these distributions are the same for all nodes
	protected static Distribution speedDistribution = new UniformDistribution(0.5, 1.42); // 1.42 metres/s
	protected static Distribution waitingTimeDistribution = new UniformDistribution(0, 300); //seconds
	
	
	private int node_id = -1;
	
	private Position home_position = new Position();
	private int home_cell_id;
	
	private int neighbour_location_limit = 3; // radius mesured in cells
	
	private double popularity_decision_threshold = 0.7; // 
	
	private double alpha = 0.5;
	private double return_home_probability = 0.35; // probability to return home
	
	private static int side_num_cells = 100;
	
	private static Grid grid_map = new Grid(Configuration.dimX, Configuration.dimY, side_num_cells);
	
	//private List<Integer> neighborhood_cells_index = new ArrayList<Integer>();
	
	//private List<Integer> visiting_cells_index = new ArrayList<Integer>();
	
	//private Hashtable<Integer, Double> weight_cells = new Hashtable<Integer, Double>(); // cell_id and weight
	//private Vector<Double> weight_cells = null;
	
	// seen_cells is the number of nodes that node A encountered at cell C, and this value updates each time node A visits cell C.
	//private Hashtable<Integer, Integer> seen_cells = new Hashtable<Integer, Integer>();
	//private Vector<Integer> seen_cells = null;
	//private Vector<NodeProp> node_cells_properties = null;
	public Vector<NodeProp> node_neighbourding_cells_properties = null;
	public Vector<NodeProp> node_visiting_cells_properties = null;

	private boolean locations_created = false;
	private int num_of_locs= 20;
	public Vector<Loc> locations = null;
	
	
	
	private Position neew;
	private Position last_target  = new Position();
	private Position target_position  = new Position();
	protected Position move_vector = new Position(); // The vector that is added in each step to the current position of this node
	private Position current_position; // the current position, to detect if the node has been moved by other means than this mobility model between successive calls to getNextPos()
	protected int remaining_hops = 0; // the remaining hops until a new path has to be determined
	protected int remaining_waitingTime = 0;
	
	private boolean is_moving = false;

	public SWIM() {
		super();
	}

	public SWIM(Position home_position) {
		super();
		this.home_position = home_position;
		home_cell_id = grid_map.getCellId(home_position);
		
	}

	private void finish()	{
		
		/*I do not know if this is needed*/
		
		
		locations_created = false;
		locations.removeAllElements();
	}
	
	public void swimInit(Node n) {
		
		//finish();
		
		this.node_id = n.ID;
		
		this.home_position.assign(n.getPosition());
		this.home_cell_id = grid_map.getCellId(home_position);
		
		this.last_target.assign(home_position);
		
		if(num_of_locs <= 0) {
			num_of_locs = 1;

		}
		
		locations = new Vector<Loc>(num_of_locs);
		createLocations();
		fillNodeCellsProperties();
		
		
		//this.neighborhood_cells_index = grid_map.getNeighbouringCellList(home_cell_id, neighbour_location_limit);
		
		//this.visiting_cells_index = grid_map.getVisitingCellList(home_cell_id, neighbour_location_limit);
		
		//seen_cells = new Vector<Integer>(side_num_cells * side_num_cells);
		//weight_cells = new Vector<Double>(side_num_cells * side_num_cells);
		//node_cells_properties = new Vector<NodeProp>((int)Math.pow(side_num_cells, 2));
		
		
	
		//updateNodesCount();
	}
	
	private void createLocations() {
		
		List<Integer> neighborhood_cells_index;
		List<Integer> visiting_cells_index;
		
		neighborhood_cells_index = grid_map.getNeighbouringCellList(home_cell_id, neighbour_location_limit);
		visiting_cells_index = grid_map.getVisitingCellList(home_cell_id, neighbour_location_limit);
	
		int random_cell_id;
		Loc l;
		Position p;
		
		if(!locations.isEmpty()) {
			locations.removeAllElements();
		}
		
		//by default one of the locations is the home location.
		l = new Loc(home_position.xCoord, home_position.yCoord, home_position.zCoord, 
				0, home_cell_id, CellType.HOME);
		locations.add(l);
		
		for(int i = 0; i < num_of_locs - 1; i++) {
			random_cell_id = (int) UniformDistribution.nextUniform(0, Math.pow(side_num_cells, 2));
			
			p = grid_map.randomPosInCell(random_cell_id);
			
			if(random_cell_id == home_cell_id) {
				continue;
			}
			
			if(neighborhood_cells_index.contains(random_cell_id)) {
				l = new Loc(p.xCoord, p.yCoord, p.zCoord, 0, random_cell_id, CellType.NEIGHBOURDING);
				locations.add(l);
				continue;
			}
			
			if(visiting_cells_index.contains(random_cell_id)) {
				l = new Loc(p.xCoord, p.yCoord, p.zCoord, 0, random_cell_id, CellType.VISITING);
				locations.add(l);
			}
			
		}
		
//		for(Loc i : locations) {
//			System.out.println(i);
//		}
		
	}

	private void fillNodeCellsProperties() {
		List<Integer> neighborhood_cells_index;
		List<Integer> visiting_cells_index;

		neighborhood_cells_index = grid_map.getNeighbouringCellList(home_cell_id, neighbour_location_limit);
		
		node_neighbourding_cells_properties = new Vector<NodeProp>();
		
		for(Integer i : neighborhood_cells_index){
			NodeProp np_temp;
			if(i != home_cell_id) {
				np_temp = new NodeProp(0, 0, i, home_position.distanceTo(grid_map.getCellCenterPositionByID(i)), CellType.NEIGHBOURDING);
			}else {
				np_temp = new NodeProp(0, 0, i, 0, CellType.HOME);
			}
			
			node_neighbourding_cells_properties.add(np_temp);
			
		}
		
		visiting_cells_index = grid_map.getVisitingCellList(home_cell_id, neighbour_location_limit);
		
		node_visiting_cells_properties = new Vector<NodeProp>();
		
		for(Integer i : visiting_cells_index){
			NodeProp np_temp = new NodeProp(0, 0, i, home_position.distanceTo(grid_map.getCellCenterPositionByID(i)), CellType.VISITING);
			
			node_visiting_cells_properties.add(np_temp);
			
		}
		
//		for(NodeProp p : node_neighbourding_cells_properties) {
//			System.out.println(p.toString());
//		}
//		for(NodeProp p : node_visiting_cells_properties) {
//			System.out.println(p.toString());
//		}

	}

//	private Position setTargetPosition() {
//		double randomNum = UniformDistribution.nextUniform(0, 1);
//
//		if (randomNum < return_home_probability && last_position != home_position) {
//			return home_position;
//		}else {
//			
//			// compute the weights assign to each node
//            seperateAndUpdateWeights();
//            
//            
//			//targe_position = decide_neibooring_or_visiting();
//		}
//		
//		return null;
//
//	}

	public void seperateAndUpdateWeights() {
		
		List<Integer> neighbor_ids = grid_map.getNeighbouringCellList(home_cell_id, neighbour_location_limit);
		
		// compute the maximum possible weight (to normalize)
//	    double max_weight = 
//	    		alpha * ( Math.sqrt( Math.pow(Configuration.dimX, 2) + Math.pow(Configuration.dimY, 2)) ) 
//	    		+ (1.0 - alpha) * Tools.getNodeList().size();
		double max_distance = Math.sqrt( Math.pow(Configuration.dimX, 2) + Math.pow(Configuration.dimY, 2));
		
//	    System.out.println("max_weight="+max_weight);
//	    System.out.println("max_distance="+max_distance);
//	    System.out.println("neighbor_ids="+neighbor_ids);
//	    System.out.println("locations="+locations.toString());
	    
	    
	    for(Loc l : locations) {
	    	//System.out.println("l.cell_id="+l.cell_id);
	    	if(neighbor_ids.contains(l.cell_id)) {
	    		//System.out.println("contains");
	    		for(NodeProp np : node_neighbourding_cells_properties) {
	    			if(np.cell_map_id == l.cell_id) {
	    				
	    				np.seen = l.no_of_nodes_present;
	    				np.weight = (alpha * (1 - (l.distanceTo(home_position)/max_distance)) + (1.0 - alpha) * (np.seen/Tools.getNodeList().size()));
	    				
//	    				System.out.println("l.cell_id = "+l.cell_id);
//	    				System.out.println("alpha="+alpha);
//	    				System.out.println("l.distanceTo(home_position)="+l.distanceTo(home_position));
//	    				System.out.println("l.distanceTo(home_position)/max_distance="+(l.distanceTo(home_position)/max_distance));
//	    				System.out.println("Distance part = "+(alpha * (l.distanceTo(home_position)/max_distance)));
//	    				System.out.println("Seen part = "+((1.0 - alpha) * (np.seen/Tools.getNodeList().size())));
//	    				System.out.println();
	    				
	    				//np.weight = np.weight/max_weight;
	    				//break;
	    			}
	    		}
	    	}else {
	    		//System.out.println("non-contains");
	    		for(NodeProp np : node_visiting_cells_properties) {
	    			if(np.cell_map_id == l.cell_id) {
	    				
	    				np.seen = l.no_of_nodes_present;
	    				np.weight = (alpha * (1 - (l.distanceTo(home_position)/max_distance)) + (1.0 - alpha) * (np.seen/Tools.getNodeList().size()));
	    				
//	    				System.out.println("l.cell_id = "+l.cell_id);
//	    				System.out.println("Distance part = "+(alpha * (l.distanceTo(home_position)/max_distance)));
//	    				System.out.println("Seen part = "+((1.0 - alpha) * (np.seen/Tools.getNodeList().size())));
//	    				System.out.println();
	    				//np.weight = np.weight/max_weight;
	    				//break;
	    			}
	    		}
	    	}
	    	
	    }
	    
	    
//	    for(NodeProp np : node_neighbourding_cells_properties) {
//	    	System.out.println(np);
//	    }
//	    for(NodeProp np : node_visiting_cells_properties) {
//	    	System.out.println(np);
//	    }
		
	}

	private Position decideHomeNeibooringVisiting() {

//		System.out.println("\n\ndecideHomeNeibooringVisiting() ");

		// determine the next point where this node moves to
		// nextDestination = getNextWayPoint();
		double randomNum = UniformDistribution.nextUniform(0, 1);

		if (randomNum < return_home_probability && !this.last_target.equals(home_position)) {

			System.out.println("Returning home");

			//this.last_target.assign(current_position);
			
			for(NodeProp np:  node_neighbourding_cells_properties) {
				if(np.cell_map_id == home_cell_id)
					np.increaseLocationPicks();
			}

			return home_position;

		} else {

			// the procedure of identifying the next location is as follows
			// 1) decide randomly what type of location to go to next (neighboring or
			// visiting)
			// using the alpha parameter
			// if alpha is lower, more likely to choose a visisting location
			// if alpha is larger, more likely to choose a neighbouring location
			// 2) call chooseDestination() to select the destination to go to
			// 3) if there are no locations selected by chooseDestination() based on
			// the given list (i.e., neighboring or visiting), check in the other
			// list (i.e., visiting or neighbouring)

			// get random number between 0 & 1 (included)
			double randomNumber = UniformDistribution.nextUniform(0.0, 1.0);

			if (randomNumber <= alpha) {
				// if random number is lower than alpha, choose a neighbor location as
				// next destination

				Collections.sort(node_neighbourding_cells_properties);
//				for (NodeProp np : node_neighbourding_cells_properties) {
//					System.out.println(np);
//				}
//				System.out.println("Picking from node_neighbourding_cells_properties");
				return pickDestination(node_neighbourding_cells_properties);
			} else {
				// if random number is higher than alpha, choose a visiting location as
				// next destination

				Collections.sort(node_visiting_cells_properties);
//				for (NodeProp np : node_visiting_cells_properties) {
//					System.out.println(np);
//				}
//				System.out.println("Picking from node_visiting_cells_properties");
				return pickDestination(node_visiting_cells_properties);
			}

		}

	}

	private Position pickDestination(Vector<NodeProp> vec) {

		double top_k_percent = 0.3;
		int popular = 0;
		int notPopular = 0;

		Position target = new Position();

		// count the total number of popular locations based on
		// previously computed weight values
//		for (NodeProp i : vec) {
//			if (i.weight > 0.75) {
//				popular++;
//			}
//		}
		//top k% in weight to be the popular
		popular = (int) Math.ceil(top_k_percent*vec.size());
		// compute the not-popular locations
		notPopular = vec.size() - popular;

		// choose a destination from the given array in the following manner
		// 1) obtain a random number and check if a popular or not popular
		// item is selected (i.e., popularityDecisionThreshold)
		// 2) if popular item to be selected and the popular range has items,
		// select an item in the popular range, randomly
		// 3) if not popular to be selected and the not popular range has items,
		// select an item in the not popular range, randomly
		// 4) if none of the above, select an item from the whole array,
		// randomly
		
		
		// find a position within the radius given from the selected location
	    // to move to
	    // REASON: don't want all the nodes to pile up at the center of the
	    // location
		
		int randomNum = (int) UniformDistribution.nextUniform(0, 1);
//		System.out.println("Vec.size="+vec.size());
//
//		System.out.println("Popularity="+popular);

		if (popular > 0 && randomNum < popularity_decision_threshold) {
			
			randomNum = (int) UniformDistribution.nextUniform(0, (popular - 1));
			
//			System.out.println("Picking randomNum popular idx between = ["+0+","+popular+"-1]");
//			System.out.println("randomNum ="+randomNum);
//			System.out.println("pick idx ="+(randomNum));
			
			target = grid_map.randomPosInCell(vec.get(randomNum).cell_map_id);
			
			vec.get(randomNum).increaseLocationPicks();

		} else if (notPopular > 0) {
			
			randomNum = (int) UniformDistribution.nextUniform(0, (notPopular - 1));
			
//			System.out.println("Picking randomNum NOT popular idx between = ["+0+","+notPopular+"-1]");
//			System.out.println("randomNum ="+randomNum);
//			System.out.println("pick idx ="+(popular + randomNum));
			target = grid_map.randomPosInCell(vec.get(popular + randomNum).cell_map_id);
			
			vec.get(popular + randomNum).increaseLocationPicks();
			
		} 
//		else {
//			
//			randomNum = (int) UniformDistribution.nextUniform(0, (vec.size() - 1));
//			
//			System.out.println("Picking randomNum NOT popular idx between = ["+0+","+vec.size()+"-1]");
//			System.out.println("randomNum ="+randomNum);
//			System.out.println("pick idx ="+(randomNum));
//			target = grid_map.randomPosInCell(vec.get(randomNum).cell_map_id);
//			
//			vec.get(randomNum).increaseLocationPicks();
//			
//		}
		
		return target;
		
		
	}
	
	public void updateNodeCount() {
		
//		System.out.println("\n\nupdateNodeCount()");
//		System.out.println("Before updating");
//		for (Loc l : locations) {
//			System.out.println(l);
//		}
		
		int c_id;
		
		Node n = Tools.getNodeByID(node_id);
		
		c_id = grid_map.getCellId(n.getPosition());
		
		if (!is_moving) {
			for (Loc l : locations) {
				if (l.cell_id == c_id && n.outgoingConnections.size()>0) {
					
					for(Edge e : n.outgoingConnections) {
						l.seen_nodes_ids.add(e.endNode.ID);
					}
					//l.no_of_nodes_present += n.outgoingConnections.size();
					l.no_of_nodes_present  = l.seen_nodes_ids.size();
					
//					System.out.println("Updating location = "+ l);
				}
			}

		}
	}
		

	@Override
	public Position getNextPos(Node n) {
		
		HumanNode hn_tmp = null;
		
		if(!swim_is_inited) {
			swim_is_inited = true;
			swimInit(n);
		}
		
		
		// restart a new move to a new destination if the node was moved by another
		// means than this mobility model
		if (current_position != null) {
			if (!current_position.equals(n.getPosition())) {
				remaining_waitingTime = 0;
				remaining_hops = 0;
			}
		} else {
			current_position = new Position(0, 0, 0);
		}

		Position nextPosition = new Position();

		// execute the waiting loop
		if (remaining_waitingTime > 0) {
			
			//System.out.println("Remaning Waithing time = "+remaining_waitingTime);
			remaining_waitingTime--;
			this.is_moving = false;
			
//			CustomGlobal.pos_trace.logln(""+n.ID+
//										", "+n.getPosition().xCoord,
//										", "+n.getPosition().yCoord,
//										", "+Tools.getGlobalTime());
//			if (n instanceof ObjectNode) {
//				CustomGlobal.contacts_trace.logln("" + n.ID + ", " + n.getPosition().xCoord + ", "
//						+ n.getPosition().yCoord + ", " + Tools.getGlobalTime());
//			}
			return n.getPosition();
		}

		if (remaining_hops == 0) {
			
			// determine the speed at which this node moves
			double speed = Math.abs(speedDistribution.nextSample()); // units per round
			
			//I'm at home?
			if(n.getPosition().equals(home_position)) {
				if(n instanceof HumanNode) {
					hn_tmp = (HumanNode) n;
					hn_tmp.pickWhatIWillTakeWithMe();
				}
			}

			// determine the next point where this node moves to
			//nextDestination = getNextWayPoint();
			//double randomNum = UniformDistribution.nextUniform(0, 1);
			
			// randomly base to where the next move will be; home or
	        // other location (neighboring or visiting), provided that
	        // node is not already at home location
			
//			if (randomNum < return_home_probability && !this.last_position.equals(home_position)) {
//				
//				
//				
//				System.out.println("Returning home");
//				
//				this.last_position.assign(n.getPosition());
//				
//				target_position.assign(home_position);
//				
//				
//			}else {
				
				this.last_target.assign(target_position);
				
				// compute the weights assignd to each node
	            seperateAndUpdateWeights();
				
	            // find the next location (position) to move to
	            target_position.assign(decideHomeNeibooringVisiting());
	            
//			}
			
			// determine the number of rounds needed to reach the target
			double dist = target_position.distanceTo(n.getPosition());
			double rounds = dist / speed;
			remaining_hops = (int) Math.ceil(rounds);
			// determine the moveVector which is added in each round to the position of this
			// node
			double dx = target_position.xCoord - n.getPosition().xCoord;
			double dy = target_position.yCoord - n.getPosition().yCoord;
			double dz = target_position.zCoord - n.getPosition().zCoord;
			move_vector.xCoord = dx / rounds;
			move_vector.yCoord = dy / rounds;
			move_vector.zCoord = dz / rounds;
			
			this.is_moving = true;
		}
		if (remaining_hops <= 1) { // don't add the moveVector, as this may move over the destination.
			nextPosition.xCoord = target_position.xCoord;
			nextPosition.yCoord = target_position.yCoord;
			nextPosition.zCoord = target_position.zCoord;
			// set the next waiting time that executes after this mobility phase
			remaining_waitingTime = (int) Math.ceil(waitingTimeDistribution.nextSample());
			remaining_hops = 0;
			this.is_moving = false;
			
			/*
			 * 
			 * PROVAVELMENTE TEREI QUE ADICIONAR UMA ATUALIZAÇÃO DE CÉLULAS AQUI!!!
			 * 
			 */
			
			updateNodeCount();
			
		} else {
			double newx = n.getPosition().xCoord + move_vector.xCoord;
			double newy = n.getPosition().yCoord + move_vector.yCoord;
			double newz = n.getPosition().zCoord + move_vector.zCoord;
			nextPosition.xCoord = newx;
			nextPosition.yCoord = newy;
			nextPosition.zCoord = newz;
			remaining_hops--;
			this.is_moving = true;
		}
		
		
		current_position.assign(nextPosition);
		return nextPosition;
	}

	public Position getHome_position() {
		return home_position;
	}

	public void setHome_position(Position home_position) {
		this.home_position = home_position;
	}
	
}
