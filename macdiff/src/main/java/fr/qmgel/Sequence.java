package fr.qmgel;

public class Sequence
{
	private int current, step;

	/**
	 * Construit une séquence
	 * 
	 * @param start la valeur de départ
	 * @param step l'intervalle entre deux valeurs
	 */
	public Sequence(int start, int step)
	{
		this.current = start - step;
		this.step = step;
	}

	/**
	 * Récupère la valeur suivante de la séquence
	 * 
	 * @return la valeur suivante
	 */
	public int next()
	{
		this.current += this.step;
		return this.current;
	}
}