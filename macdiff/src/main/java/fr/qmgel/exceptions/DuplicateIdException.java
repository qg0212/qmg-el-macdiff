package fr.qmgel.exceptions;

public class DuplicateIdException extends Exception
{
	public DuplicateIdException(int line_number)
	{
		super(String.format("L'identifiant est déjà utilisé (l.%d)", line_number));
	}
}