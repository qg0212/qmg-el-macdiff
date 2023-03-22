package fr.qmgel.exceptions;

public class SyntaxeException extends Exception
{
	public SyntaxeException(int line_number)
	{
		super(String.format("Erreur de syntaxe (l.%d)", line_number));
	}
}