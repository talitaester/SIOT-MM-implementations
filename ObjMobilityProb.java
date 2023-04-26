package projects.aSIOTmm.nodes.nodeImplementations;

public enum ObjMobilityProb {
	LOW(0.01), MEDIUM(0.5), HIGH(.95);

	private double p;

	ObjMobilityProb(double p) {
		this.p = p;
	}

	public double getP() {
		return p;
	}

}
