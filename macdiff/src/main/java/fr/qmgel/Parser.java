package fr.qmgel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Parser
{
	/**
	 * Traduit le fichier d'instance en un objet Network
     * @param file le fichier d'instance décrivant le réseau de contrainte
	 * @return le reseau instancié avec ses variables, leur domaine et les contraintes
	 * @throws SyntaxeException
	 */
    public Network analyseFile(File file) throws SyntaxeException
    {
        ArrayList<String> tabLine = readFile(file);
        Hashtable<String,Variable> variables = new Hashtable<>();
        ArrayList<ArrayList<String>> constraints = new ArrayList<>();

        for(String line : tabLine)
        {
            if(line.matches("^[AB]\\s[1-9][0-9]*\\s:(\\s[0-9]+)+\\s*(%.*)?$")) //Si la ligne correspond à l'expression d'une variable
            {
                String[] data = line.split("\\s+"); //Separer la ligne selon les espaces
                /*boolean branchement = false;
                if(data[0].equals("B"))
                {
                branchement = true;
                }*/

                int id = Integer.parseInt(data[1]); //Recuperer l'identifiant de la variable
                Variable variable;
                if(variables.containsKey(Integer.toString(id))) //Si la variable existe deja
                {
                    variable = variables.get(Integer.toString(id));
                }
                else
                {
                    variable = new Variable(id); //Initialisation d'une Variable avec l'identifiant
                }

                //Recuperer les valeurs du domaine de la variable
                int i = 3;
                while(i<data.length && !data[i].substring(0,1).equals("%"))
                {
                    int val = Integer.parseInt(data[i]);
                    variable.domain().add(val); 
                    i++;
                }
                variables.put(data[1], variable);
            }
            else if(line.matches("^C(\\s[1-9][0-9]*){2}\\s*(%.*)?")) //Regex correspondant à l'expression d'une contrainte
			{
                String[] dataConstraints = line.split("\\s+"); //Separer la ligne selon les espaces
				ArrayList<String> contDiff = new ArrayList<>();

                int i = 1;
                while(i<dataConstraints.length && !dataConstraints[i].substring(0,1).equals("%"))
				{
                    if(variables.containsKey(dataConstraints[i])) //Si la variable existe deja
                    {
                        contDiff.add(dataConstraints[i]);
                        i++;
                    }
                    else //Si elle n'existe pas encore
                    {
                        variables.put(dataConstraints[i], new Variable(Integer.parseInt(dataConstraints[i])));
                    }
				}
				constraints.add(contDiff);
			}
            else if(line.matches("^C\\s[0-9]{0,1}$|^C.*[,;.=+\\/_<>+a-zAD-Zéè&@#ç-].*") || line.matches("^B\\s[1-9][0-9]*\\s*:*[0-9]*(\\s[0-9])*\\s*$|^B.*[.;,=+^\\_/\"<>a-zAD-Z-].*$"))
            {
                throw new SyntaxeException();
            }
            else if(line.matches("^B\\s0[0-9]*.+")) //Identifiant de type "01"
            {
                System.out.println(">>>>Warning: Identifiant d'une variable commançant par un 0. Elle n'a pas été stocké.");
            }
        }
        return addToNetwork(variables, constraints);
    }

    /**
	 * Convertit le fichier d'instance en une liste de String contenant chaque ligne du fichier
     * @param file le fichier d'instance décrivant le réseau de contrainte
	 * @return la liste des lignes du fichier
	 */
    public ArrayList<String> readFile(File file)
    {
        String line;
        ArrayList<String> tabLine = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) //Lire tant qu'on n'arrive pas a la fin du fichier
            {
            	tabLine.add(line);
            }
            br.close();
        } catch (Exception e)
        {
            //Exception si le fichier ne peut pas etre lu
            System.out.println(">>>>Erreur: Le fichier n'a pas pu etre lu.");
        }
        return tabLine;
    }

   	/**
	 * Traduit le fichier d'instance en un objet Network
     * @param variables le fichier d'instance décrivant le réseau de contrainte
	 * @return la liste
	 */
    public Network addToNetwork(Hashtable<String, Variable> variables, ArrayList<ArrayList<String>> constraints)
    {
        //Initalisation d'un reseau et ajout des variables
        Network network = new Network();
        variables.forEach((key, value) -> network.add(value, true));

        //Ajout des contraintes
        for(int x = 0; x < constraints.size(); x++)
		{
			network.add(variables.get(constraints.get(x).get(0)), variables.get(constraints.get(x).get(1)));
		}
        return network;
    }
}
