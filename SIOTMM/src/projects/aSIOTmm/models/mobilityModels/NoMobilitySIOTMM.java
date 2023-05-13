package projects.aSIOTmm.models.mobilityModels;

import projects.aSIOTmm.CustomGlobal;
import projects.aSIOTmm.nodes.nodeImplementations.ObjectNode;
import sinalgo.configuration.Configuration;
import sinalgo.models.MobilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.runtime.Main;
import sinalgo.tools.Tools;

public class NoMobilitySIOTMM extends MobilityModel {
	private static boolean firstTime = true;
	private boolean firstTimeLog = true;
	
	/* (non-Javadoc)
	 * @see models.MobilityModel#getNextPos(nodes.Node)
	 */
	public Position getNextPos(Node n) {
		if (n instanceof ObjectNode) {
			if(firstTimeLog) {
			CustomGlobal.contacts_trace.logln("" + n.ID + ", " + n.getPosition().xCoord + ", "
					+ n.getPosition().yCoord + ", " + Tools.getGlobalTime());
			firstTimeLog = false;
			}
		}
		return n.getPosition();
	}

	/**
	 * Constructor that prints a warning if interference is turned on 
	 */
	public NoMobilitySIOTMM() {
		super(false);
		if(firstTime && Configuration.mobility && Configuration.showOptimizationHints) {
			Main.warning("At least some nodes use the '"+ this.getClass().getSimpleName() + "' mobility model. " +
			             "If you do not consider mobility at all in your project, you can " +
			             "considerably improve performance by turning off mobility in the " +
			             "XML configuration file."
			);
			firstTime = false; // important to only have one message. 
		}
	}
}
