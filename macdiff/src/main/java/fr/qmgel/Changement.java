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
}