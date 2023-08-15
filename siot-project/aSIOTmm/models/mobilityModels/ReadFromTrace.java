package projects.aSIOTmm.models.mobilityModels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.JFileChooser;


import projects.contactTimes.CustomGlobal;
import sinalgo.configuration.AppConfig;
import sinalgo.configuration.Configuration;
import sinalgo.io.eps.Exporter.PositionFileFilter;
import sinalgo.io.eps.Exporter.SingleFileFilter;
import sinalgo.io.positionFile.PositionFileIO.PositionFileException;
import sinalgo.models.MobilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.tools.Tools;

public class ReadFromTrace extends MobilityModel {

	static class PQsort implements Comparator<TracePosition> {

		public int compare(TracePosition o1, TracePosition o2) {

			return (int) (o1.getTime() - o2.getTime());
		}

	}

	LineNumberReader lnr = null;
	FileReader fr = null;
	double time_offset = 0;

	static boolean init_pqueue = false;

	static Vector<PriorityQueue> pq_vector = new Vector<PriorityQueue>();

	static String separator = "";
	static String trace_file_name = "";
	String str;
	int i;

	/**
	 * Trace format
	 * 
	 * #node time(s) x y 1 0.0 0 10 1 1.0 10 10 1 2.0 10 0 1 3.0 0 0 1 4.0 0 10
	 */

	public ReadFromTrace() {
		super();
		if (!Tools.isSimulationInAsynchroneMode()) {
			try {
				trace_file_name = Configuration.getStringParameter("MobilityModelTraceFile/trace_name");

				fr = new FileReader(trace_file_name);
				lnr = new LineNumberReader(fr);
				
				
				PQsort pqs = new PQsort();
		
				PriorityQueue<TracePosition> pqueu = new PriorityQueue<TracePosition>(pqs);
				pq_vector.addElement(pqueu);

				System.out.print("pq_vec size " + pq_vector.size());

			} catch (Exception e) {
				Tools.fatalError(e.getMessage());
				// if any error occurs
				e.printStackTrace();
			}
		}
	}



	private void build_pqueue() {
		if (!init_pqueue && !Tools.isSimulationInAsynchroneMode()) {
			init_pqueue = true;

			// read lines till the end of the stream
			try {
				while ((str = lnr.readLine()) != null) {
					i = lnr.getLineNumber();


					try {
						String[] parts = str.split(" ");
						if (parts.length < 4) {
							throw new PositionFileException(
									"Illegal line: expected six doubles, separated by space. Found \n" + str);
						}
						int node_id = Integer.parseInt(parts[0]);
						double time = Double.parseDouble(parts[3]);
						double x = Double.parseDouble(parts[1]);
						double y = Double.parseDouble(parts[2]);
						//String read_role = parts[4];
						//String read_behavior = parts[5];

						PriorityQueue<TracePosition> pq = pq_vector.get(node_id - 1);
						pq.add(new TracePosition(time * CustomGlobal.ONE_SECOND, x, y, 0));

					} catch (NumberFormatException e) {
						throw new PositionFileException(
								"Illegal line: expected six doubles, separated by space. Found \n" + str);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Position getNextPos(Node n) {
		build_pqueue();

		PriorityQueue<TracePosition> pq = pq_vector.get(n.ID - 1);
		if (pq.size() > 0) {
				System.out.println(pq.peek() + " " + Tools.getGlobalTime());
			if (pq.peek().getTime() > Tools.getGlobalTime()) {
				return n.getPosition();
			} else {
				TracePosition p = pq.poll();
				return new Position(p.xCoord, p.yCoord, p.zCoord);
			}
		}

		return n.getPosition();
	}
}