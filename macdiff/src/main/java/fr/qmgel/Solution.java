package fr.qmgel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class Solution
{
	private int number_of_branchings;
	private HashMap<Variable,Integer> assignments;

	/**
	 * Construit une solution pour un réseau spécifique
	 * 
	 * @param network le réseau auquel appartient la solution
	 * @param number_of_branchings le nombre de branchements nécessaire pour trouver la solution
	 */
	public Solution(Network network, int number_of_branchings)
	{
		this.number_of_branchings = number_of_branchings;
		this.assignments = new HashMap<>();
		for(Variable variable : network.branchings())
		{
			if(!variable.singleton())
			{
				throw new RuntimeException("La variable possède plusieurs valeurs dans son domaine");
			}
			this.assignments.put(variable, variable.domain().get(0));
		}
	}

	public int numberOfBranchings()
	{
		return this.number_of_branchings;
	}

	public Integer assignment(Variable variable)
	{
		return this.assignments.get(variable);
	}

	public HashMap<Variable,Integer> assignments()
	{
		return this.assignments;
	}

	@Override
	public boolean equals(Object object)
	{
		if(object!=null)
		{
			if(this!=object)
			{
				if(this.getClass()==object.getClass())
				{
					return this.equals((Solution)object);
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean equals(Solution solution)
	{
		if(solution!=null)
		{
			if(this!=solution)
			{
				return (this.number_of_branchings==solution.numberOfBranchings() && this.assignments.equals(solution.assignments()));
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		String solution = "Solution : {";
		Iterator<Variable> variables = this.assignments.keySet().iterator();
		while(variables.hasNext())
		{
			Variable variable = variables.next();
			solution = solution.concat(String.format(" %d=%d", variable.id(), this.assignment(variable)));
			if(variables.hasNext())
			{
				solution = solution.concat(";");
			}
		}
		return solution.concat(" }");
	}
}