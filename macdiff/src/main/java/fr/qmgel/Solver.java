package fr.qmgel;

import java.util.ArrayList;

public abstract class Solver
{
	public static Solver get(String name, Network network)
	{
		if(name.equals("learning"))
		{
			return new LearningSolver(network);
		}
		return new BasicSolver(network);
	}

	protected int number_of_branchings;
	protected Network network;

	public Solver(Network network)
	{
		this.number_of_branchings = 0;
		this.network = network;
	}

	public int numberOfBranchings()
	{
		return this.number_of_branchings;
	}

	public Network network()
	{
		return this.network;
	}

	/**
	 * Cherche la variable la plus adaptée pour effectuer un branchement
	 * 
	 * @return la variable trouvée, ou <code>null</code> si toutes les variables sont des singletons
	 */
	public Variable branchingVariable()
	{
		double ratio = Double.POSITIVE_INFINITY;
		Variable branching_variable = null;

		for(Variable variable : this.network.branchings())
		{
			if(!variable.singleton())
			{
				double tmp = (double)variable.domain().count() / (double)variable.degree();
				if(tmp<ratio)
				{
					ratio = tmp;
					branching_variable = variable;
				}
			}
		}

		return branching_variable;
	}

	public abstract ArrayList<Solution> solve(boolean all_solutions);
}