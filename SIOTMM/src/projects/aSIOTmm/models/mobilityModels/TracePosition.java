package projects.aSIOTmm.models.mobilityModels;

import sinalgo.nodes.Position;

public class TracePosition extends Position {
	private double time;

	public TracePosition(double time, double x, double y, double z) {
		super(x, y, z);
		this.time = time;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "TracePosition [time=" + time + ", xCoord=" + xCoord + ", yCoord=" + yCoord
				+ ", zCoord=" + zCoord + "]";
	}

}