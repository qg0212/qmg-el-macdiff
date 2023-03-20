package fr.qmgel;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Structure de données permettant de redéfinir certaines méthodes de la classe ArrayList, et qui n'accepte pas les doublons
 * 
 * @author Quentin G., Elodie L.
 */
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
			for(int i=to; i>=from; i--)
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

	@Override
	public boolean equals(Object object)
	{
		if(object!=null)
		{
			if(this!=object)
			{
				if(this.getClass()==object.getClass())
				{
					return this.equals((UniqueList<T>)object);
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean equals(UniqueList<T> unique_list)
	{
		if(unique_list!=null)
		{
			if(this!=unique_list)
			{
				if(this.count()==unique_list.count())
				{
					for(T element : this.list)
					{
						if(!unique_list.contains(element))
						{
							return false;
						}
					}
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
}