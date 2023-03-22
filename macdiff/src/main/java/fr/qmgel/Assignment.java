package fr.qmgel;

public class Assignment
{
	protected Variable variable;
	protected Integer value;
	protected boolean equal_assignment;

	/**
	 * Construit une assignation
	 * Une assignation d'égalité correspont à variable=value, tandis qu'une assignation de différence coorespond à variable!=value
	 * 
	 * @param variable la variable qui a été assigné
	 * @param value la valeur de l'assignation
	 * @param equal_assignmen indique s'il s'agit d'une assignation d'égalité (<code>true</code>) ou de différence (<code>false</code>)
	 */
	public Assignment(Variable variable, Integer value, boolean equal_assignment)
	{
		this.variable = variable;
		this.value = value;
		this.equal_assignment = equal_assignment;
	}

	/**
	 * Indique s'il s'agit d'une assignation d'égalité ou de différence
	 * 
	 * @return <code>true</code> si c'est une assignation d'égalité, <code>false</code> sinon
	 */
	public boolean equalAssignment()
	{
		return this.equal_assignment;
	}

	/**
	 * Ajoute la variable auxiliaire au réseau, afin de construire une connaissance
	 * Consulter le rapport pour des explications plus détaillées
	 * 
	 * @param network le réseau concerné
	 * @param master la variable auxiliaire maître
	 * @param control_value la valeur de contrôle
	 */
	public void knowledge(Network network, Variable master, Integer control_value)
	{
		int id = network.variableId().next();
		Variable auxiliary = new Variable(id);

		auxiliary.domain().add(this.value);
		auxiliary.domain().add(control_value);
		master.domain().add(control_value);
		
		network.add(auxiliary);
		network.add(this.variable, auxiliary);
		network.add(auxiliary, master);
	}
}