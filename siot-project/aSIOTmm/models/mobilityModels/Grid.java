package projects.aSIOTmm.models.mobilityModels;

import sinalgo.nodes.Position;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.UniformDistribution;

import java.util.*;

public class Grid {

	private int x_dim;
	private int y_dim;
	private int side_num_tiles;

	private double tile_size_x;
	private double tile_size_y;


	public Grid(int x_dim, int y_dim, int side_num_tiles) {
		super();
		this.x_dim = x_dim;
		this.y_dim = y_dim;
		this.side_num_tiles = side_num_tiles;
		this.tile_size_x = x_dim / side_num_tiles;
		this.tile_size_y = y_dim / side_num_tiles;

	}
		
	
	public Position getCellCenterPositionByID(int cell_id) {
		Position center = new Position(0,0,0);
		double i, j;

		i = cell_id / side_num_tiles;
		j = cell_id % side_num_tiles;
		
		center.yCoord = ((tile_size_y)*(i+1)) - (tile_size_y/2);
		
		center.xCoord = ((tile_size_x)*(j+1)) - (tile_size_x/2);
		
		
		return center;
	}
	
	public double getCellsDistance(int cell_a, int cell_b) {
		Position a = getCellCenterPositionByID(cell_a);
		Position b = getCellCenterPositionByID(cell_b);
		
		return a.distanceTo(b);
	}
	
	public Position randomPosInCell(int cell_id) {
		Position p = new Position(0,0,0);
		UniformDistribution ud;
		
		double i, j;
		double y_min, y_max;
		double x_min, x_max;

		i = cell_id / side_num_tiles;
		j = cell_id % side_num_tiles;
		
		y_min = ((tile_size_y)*(i+1)) - (tile_size_y);
		y_max = ((tile_size_y)*(i+1));
		
		x_min = ((tile_size_x)*(j+1)) - (tile_size_x);
		x_max = ((tile_size_x)*(j+1));
		
		ud = new UniformDistribution(y_min, y_max);
		
		p.yCoord = ud.nextSample();
		
		ud = new UniformDistribution(x_min, x_max);
		
		p.xCoord = ud.nextSample();		
		
		return p;
	}
	
	
	
	public int getCellId(Position p) {
		int i, j;

		i = (int) Math.floor((p.yCoord) / tile_size_y);
		j = (int) Math.floor((p.xCoord) / tile_size_x);

		return i * side_num_tiles + j;
	}
	
	public int getCellId(int i, int j) {
		return i * side_num_tiles + j;
	}
	
	public List<Integer> getVisitingCellList(int cell_home,  int radius){
		int i;
		List<Integer> visiting_cells = new ArrayList<Integer>();
		List<Integer> neighborhood = new ArrayList<Integer>();
		
		neighborhood = getNeighbouringCellList(cell_home, radius);
		
		for(i=0; i<Math.pow(side_num_tiles, 2); i++) {
			if(!neighborhood.contains(i)) {
				visiting_cells.add(i);
			}
		}
		
		return visiting_cells;
	}
	
	public List<Integer> getNeighbouringCellList(int cell_home,  int radius){
		
		List<Integer> neighborhood = new ArrayList<Integer>();
		
		int i, j;
		
		int top_left_line, top_left_col;
		int top_right_line, top_right_col;
		
		int bottom_left_line, bottom_left_col;
		int bottom_right_line, bottom_right_col;
		
		int home_line;
		int home_col;
		
		home_line = cell_home / side_num_tiles;
		home_col = cell_home % side_num_tiles;
		
		
		//getting top left corner from the cell_home
		top_left_line = home_line - radius;
		top_left_col = home_col - radius;
		
		while(top_left_line < 0) {
			top_left_line += 1;
		}
		
		while(top_left_col < 0) {
			top_left_col += 1;
		}
		
		//getting top right corner from the cell_home
		top_right_line = home_line - radius;
		top_right_col = home_col + radius;
		
		while(top_right_line < 0) {
			top_right_line += 1;
		}
		
		while(top_right_col > side_num_tiles - 1) {
			top_right_col -= 1;
		}
		
		//getting bottom right corner from the cell_home
		bottom_right_line = home_line + radius;
		bottom_right_col = home_col + radius;
		
		while(bottom_right_line > side_num_tiles - 1) {
			bottom_right_line -= 1;
		}
		
		while(bottom_right_col > side_num_tiles - 1) {
			bottom_right_col -= 1;
		}
		
		//getting bottom left corner from the cell_home
		bottom_left_line = home_line + radius;
		bottom_left_col = home_col - radius;
		
		while(bottom_left_line > side_num_tiles - 1) {
			bottom_left_line -= 1;
		}
		
		while(bottom_left_col < 0) {
			bottom_left_col += 1;
		}
		
		
		for(i= top_left_col; i<=top_right_col; i++) {
			for(j = top_left_line; j<=bottom_left_line; j++) {
				neighborhood.add(getCellId(j, i));
			}
		}

		return neighborhood;
		
	}


	public boolean isNeighbouringCell(int cell_home, int cell_id, int radius) {
		if (isUpperCells(cell_home, cell_id, radius) || isDownCells(cell_home, cell_id, radius)
				|| isRightCells(cell_home, cell_id, radius) || isLeftCells(cell_home, cell_id, radius)
				|| isLeftUppCells(cell_home, cell_id, radius) || isRightUppCells(cell_home, cell_id, radius)
				|| isLeftDownCells(cell_home, cell_id, radius) || isRightDownCells(cell_home, cell_id, radius)) {
			return true;
		}

		return false;
	}

	private boolean isUpperCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home - (side_num_tiles * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isDownCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home + (side_num_tiles * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isRightCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home + i == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isLeftCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home - i == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isLeftUppCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home - ((side_num_tiles + 1) * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isRightUppCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home - ((side_num_tiles - 1) * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isLeftDownCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home + ((side_num_tiles - 1) * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	private boolean isRightDownCells(int cell_home, int cell_id, int radius) {

		int i;
		for (i = 1; i <= radius; i++) {
			if (cell_home + ((side_num_tiles + 1) * i) == cell_id) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "Grid [x_dim=" + x_dim + ", y_dim=" + y_dim + ", num_tiles=" + side_num_tiles + ", tile_size_x=" + tile_size_x
				+ ", tile_size_y=" + tile_size_y + "]";
	}

	
}
