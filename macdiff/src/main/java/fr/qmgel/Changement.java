package fr.qmgel;

public class Changement
{
	private Variable variable;
	private Integer value;

	/**
	 * Enregistre un changement pour une variable et une valeur spécifique
	 * 
	 * @param variable la variable concernée par le changement
	 * @param value la valeur qui a été retiré du domain de la variable
	 */
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

	/**
	 * Restaure le changement
	 * 
	 * @return <code>true</code> si la restauration a réussi, <code>false</code> sinon
	 */
	public boolean restore()
	{
		return this.variable.domain().add(this.value);
	}
}