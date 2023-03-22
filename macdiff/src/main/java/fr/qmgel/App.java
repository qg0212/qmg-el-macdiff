package fr.qmgel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Quentin G., Elodie L.
 */
public class App
{
	/**
	 * Point d'entrée dans l'application
	 * 
	 * @param args les paramètres passés à l'application
	 */
	public static void main(String[] args)
	{
		Parser parser = Parser.instance();
		Network network = null;

		String solver_name = "basic";
		File file = null;
		boolean all_solutions = false;
		
		int index = 0;
		while(index<args.length)
		{
			switch(args[index])
			{
				case "-a":
				case "--all":
				{
					all_solutions = true;
					break;
				}
				case "-f":
				case "--file":
				{
					index += 1;
					String path = args[index].replaceAll("^r/", "/Users/quentingermain/development/Java/qmg-el-macdiff/macdiff/src/main/resources/");
					file = new File(path);
					break;
				}
				case "-s":
				case "--solver":
				{
					index += 1;
					solver_name = args[index];
					break;
				}
			}
			index += 1;
		}

		if(file!=null)
		{
			try
			{
				network = parser.parse(file);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			Solver solver = Solver.get(solver_name, network);
			ArrayList<Solution> solutions = solver.solve(all_solutions);

			System.out.println("> Solution(s) :");
			if(solutions.isEmpty())
			{
				System.out.println(">>>>> Aucune solution trouvée...");
			}
			else
			{
				for(Solution solution : solutions)
				{
					System.out.printf(">>> %s\n", solution);
				}
				System.out.printf(">>>>> %d solution(s) trouvée(s) !\n", solutions.size());
			}
		}
	}
}