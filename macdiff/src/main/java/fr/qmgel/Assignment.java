package fr.qmgel;

public class Assignment
{
	protected Variable variable;
	protected Integer value;
	protected boolean equal_assignment;

	public Assignment(Variable variable, Integer value, boolean equal_assignment)
	{
		this.variable = variable;
		this.value = value;
		this.equal_assignment = equal_assignment;
	}

	public boolean equalAssignment()
	{
		return this.equal_assignment;
	}

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