package sudoku;

import java.util.*;

public class Solver
{
	private Grid						problem;
	private ArrayList<Grid>				solutions;

	public Solver(Grid problem)
	{
		this.problem = problem;
	}

	public void solve()
	{
		solutions = new ArrayList<Grid>();
		solveRecurse(problem);
	}

	//
	// Standard backtracking recursive solver.
	//
	private void solveRecurse(Grid grid)
	{
		Evaluation eval = evaluate(grid);

		if (eval == Evaluation.ABANDON)
		{
			// Abandon evaluation of this illegal board.
			return;

		}
		else if (eval == Evaluation.ACCEPT)
		{
			// A complete and legal solution. Add it to solutions.
			solutions.add(grid);
		}
		else
		{
			// Here if eval == Evaluation.CONTINUE. Generate all 9 possible next grids. Recursively
			// call solveRecurse() on those grids.

			ArrayList<Grid> nextSolutions = grid.next9Grids();
			for(Grid g: nextSolutions)
				solveRecurse(g);
		}
	}

	public Evaluation evaluate(Grid grid)	{
		if (!grid.isLegal())
			return Evaluation.ABANDON;
		else if (grid.isFull())
			return Evaluation.ACCEPT;
		else
			return Evaluation.CONTINUE;


	}

	public ArrayList<Grid> getSolutions()
	{
		return solutions;
	}


	public static void main(String[] args)
	{


		long initial = System.nanoTime();
		Grid g = TestGridSupplier.getPuzzle1();
		Solver solver = new Solver(g);
		solver.solve();
		Grid test = TestGridSupplier.getSolution3();
		long later = System.nanoTime();
		System.out.println((double)(later - initial)/ 1000000000.0);

		if(test.toString().equals(solver.getSolutions().get(0).toString()))
			System.out.println("Success");

		else System.out.println("Failed (again, for now)");
		for (Grid elt: solver.getSolutions())
		{
			System.out.println(elt.toString());
		}


	}


}

