package tests;

import state.Item;
import state.State;
import parser_LR0.Grammar;
import parser_LR0.Parser;

import java.util.List;
import java.util.Objects;

public class Tests {
    private Grammar grammar;
    private Parser parser;

    public Tests() {
        grammar = new Grammar("./res/data/g1Test.txt");
        parser = new Parser(grammar);
    }

    public void runAllTests() {
        closureTest1();
        closureTest2();
        closureTest3();
        goToTest1();
        goToTest2();
        canonicalCollectionTest1();
        canonicalCollectionTest2();
    }

    private void closureTest1() {
        parser.canonicalCollection();
        Object[] result = parser.closure(new Item(
                parser.getWorkingGrammar().getStartSymbol(),
                parser.getWorkingGrammar().getProductionsForNonTerminal(parser.getGrammar().getStartSymbol()).get(0),
                0)).items.toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a", "A"), 0));
        System.out.println("Closure test 1 successful");
    }

    public void closureTest2() {
        grammar = new Grammar("./res/data/g2Test.txt");
        parser = new Parser(grammar);
        parser.canonicalCollection();
        Object[] result = parser.closure(new Item(
                parser.getWorkingGrammar().getStartSymbol(),
                parser.getWorkingGrammar().getProductionsForNonTerminal(parser.getGrammar().getStartSymbol()).get(0),
                0)).items.toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a"), 0));
        System.out.println("Closure test 2 successful");
    }

    public void closureTest3() {
        grammar = new Grammar("./res/data/g3Test.txt");
        parser = new Parser(grammar);
        parser.canonicalCollection();
        Object[] result = parser.closure(new Item(
                parser.getWorkingGrammar().getStartSymbol(),
                parser.getWorkingGrammar().getProductionsForNonTerminal(parser.getGrammar().getStartSymbol()).get(0),
                0)).items.toArray();
        assert result.length == 3;
        assert Objects.equals(result[1], new Item("A", List.of("S"), 0));
        System.out.println("Closure test 3 successful");
    }

    public void goToTest1() {
        grammar = new Grammar("./res/data/g1Test.txt");
        parser = new Parser(grammar);
        parser.canonicalCollection();
        State state = parser.closure(new Item(
                parser.getWorkingGrammar().getStartSymbol(),
                parser.getWorkingGrammar().getProductionsForNonTerminal(parser.getGrammar().getStartSymbol()).get(0),
                0));
        State result = parser.goTo(state, state.getSymbolsSucceedingTheDot().get(0));
        assert result.items.size() == 2;
        assert Objects.equals(result.items.toArray()[1], new Item("A", List.of("a", "b"), 0));
        System.out.println("Go To Test 1 Successful");
    }

    public void goToTest2() {
        grammar = new Grammar("./res/data/g2Test.txt");
        parser = new Parser(grammar);
        parser.canonicalCollection();
        State state = parser.closure(new Item(
                parser.getWorkingGrammar().getStartSymbol(),
                parser.getWorkingGrammar().getProductionsForNonTerminal(parser.getGrammar().getStartSymbol()).get(0),
                0));
        State result = parser.goTo(state, state.getSymbolsSucceedingTheDot().get(0));
        assert result.items.size() == 1;
        assert Objects.equals(result.items.toArray()[1], new Item("S0", List.of("a"), 1));
        System.out.println("Go To Test 2 Successful");
    }

    public void canonicalCollectionTest1() {
        grammar = new Grammar("./res/data/g1Test.txt");
        parser = new Parser(grammar);
        List<State> result = parser.canonicalCollection().getStates();
        assert result.size() == 6;
        assert Objects.equals(result.get(result.size() - 1).items.toArray()[0], new Item("A", List.of("a", "b"), 2));
        System.out.println("Canonical Test 1 Successful");
    }

    public void canonicalCollectionTest2() {
        grammar = new Grammar("./res/data/g2Test.txt");
        parser = new Parser(grammar);
        List<State> result = parser.canonicalCollection().getStates();
        assert result.size() == 1;
        assert Objects.equals(result.get(0).items.toArray()[0], new Item("A", List.of("a"), 1));
        System.out.println("Canonical Test 2 Successful");
    }
}
