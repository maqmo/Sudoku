package sudoku;

import java.util.*;


public class Grid 	{

	private int[][]						values;


	public Grid(String[] rows)
	{

		values = new int[9][9];
		for (int j=0; j<9; j++)
		{
			String row = rows[j];
			char[] charray = row.toCharArray();
			for (int i=0; i<9; i++)
			{
				char ch = charray[i];
				if (ch != '.')
					values[j][i] = ch - '0';
			}

		}

	}

	public String toString()
	{
		String s = "";
		for (int j=0; j<9; j++)
		{
			for (int i=0; i<9; i++)
			{
				int n = values[j][i];
				if (n == 0)
					s += '.';
				else
					s += (char)('0' + n);
			}
			s += "\n";
		}
		return s;
	}

	public ArrayList<Grid> next9Grids()		{

		int nextNum = 1;
		boolean zeroFound = false;
		int zeroRow = 0;
		int zeroColumn = 0;
		String row = "";

		int [][] valuesClone = new int[9][9];
		String[] rowString = new String[9];
		ArrayList<Grid> grids= null;




		forLoops:
			for (int i = 0; i < 9; i++) 	{
				for (int j = 0; j < 9; j++) 	{

					//locate the zero and break if found
					if(values[i][j] == 0)	{
						zeroFound = true;

						//save its location in the array
						zeroRow = i;
						zeroColumn = j;

						break forLoops;
					}
				}
			}


		//do this once for each value 1-9, if an empty cell exists (0)
		if(zeroFound)	{

			grids = new ArrayList<Grid>();
			//make a copy of values
			valuesClone = values.clone();

			while(nextNum < 10)		{
				//increment the zero location
				valuesClone[zeroRow][zeroColumn] = nextNum;

				//convert the values of valuesClone to strings, and add each row to an array of strings
				for (int i = 0; i< 9; i++){
					for (int j = 0; j < 9; j++)
						row += Integer.toString(valuesClone[i][j]);

					rowString[i] = row;
					//clear the variable for reuse
					row = "";
				}
				//add to the list, increment and repeat
				grids.add(new Grid(rowString));
				nextNum++;
			}

		}
		return grids;

	}

	//check for legality by checking blocks, columns and rows
	public boolean isLegal()	{

		return (blockCheck() && rowsCheck(this.transpose()) && rowsCheck(values));
	}


	//check the rows of cells for validity
	private boolean rowsCheck(int[][] row) {

		boolean rowsValid = true;
		HashSet<Integer> hash = new HashSet<Integer>();

		//for every row, take each non zero element and add to the hash, any unsuccessful add is an invalid cell
		for( int[] rowElt: row) {
			for( int elt: rowElt) {
				if (elt != 0 && !hash.add(elt))
					return false;
			}

			//clear the hash for the next row (else nothing will be added to it [small bug location] )
			hash.clear();
		}
		return rowsValid;
	}

	// There are 3 rows of 3x3 blocks. 3 left blocks, 3 middles, and 3 right blocks
	private boolean blockCheck() {

		boolean bigRowsValid = true;


		HashSet<Integer> leftBlock = new HashSet<>();
		HashSet<Integer> middleBlock = new HashSet<>();
		HashSet<Integer> rightBlock = new HashSet<>();

		//cycle through each cell
		for( int i = 0; i < 9;i++)	{
			for( int j = 0; j < 9; j++)	{
				//exclude all zeros
				if (values[i][j] != 0)	{
					// left, middle and right blocks have column values of >3, >6, >9 resp.
					if(j < 3) {
						if(!(leftBlock.add(values[i][j])))
							return false; }
					else if(j < 6) {
						if(!middleBlock.add(values[i][j]))
							return false; }
					else {
						if(!rightBlock.add(values[i][j]))
							return false; }
				}

			}
			//indexes 2,5,8 signify the end of a 1x3 row of blocks, clear the lists and check the next row of blocks
			if(i == 2 || i == 5 || i == 8){
				leftBlock.clear();
				middleBlock.clear();
				rightBlock.clear();}
		}

		return bigRowsValid;
	}


	private int[][] transpose() {

		//for square matrices only!
		int [][] copy = new int[values.length][values.length];
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values.length; j++)	{
				copy[i][j] = values[j][i];
			}
		return copy;
	}

	//check for empty cells (zeros)
	public boolean isFull()	{

		for( int[] row: values) {
			for( int elt: row) {
				if( elt == 0)
					return false;
			}

		}
		return true;
	}



	public boolean equals(Object x)	{

		if (x instanceof Grid)	{
			Grid that = (Grid) x;
			//on first occurrence of non equality, returns false
			for (int i = 0; i < values.length; i++)
				for (int j = 0; j< values.length; j++)
					if (this.values[i][j] != that.values[i][j])
						return false;

		}

		return true;
	}

	public class Node {

		Node next;
		Object data;
		Node(Object data) {
			this.data = data;
		}


	}


	public static void main(String[] args) {



		String[] values = 	  {
				"2.3.1.5.*",
				"8..395..1",
				"15.....27",
				".8..7..5.",
				"62.9.4.13",
				".9..2..7.",
				"91.....34",
				"2..748..9",
				"..6.3.2.."
		};


		Grid testG = new Grid(values);
		// for (Grid elt: testG.next9Grids())
		//  System.out.println(testG.toString() + "\n" + "Legal status: " + testG.isLegal() + "\n Full status: " + testG.isFull());

		System.out.println(testG.isLegal());



	}

}


