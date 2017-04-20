import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


class Grid
{
	// A Grid stores a cell-based map of blocks (and maybe other goodies).
	// The Grid know how many pixels each block is, so can relate pixel-based
	// coords to the map.

	public static final int MWIDTH = 40;
	public static final int MHEIGHT = 15;
	int map[][] = new int[MWIDTH][MHEIGHT];
	static final int CELLSIZE = 40; // Number of pixels per map cell

	Grid()
	{
		for (int row = 0; row < MHEIGHT; row++)
		 for (int col = 0; col < MWIDTH; col++)
			map[col][row] = 0;
	}
	
	public int width()
	{
		return MWIDTH*CELLSIZE;
	}

	public int moveRight(BoundingBox r, int d)
	{
		// Return the number of pixels (not exceeding d) that
		// the Rectangle r can move to the right without hitting
		// a block.
		// Assume d is less than CELLSIZE.
		int right = (int)r.getMaxX();
		int col = right/CELLSIZE;
		int row1 = ((int)r.getMinY())/CELLSIZE;
		int row2 = ((int)r.getMaxY())/CELLSIZE;
		if (row2 >= MHEIGHT)
			row2 = MHEIGHT-1;
		int edge = CELLSIZE*(col+1);
		if ((right+d) < edge)
			return d;
		if (col == (MWIDTH-1))
			return width()-right-1;
		for (int row = row1; row <= row2; row++)
			if (map[col+1][row] != 0)
				return edge-right-1;
		return d;
	}

	public int moveLeft(BoundingBox r, int d)
	{
		// Return the number of pixels (not exceeding d) that
		// the Rectangle r can move to the left without hitting
		// a block.
		// Assume d is less than CELLSIZE.
		int left = (int)r.getMinX();
		int col = left/CELLSIZE;
		int row1 = ((int)r.getMinY())/CELLSIZE;
		int row2 = ((int)r.getMaxY())/CELLSIZE;
		if (row2 >= MHEIGHT)
			row2 = MHEIGHT-1;
		int edge = CELLSIZE*col;
		if ((left-d) >= edge)
			return d;
		if (col == 0)
			return left;
		for (int row = row1; row <= row2; row++)
			if (map[col-1][row] != 0)
				return left-edge;
		return d;
	}

	/**
	 *  Return the number of pixels (not exceeding d) that
	 * the Rectangle r can move down without hitting
	 * a block.
	 * Assume d is less than CELLSIZE.
	 */
	public int moveDown(BoundingBox r, int d)
	{
		int rbottom = (int)r.getMaxY();
		int row = rbottom/CELLSIZE;
		int col1 = ((int)r.getMinX())/CELLSIZE;
		int col2 = ((int)r.getMaxX())/CELLSIZE;
		int edge = CELLSIZE*(row+1);
		if (rbottom+d < edge)
			return d;
		for (int col = col1; col <= col2; col++)
			if (map[col][row+1] != 0)
				return edge - rbottom - 1;
		return d;
	}

	public int moveUp(BoundingBox r, int d)
	{
		return d;
	}

	public boolean onGround(BoundingBox r)
	{
		// Return true if Rectangle r is resting on the ground (or a block)
		int rbottom = (int)r.getMaxY();
		int row = rbottom/CELLSIZE;
		int edge = CELLSIZE*(row+1);
		if ((rbottom+1) != edge)
			return false;
		int col1 = ((int)r.getMinX())/CELLSIZE;
		int col2 = ((int)r.getMaxX())/CELLSIZE;
		for (int i = col1; i <= col2; i++)
			if (map[i][row+1] != 0)
				return true;
		return false;
	}

	public boolean atTop(BoundingBox r)
	{
		return false;
	}

	public void setBlock(int x, int y)
	{
		map[x][y] = 1;
	}
	
	public void render(GraphicsContext gc)
	{
		gc.setFill(Color.GREEN);
		// Just draw visible blocks
		int col1 = (Scroll.vleft)/CELLSIZE;
		int col2 = (Scroll.vleft + Scroll.VWIDTH)/CELLSIZE;
		if (col2 >= MWIDTH)
			col2 = MWIDTH-1;
		for (int row = 0; row < MHEIGHT; row++)
		 for (int col = col1; col <= col2; col++)
			if (map[col][row] == 1)
				gc.fillRect(col*CELLSIZE-Scroll.vleft, row*CELLSIZE, CELLSIZE, CELLSIZE);
	}
}