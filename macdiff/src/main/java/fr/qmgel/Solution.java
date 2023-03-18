package fr.qmgel;

import java.util.Hashtable;

public class Solution
{
	private int number_of_branchings;
	private Hashtable<Variable,Integer> assignments;

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
}