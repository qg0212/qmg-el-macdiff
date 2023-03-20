package fr.qmgel.exceptions;

public class UnknowVariableException extends Exception
{
	public UnknowVariableException(int line_number, int variable_id)
	{
		super(String.format("La variable %d n'a pas été définie (l.%d)", variable_id, line_number));
	}
}