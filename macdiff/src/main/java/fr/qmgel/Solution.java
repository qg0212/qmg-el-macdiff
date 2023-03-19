package fr.qmgel;

import java.util.Enumeration;
import java.util.Hashtable;

public class Solution
{
	private int number_of_branchings;
	private Hashtable<Variable,Integer> assignments;

	/**
	 * Construit une solution pour un réseau spécifique
	 * 
	 * @param network le réseau auquel appartient la solution
	 * @param number_of_branchings le nombre de branchements nécessaire pour trouver la solution
	 */
	public Solution(Network network, int number_of_branchings)
	{
		this.number_of_branchings = number_of_branchings;
		this.assignments = new Hashtable<>();
		for(Variable variable : network.branchings())
		{
			if(!variable.singleton())
			{
				throw new RuntimeException("La variable possède plusieurs valeurs dans son domaine");
			}
			this.assignments.put(variable, variable.domain().get(0));
		}
	}

	public Integer assignment(Variable variable)
	{
		return this.assignments.get(variable);
	}

	public Hashtable<Variable,Integer> assignments()
	{
		return this.assignments;
	}

	@Override
	public String toString()
	{
		String solution = "Solution : {";
		Enumeration<Variable> variables = this.assignments.keys();
		while(variables.hasMoreElements())
		{
			Variable variable = variables.nextElement();
			solution = solution.concat(String.format(" %d=%d", variable.id(), this.assignment(variable)));
			if(variables.hasMoreElements())
			{
				solution = solution.concat(";");
			}
		}
		return solution.concat(" }");
	}
}