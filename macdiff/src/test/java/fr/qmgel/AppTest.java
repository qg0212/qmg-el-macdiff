package fr.qmgel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class AppTest {
    private Parser pl;
    private Network networkResult, networkExpected, network3Solutions;
    private Variable one, two, three, four;
    private ArrayList<Solution> solutions;

    @BeforeEach
    public void init() throws Exception {
        this.pl = new Parser();
        this.networkExpected = new Network();
        this.networkResult = pl.analyseFile(new File("/Users/quentingermain/development/Java/qmg-el-macdiff/macdiff/src/main/resources/default.i"));
        this.network3Solutions = pl.analyseFile(new File("/Users/quentingermain/development/Java/qmg-el-macdiff/macdiff/src/main/resources/ct.i"));
    }
   
    @Test
    public void parserSuccessTest() throws Exception {
        this. one = new Variable(1);
        this.two = new Variable(2);
        this.three = new Variable(3);
        this.four = new Variable(4);
        one.domain().add(1);
        one.domain().add(2);
        two.domain().add(1);
        two.domain().add(2);
        three.domain().add(1);
        three.domain().add(2);
        four.domain().add(3);
        four.domain().add(4);
        four.domain().add(5);
        networkExpected.add(one, true);
        networkExpected.add(two, true);
        networkExpected.add(three, true);
        networkExpected.add(four, true);
        networkExpected.add(one, two);
        networkExpected.add(one, three);
        networkExpected.add(two, three);
        networkExpected.add(three, four);   
        assertEquals(networkResult, networkExpected);
    }

    @Test
    public void noSolutionTest(){
        BasicSolver solver = new BasicSolver(this.networkResult);
        ArrayList<Solution> solutions = solver.solve(true);
        assertTrue(solutions.isEmpty());
    }

    @Test
    public void numberOfSolutionsTest() throws SyntaxeException{
        BasicSolver solver = new BasicSolver(this.network3Solutions);
        ArrayList<Solution> solutions = solver.solve(true);
        assertEquals(3, solutions.size());
    }

    @Test
    public void branchingTest(){
        BasicSolver solver = new BasicSolver(this.network3Solutions);
        Variable branching_variable = solver.branchingVariable();
        assertEquals(branching_variable, solver.network().variables().get(0));
    }

    @Test
    public void singletonTest(){
        assertTrue(this.network3Solutions.variables().get(3).singleton());
        assertFalse(this.network3Solutions.variables().get(0).singleton());
    }

    @Test
    public void restoreConsistencyTest(){
        BasicSolver solver = new BasicSolver(this.networkResult);
        Stack<Changement> changes = solver.network().restoreConsistency();
        Stack<Changement> changesEmpty = new Stack<>();
        assertEquals(changes.size(), changesEmpty.size()); 
    }

}
