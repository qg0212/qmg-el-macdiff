package fr.qmgel;

import java.util.ArrayList;
import java.util.Iterator;

public class UniqueList<T> implements Iterable<T>
{
	private ArrayList<T> list;

	public UniqueList()
	{
		this.list = new ArrayList<>();
	}

	public int count()
	{
		return this.list.size();
	}

	public boolean empty()
	{
		return this.list.isEmpty();
	}

	public T get(int index)
	{
		return this.list.get(index);
	}

	public boolean contains(T element)
	{
		return this.list.contains(element);
	}

	public boolean add(T element)
	{
		if(!this.contains(element))
		{
			return this.list.add(element);
		}
		return false;
	}

	public boolean add(Iterable<T> list)
	{
		int count = this.count();
		for(T element : list)
		{
			this.add(element);
		}
		return (this.count()>count);
	}

	public T remove(int index)
	{
		return this.list.remove(index);
	}

	public boolean remove(T element)
	{
		return this.list.remove(element);
	}

	public ArrayList<T> remove(int from, int to)
	{
		to = (to + this.count()) % this.count();
		from = (from + this.count()) % this.count();
		ArrayList<T> sub_list = new ArrayList<>();
		if(from>=0 && to<this.count() && from<=to)
		{
			for(int i=from; i<=to; i++)
			{
				sub_list.add(this.remove(i));
			}
		}
		else
		{
			throw new RuntimeException("Valeurs incorrectes : ("+from+"; "+to+")");
		}
		return sub_list;
	}

	public Iterator<T> iterator()
	{
		return this.list.iterator();
	}
}