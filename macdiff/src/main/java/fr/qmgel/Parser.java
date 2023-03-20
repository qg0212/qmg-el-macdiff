package fr.qmgel;

import fr.qmgel.exceptions.*;

import java.io.*;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parser
{
	public static Parser instance()
	{
		if(Parser.instance==null)
		{
			Parser.instance = new Parser();
		}
		return Parser.instance;
	}
	private static Parser instance;

	private static final String INT = "(0|[1-9][0-9]*)";
	private static final String COMMENT = "(%.*)";
	private static final String VARIABLE = "([AB]\\s+"+ INT +"\\s+:(\\s+"+ INT +")+)";
	private static final String CONSTRAINT = "(C\\s+"+ INT +"\\s+"+ INT +")";
	private static final String PATTERN = "^("+ CONSTRAINT +"|"+ VARIABLE +")\\s*"+ COMMENT +"?$";

	private File file;
	private Network network;
	private Hashtable<Integer,Variable> variables;
	private Hashtable<Integer,String[]> constraints;

	private void init()
	{
		this.network = new Network();
		this.variables = new Hashtable<>();
		this.constraints = new Hashtable<>();
	}

	public Network parse(File file) throws FileNotFoundException
	{
		if(!file.exists())
		{
			throw new FileNotFoundException();
		}
		this.file = file;

		this.init();

		try
		{
			this.addVariables();
			this.addConstraints();
		}
		catch(Exception exception)
		{
			System.err.println(exception);
			System.exit(1);
		}

		return this.network;
	}

	private void addVariables() throws SyntaxeException, DuplicateIdException, IOException
	{
		int line_number = 1;
		String line;
		FileReader fr = new FileReader(this.file);
		BufferedReader br = new BufferedReader(fr);
		while((line=br.readLine())!=null)
		{
			line = line.trim();
			if(!line.isEmpty() && line.charAt(0)!='%')
			{
				if(line.matches(Parser.PATTERN))
				{
					String[] data = line.split("\\s+");
					if(data[0].equals("C"))
					{
						this.constraints.put(line_number, data);
					}
					else
					{
						Variable variable = this.createVariable(data);
						if(this.variables.get(variable.id())!=null)
						{
							throw new DuplicateIdException(line_number);
						}
						this.variables.put(variable.id(), variable);
						this.network.add(variable, data[0].equals("B"));
					}
				}
				else
				{
					throw new SyntaxeException(line_number);
				}
			}
			line_number += 1;
		}
	}

	private void addConstraints() throws DuplicateConstraintException, UnknowVariableException
	{
		Enumeration<Integer> keys = this.constraints.keys();
		while(keys.hasMoreElements())
		{
			Integer line_number = keys.nextElement();
			String[] data = this.constraints.get(line_number);
			Integer id_a = Integer.parseInt(data[1]);
			Variable variable_a = this.variables.get(id_a);
			if(variable_a!=null)
			{
				Integer id_b = Integer.parseInt(data[2]);
				Variable variable_b = this.variables.get(id_b);
				if(variable_b!=null)
				{
					if(!this.network.add(variable_a, variable_b))
					{
						throw new DuplicateConstraintException(line_number);
					}
				}
				else
				{
					throw new UnknowVariableException(line_number, id_b);
				}
			}
			else
			{
				throw new UnknowVariableException(line_number, id_a);
			}
		}
	}

	private Variable createVariable(String[] data)
	{
		int index=3, id=Integer.parseInt(data[1]);
		Variable variable = new Variable(id);
		do
		{
			Integer value = Integer.parseInt(data[index]);
			variable.domain().add(value);
			index += 1;
		}
		while(index<data.length && data[index].charAt(0)!='%');
		return variable;
	}
}