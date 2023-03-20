package fr.qmgel;

import java.util.ArrayList;
import java.util.Hashtable;

public class Network
{
	private UniqueList<Variable> variables;
	private ArrayList<Variable> branchings;
	private Hashtable<Variable,UniqueList<Variable>> constraints;

	/**
	 * Construit un réseau de contraintes vide
	 */
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

	/**
	 * Cherche toutes les variables du réseau qui sont des singletons
	 * 
	 * @return la liste des variables qui sont des singletons, représentée par une pile
	 */
	public MyStack<Variable> singletons()
	{
		MyStack<Variable> singletons = new MyStack<>();
		for(Variable variable : this.variables)
		{
			if(variable.singleton())
			{
				singletons.push(variable);
			}
		}
		return singletons;
	}

	/**
	 * Ajoute une variable auxiliare au réseau
	 * 
	 * @param variable la variable à ajouter
	 * 
	 * @return <code>true</code> si l'ajout a été fait, <code>false</code> sinon
	 * 
	 * @see #add(Variable,boolean)
	 */
	public boolean add(Variable variable)
	{
		return this.add(variable, false);
	}

	/**
	 * Ajoute une variable au réseau
	 * 
	 * @param variable la variable à ajouter
	 * @param use_for_branchings indique si la variable peut être utilisée pour un branchement (<code>true</code>) ou non (<code>false</code>)
	 * 
	 * @return <code>true</code> si l'ajout a été fait, <code>false</code> sinon
	 */
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

	/**
	 * Ajoute une contrainte au réseau
	 * 
	 * @param a la première variable de la contrainte
	 * @param b la deuxième variable de la contrainte
	 * 
	 * @return <code>true</code> si la contrainte a été ajouté, <code>false</code> sinon
	 * 
	 * @throws RuntimeException si l'une des variables est reliée à l'autre, mais pas l'inverse
	 */
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

	/**
	 * Restaure la cohérence des domaines du réseau
	 * 
	 * @return la liste des changements effectués, sous forme d'une pile
	 */
	public MyStack<Changement> restoreConsistency()
	{
		MyStack<Changement> changes = new MyStack<>();
		MyStack<Variable> singletons = this.singletons();
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