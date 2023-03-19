package fr.qmgel;

public class Variable
{
	private int id, degree;
	private UniqueList<Integer> domain;

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

	public boolean singleton()
	{
		return (this.domain.count()==1);
	}

	public void incrDegree()
	{
		this.degree += 1;
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