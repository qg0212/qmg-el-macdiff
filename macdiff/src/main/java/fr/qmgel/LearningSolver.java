package fr.qmgel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class LearningSolver extends Solver
{
	protected int number_of_starts;
	protected int current_number_of_branchings;
	protected boolean restart, echecs[];
	protected LinkedList<Assignment> branchings;
	protected UniqueList<Variable> all_branchings;
	protected LinkedList<Variable> last_start_variables;

	public LearningSolver(Network network)
	{
		super(network);
		this.echecs = new boolean[] {false, false, false};
		this.number_of_starts = 0;
		this.branchings = new LinkedList<>();
		this.all_branchings = new UniqueList<>();
		this.last_start_variables = new LinkedList<>();
	}

	public int numberOfStarts()
	{
		return this.number_of_starts;
	}

	public LinkedList<Variable> lastStartVariables()
	{
		return this.last_start_variables;
	}

	/**
	 * Recherche de solutions du réseau en gérant les redémarrages
	 * 
	 * @param all_solutions indique si on cherche toutes les solutions (<code>true</code>) ou seulement une (<code>false</code>)
	 * 
	 * @return la liste des solutions trouvées (vide s'il n'y en a pas)
	 */
	public ArrayList<Solution> solve(boolean all_solutions)
	{
		ArrayList<Solution> solutions = new ArrayList<>();

		do
		{
			this.reset();
			this.number_of_starts += 1;
			solutions.addAll(this.solve_aux(all_solutions));
			if(this.echecs[0])
			{
				this.addKnowledge();
			}
		}
		while(this.restart);

		return solutions;
	}

	/**
	 * Recherche les solutions jusqu'au prochain redémarrage de la recherche
	 * 
	 * @param all_solutions indique si on cherche toutes les solutions (<code>true</code>) ou seulement une (<code>false</code>)
	 * 
	 * @return la liste des solutions trouvées (vide s'il n'y en a pas)
	 */
	private ArrayList<Solution> solve_aux(boolean all_solutions)
	{
		ArrayList<Solution> solutions = new ArrayList<>();

		MyStack<Changement> changes = this.network.restoreConsistency();
		if(changes.empty() || !changes.peek().variable().domain().empty())
		{
			Variable branching_variable = this.branchingVariable();
			if(branching_variable==null)
			{
				//This is a solution
				Solution solution = new Solution(this.network, this.number_of_branchings);
				solutions.add(solution);
				Variable master = new Variable(this.network.variableId().next());
				this.network.add(master);
				Sequence control_values = new Sequence(-1, -1);
				for(Variable variable : solution.assignments().keySet())
				{
					Assignment assignment = new Assignment(variable, variable.domain().get(0), true);
					assignment.knowledge(this.network, master, control_values.next());
				}
			}
			else
			{
				if(!this.restart())
				{
					this.all_branchings.add(branching_variable);
					this.branchings.addLast(new Assignment(branching_variable, branching_variable.domain().get(0), true));

					//First branch
					this.incrNumberOfBranchings();
					ArrayList<Integer> queue = branching_variable.domain().remove(1, -1);
					solutions.addAll(this.solve_aux(all_solutions));
					branching_variable.domain().add(queue);

					//Second branch
					if(!this.restart() && (solutions.isEmpty() || all_solutions))
					{
						this.branchings.removeLast();
						this.branchings.addLast(new Assignment(branching_variable, branching_variable.domain().get(0), false));
						Integer head = branching_variable.domain().remove(0);
						if(branching_variable.singleton())
						{
							this.incrNumberOfBranchings();
						}
						solutions.addAll(this.solve_aux(all_solutions));
						branching_variable.domain().add(head);
						if(!this.restart())
						{
							this.branchings.removeLast();
						}
					}
				}
			}
		}
		else
		{
			this.echecs[0] = true;
		}

		//Rollback of changes
		while(!changes.empty())
		{
			changes.pop().restore();
		}

		return solutions;
	}

	/**
	 * Ajoute une connaissance au réseau
	 * Consulter le rapport pour des explications plus détaillées
	 */
	private void addKnowledge()
	{
		if(!this.branchings.isEmpty())
		{
			Sequence control_values = new Sequence(-1, -1);
			Variable master = new Variable(this.network.variableId().next());
			this.network.add(master);
			Iterator<Assignment> iterator = this.branchings.iterator();
			Assignment current;
			do
			{
				current = iterator.next();
				current.knowledge(this.network, master, control_values.next());
			}
			while(current.equalAssignment() && iterator.hasNext());
		}
	}

	/**
	 * Incrémente le nombre de branchements effectuées
	 */
	private void incrNumberOfBranchings()
	{
		this.number_of_branchings += 1;
		this.current_number_of_branchings += 1;
	}

	/**
	 * Indique si un redémarrage doit être effectué
	 * 
	 * @return <code>true</code> si la recherche doit redémarrer, <code>false</code> sinon
	 */
	private boolean restart()
	{
		if(!this.restart)
		{
			if(this.number_of_starts<3 || this.echecs[0] || this.echecs[1] || this.echecs[2])
			{
				if(this.current_number_of_branchings>=4)
				{
					double couverture = 1.0 - ((double)this.network.currentNumberOfAssignments() / (double)this.network.initialNumberOfAssignments());
					this.restart = (couverture < 0.02*this.current_number_of_branchings);
				}
			}
		}
		return this.restart;
	}

	/**
	 * Réinitialise les paramètres d'une recherche
	 */
	private void reset()
	{
		this.current_number_of_branchings = 0;
		this.restart = false;
		this.echecs[2] = this.echecs[1];
		this.echecs[1] = this.echecs[0];
		this.echecs[0] = false;
		this.branchings.clear();
	}

	@Override
	public Variable branchingVariable()
	{
		Variable branching_variable = null;
		
		if(this.branchings.isEmpty() && !this.all_branchings.empty())
		{
			branching_variable = this.startVariable();
			this.updateLastStartVariables(branching_variable);
		}
		else
		{
			branching_variable = super.branchingVariable();
		}

		return branching_variable;
	}

	/**
	 * Recherche la variable de départ de la recherche
	 * 
	 * @return la variable à utiliser pour démarrer la recherche
	 */
	public Variable startVariable()
	{
		double tmp, ratio=Double.POSITIVE_INFINITY;
		Variable start_variable = null;
		for(Variable branching : this.network.branchings())
		{
			if(start_variable!=branching && !branching.singleton() && !this.last_start_variables.contains(branching))
			{
				tmp = (double)branching.domain().count() / (double)branching.degree();
				if(tmp<ratio)
				{
					ratio = tmp;
					start_variable = branching;
				}
			}
			UniqueList<Variable> neighbors = this.network.constraint(branching);
			for(Variable neighbor : neighbors)
			{
				if(start_variable!=neighbor && !neighbor.singleton() && !this.last_start_variables.contains(neighbor))
				{
					tmp = (double)neighbor.domain().count() / (double)neighbor.degree();
					if(tmp<ratio)
					{
						ratio = tmp;
						start_variable = neighbor;
					}
				}
			}
		}
		this.updateLastStartVariables(start_variable);
		return start_variable;
	}

	/**
	 * Actualise la liste des dernières variables utilisées pour un démarrage en conservant seulement les deux dernières
	 * 
	 * @param variable la dernière variable utilisée
	 */
	private void updateLastStartVariables(Variable variable)
	{
		this.last_start_variables.addFirst(variable);
		if(this.last_start_variables.size()>2)
		{
			this.last_start_variables.removeLast();
		}
	}
}