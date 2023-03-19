package fr.qmgel;

import java.util.Enumeration;
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

	public int numberOfBranchings()
	{
		return this.number_of_branchings;
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