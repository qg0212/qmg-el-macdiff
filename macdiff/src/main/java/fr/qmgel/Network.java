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
}