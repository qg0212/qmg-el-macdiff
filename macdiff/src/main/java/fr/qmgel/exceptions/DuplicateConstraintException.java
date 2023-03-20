package fr.qmgel.exceptions;

public class DuplicateConstraintException extends Exception
{
	public DuplicateConstraintException(int line_number)
	{
		super(String.format("La contrainte a déjà été défini (l.%d)", line_number));
	}
}