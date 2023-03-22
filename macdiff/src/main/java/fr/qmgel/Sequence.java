package fr.qmgel;

public class Sequence
{
	private int current, step;

	public Sequence(int start, int step)
	{
		this.current = start - step;
		this.step = step;
	}

	public int next()
	{
		this.current += this.step;
		return this.current;
	}
}