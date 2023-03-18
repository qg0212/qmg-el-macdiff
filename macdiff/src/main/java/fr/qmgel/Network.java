package fr.qmgel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Network
{
	private UniqueList<Variable> variables;
	private ArrayList<Variable> branchings;
	private Hashtable<Variable,UniqueList<Variable>> constraints;

	public Network()
	{
		this.variables = new UniqueList<>();
		this.branchings = new ArrayList<>();
		this.constraints = new Hashtable<>();
	}

	public UniqueList<Variable> variables()
	{
		return this.variables;
	}

	public ArrayList<Variable> branchings()
	{
		return this.branchings;
	}

	public UniqueList<Variable> constraint(Variable variable)
	{
		return this.constraints.get(variable);
	}

	public Hashtable<Variable,UniqueList<Variable>> constraints()
	{
		return this.constraints;
	}

	public Stack<Variable> singletons()
	{
		Stack<Variable> singletons = new Stack<>();
		for(Variable variable : this.variables)
		{
			if(variable.singleton())
			{
				singletons.push(variable);
			}
		}
		return singletons;
	}

	public boolean add(Variable variable)
	{
		return this.add(variable, false);
	}

	public boolean add(Variable variable, boolean use_for_branching)
	{
		if(this.variables.add(variable))
		{
			this.constraints.put(variable, new UniqueList<>());
			if(use_for_branching)
			{
				this.branchings.add(variable);
			}
			return true;
		}
		return false;
	}

	public boolean add(Variable a, Variable b)
	{
		UniqueList<Variable> a_neighbors = this.constraint(a);
		if(a_neighbors!=null)
		{
			UniqueList<Variable> b_neighbors = this.constraint(b);
			if(b_neighbors!=null)
			{
				if(a_neighbors.add(b))
				{
					if(b_neighbors.add(a))
					{
						a.incrDegree();
						b.incrDegree();
						return true;
					}
					throw new RuntimeException("A est voisine de B mais B n'est pas voisine de A");
				}
			}
		}
		return false;
	}

	public Stack<Changement> restoreConsistency()
	{
		Stack<Changement> changes = new Stack<>();
		Stack<Variable> singletons = this.singletons();
		while(!singletons.empty())
		{
			Variable singleton = singletons.pop();
			Integer value = singleton.domain().get(0);
			UniqueList<Variable> neighbors = this.constraint(singleton);
			for(Variable neighbor : neighbors)
			{
				if(neighbor.domain().remove(value))
				{
					changes.push(new Changement(neighbor, value));
					if(neighbor.domain().empty())
					{
						return changes;
					}
					else
					{
						if(neighbor.singleton())
						{
							singletons.push(neighbor);
						}
					}
				}
			}
		}
		return changes;
	}
}