package fr.qmgel;

import java.util.ArrayList;
import java.util.Stack;

public class BasicSolver
{
	private int number_of_branchings;
	private Network network;

	/**
	 * Construit un solveur basique pour un réseau spécifique
	 * 
	 * @param network le réseau spécifique
	 */
	public BasicSolver(Network network)
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
	 * Recherche de solutions du réseau
	 * 
	 * @param all_solutions indique si on cherche toutes les solutions (<code>true</code>) ou seulement une (<code>false</code>)
	 * 
	 * @return la liste des solutions trouvées (vide s'il n'y en a pas)
	 */
	public ArrayList<Solution> solve(boolean all_solutions)
	{
		ArrayList<Solution> solutions = new ArrayList<>();

		Stack<Changement> changes = this.network.restoreConsistency();
		if(changes.empty() || !changes.peek().variable().domain().empty())
		{
			Variable branching_variable = this.branchingVariable();
			if(branching_variable==null)
			{
				//This is a solution
				solutions.add(new Solution(this.network, this.number_of_branchings));
			}
			else
			{
				//We must do a branching

				//First branch
				this.number_of_branchings += 1;
				ArrayList<Integer> queue = branching_variable.domain().remove(1, -1);
				solutions.addAll(this.solve(all_solutions));
				branching_variable.domain().add(queue);

				//Second branch
				if(solutions.isEmpty() || all_solutions)
				{
					this.number_of_branchings += 1;
					Integer head = branching_variable.domain().remove(0);
					solutions.addAll(this.solve(all_solutions));
					branching_variable.domain().add(head);
				}
			}
		}

		//Rollback of changes
		while(!changes.empty())
		{
			changes.pop().restore();
		}

		return solutions;
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
}