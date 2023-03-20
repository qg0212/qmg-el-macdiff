package fr.qmgel;

import java.io.File;
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
		String filename = (args.length>0 ? args[0] : "default.i");
		File file = new File("/Users/quentingermain/development/Java/qmg-el-macdiff/macdiff/src/main/resources/".concat(filename));

		Parser parser = Parser.instance();
		Network network = null;

		try
		{
			network = parser.parse(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		BasicSolver solver = new BasicSolver(network);
		ArrayList<Solution> solutions = solver.solve(true);

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