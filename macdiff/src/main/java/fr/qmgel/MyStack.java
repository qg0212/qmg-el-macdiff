package fr.qmgel;

/**
 * Implémentation d'une collection d'élément sous forme d'une pile.
 * 
 * @author Quentin G.
 */
public class MyStack<T>
{
	private static final int DEFAULT_CAPACITY = 16;

	protected int head;
	protected int capacity;
	protected T[] stack;

	/**
	 * Construit une pile vide avec une capacité initiale de 16.
	 */
	public MyStack()
	{
		this(MyStack.DEFAULT_CAPACITY);
	}

	/**
	 * Construit une pile vide avec une capacité initiale spécifique.
	 */
	public MyStack(int capacity)
	{
		this.head = -1;
		this.capacity = capacity;
		this.stack = (T[])(new Object[capacity]);
	}

	/**
	 * Indique le nombre d'éléments présent dans la pile.
	 * 
	 * @return le nombre d'éléments dans la liste.
	 */
	public int count()
	{
		return (this.head + 1);
	}

	/**
	 * Test si la pille est vide.
	 * 
	 * @return <code>true</code> si la pile est vide, <code>false</code> sinon.
	 */
	public boolean empty()
	{
		return (this.head==-1);
	}

	/**
	 * Test si la pile est pleine.
	 * 
	 * @return <code>true</code> si la pile est pleine, <code>false</code> sinon.
	 */
	private boolean full()
	{
		return (this.head==(this.capacity - 1));
	}

	/**
	 * Pousse un élément sur le desssus de la pile.
	 * 
	 * @param element l'élément à ajouter.
	 */
	public void push(T element)
	{
		if(this.full())
		{
			this.incrCapacity();
		}
		this.head += 1;
		this.stack[this.head] = element;
	}

	/**
	 * Supprime l'objet en haut de la pile.
	 * 
	 * @return l'élément supprimé.
	 */
	public T pop()
	{
		if(!this.empty())
		{
			T element = this.stack[this.head];
			this.stack[this.head] = null;
			this.head -= 1;
			if(this.head<this.capacity*0.4)
			{
				this.decCapacity();
			}
			return element;
		}
		return null;
	}

	/**
	 * Regarde l'objet qui se trouve en haut de la pile sans le supprimer.
	 * 
	 * @return l'élément en haut de la pile.
	 */
	public T peek()
	{
		if(!this.empty())
		{
			return this.stack[this.head];
		}
		return null;
	}

	/**
	 * Efface la pile
	 */
	public void clear()
	{
		while(this.head>-1)
		{
			this.stack[this.head] = null;
			this.head -= 1;
		}
	}

	/**
	 * Indique si la pile contient un élément spécifique.
	 * 
	 * @param element l'élément dont on teste la présence.
	 * 
	 * @return <code>true</code> si l'élément est dans la pile, <code>false</code> sinon.
	 */
	public boolean contains(T element)
	{
		for(int i=0; i<=this.head; i++)
		{
			if(this.stack[i].equals(element))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Divise par 2 la capacité de la pile.
	 */
	private void decCapacity()
	{
		if(this.capacity>2)
		{
			this.capacity /= 2;
			T[] new_stack = (T[])(new Object[this.capacity]);
			for(int i=0; i<=this.head; i++)
			{
				new_stack[i] = this.stack[i];
			}
			this.stack = new_stack;
		}
	}

	/**
	 * Multiplie par 2 la capacité de la pile.
	 */
	private void incrCapacity()
	{
		this.capacity *= 2;
		T[] new_stack = (T[])(new Object[this.capacity]);
		for(int i=0; i<=this.head; i++)
		{
			new_stack[i] = this.stack[i];
		}
		this.stack = new_stack;
	}
}