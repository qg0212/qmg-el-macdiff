package fr.qmgel;

public class Variable
{
	private int id, degree;
	private UniqueList<Integer> domain;

	/**
	 * Construit une variable avec un identifiant spécifique
	 * 
	 * @param id l'identifiant de la variable
	 */
	public Variable(int id)
	{
		this.id = id;
		this.degree = 0;
		this.domain = new UniqueList<>();
	}

	public int id()
	{
		return this.id;
	}

	public int degree()
	{
		return this.degree;
	}

	public UniqueList<Integer> domain()
	{
		return this.domain;
	}

	/**
	 * Indique si la variable est un singleton
	 * 
	 * @return <code>true</code> si la variable est un singleton, <code>false</code> sinon
	 */
	public boolean singleton()
	{
		return (this.domain.count()==1);
	}

	/**
	 * Incrémente le degré de la variable
	 * Le degré est le nombre de variables auxquelles cette variable est reliée par une contrainte
	 */
	public void incrDegree()
	{
		this.degree += 1;
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
					return this.equals((Variable)object);
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean equals(Variable variable)
	{
		if(variable!=null)
		{
			if(this!=variable)
			{
				return (this.id==variable.id() && this.degree==variable.degree() && this.domain.equals(variable.domain()));
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		String variable = String.format("Variable %d :", this.id);
		if(this.domain.empty())
		{
			variable = variable.concat(" empty!");
		}
		else
		{
			for(Integer value : this.domain)
			{
				variable = variable.concat(String.format(" %d", value));
			}
		}
		return variable;
	}
}