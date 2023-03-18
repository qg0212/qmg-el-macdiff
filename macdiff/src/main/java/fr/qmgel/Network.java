package fr.qmgel;

import java.util.ArrayList;
import java.util.Hashtable;

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

	@Override
	public String toString()
	{
		String network = "> Variables :";
		for(Variable variable : this.variables)
		{
			network = network.concat(String.format("\n>>> %s", variable));
		}
		network = network.concat("\n> Contraintes :");
		for(Variable variable : this.variables)
		{
			network = network.concat(String.format("\n>>> Contraintes de %d :", variable.id()));
			for(Variable neighbor : this.constraint(variable))
			{
				network = network.concat(String.format(" %d", neighbor.id()));
			}
		}
		return network;
	}
}