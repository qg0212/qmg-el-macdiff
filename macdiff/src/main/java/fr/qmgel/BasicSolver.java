package fr.qmgel;

import java.util.ArrayList;
import java.util.Stack;

public class BasicSolver
{
	private int number_of_branchings;
	private Network network;

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