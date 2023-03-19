package fr.qmgel;

public class Changement
{
	private Variable variable;
	private Integer value;

	public Changement(Variable variable, Integer value)
	{
		this.variable = variable;
		this.value = value;
	}

	public Variable variable()
	{
		return this.variable;
	}

	public Integer value()
	{
		return this.value;
	}

	public boolean restore()
	{
		return this.variable.domain().add(this.value);
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
					return this.equals((Changement)object);
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean equals(Changement changement)
	{
		if(changement!=null)
		{
			if(this!=changement)
			{
				return (this.variable.equals(changement.variable()) && this.value.equals(changement.value()));
			}
			return true;
		}
		return false;
	}
}