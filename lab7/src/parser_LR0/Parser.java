package parser_LR0;

import parsingTable.ParsingTable;
import parsingTable.RowTable;
import parsingTree.ParsingTree;
import state.Item;
import state.State;
import state.StateType;
import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Parser {
    private final Grammar grammar;
    private final Grammar workingGrammar;

    private final List<Pair<String, List<String>>> orderedProductions;

    public Parser(Grammar grammar) {
        this.grammar = grammar;

        if (this.grammar.isEnriched()) {
            this.workingGrammar = this.grammar;
        } else {
            this.workingGrammar = this.grammar.getEnrichedGrammar();
        }

        orderedProductions = this.grammar.getOrderedProductions();
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Grammar getWorkingGrammar() {
        return workingGrammar;
    }

    private String getDotPrecededNonTerminal(Item item) {
        try {
            String term = item.rhs.get(item.dotPosition);
            if (!grammar.getNonterminals().contains(term)) {
                return null;
            }

            return term;
        } catch (Exception e) {
            return null;
        }
    }

    public State closure(Item item) {

        Set<Item> oldClosure;
        Set<Item> currentClosure = Set.of(item);

        do {
            oldClosure = currentClosure;
            Set<Item> newClosure = new LinkedHashSet<>(currentClosure);
            for (Item i : currentClosure) {
                String nonTerminal = getDotPrecededNonTerminal(i);
                if (nonTerminal == null)
                    continue;

                for (List<String> prod : grammar.getProductionsForNonTerminal(nonTerminal)) {
                    Item currentItem = new Item(nonTerminal, prod, 0);
                    newClosure.add(currentItem);
                }
            }
            currentClosure = newClosure;

        } while (!oldClosure.equals(currentClosure));

        return new State(currentClosure);
    }

    public State goTo(State state, String element) {
        Set<Item> result = new LinkedHashSet<>();
        for (Item item : state.items) {
            String nonTerminal;
            if (item.dotPosition >= item.rhs.size()) {
                nonTerminal = "";
            } else {
                nonTerminal = item.rhs.get(item.dotPosition);
            }

            if (Objects.equals(nonTerminal, element)) {
                Item nextItem = new Item(item.lhs, item.rhs, item.dotPosition + 1);
                State newState = closure(nextItem);
                result.addAll(newState.items);
            }
        }
        return new State(result);
    }

    public CanonicalCollection canonicalCollection() {
        CanonicalCollection canonicalCollection = new CanonicalCollection();
        Item item = new Item(
                workingGrammar.getStartSymbol(),
                workingGrammar.getProductionsForNonTerminal(workingGrammar.getStartSymbol()).get(0),
                0);

        canonicalCollection.addState(closure(item));

        int i = 0;
        while (i < canonicalCollection.states.size()) {
            for (String symbol : canonicalCollection.states.get(i).getSymbolsSucceedingTheDot()) {
                State newState = goTo(canonicalCollection.states.get(i), symbol);
                if (newState.items.size() != 0) {
                    int indexInStates = canonicalCollection.states.indexOf(newState);
                    if (indexInStates == -1) {
                        canonicalCollection.addState(newState);
                        indexInStates = canonicalCollection.states.size() - 1;
                    }
                    canonicalCollection.connectStates(i, symbol, indexInStates);
                }
            }
            ++i;
        }

        return canonicalCollection;
    }

    public ParsingTable createParsingTable(CanonicalCollection canonicalCollection) {
        ParsingTable parsingTable = new ParsingTable();

        for (int i = 0; i < canonicalCollection.states.size(); i++) {
            //create new entry for table
            RowTable rowTable = new RowTable();

            // get the current state
            State state = canonicalCollection.getStates().get(i);

            // set the state index
            rowTable.stateIndex = i;
            // set the action of the state
            rowTable.action = state.stateType;

            rowTable.shifts = new ArrayList<>();

            // if there are conflicts the algorithm is stopped => it is not LR(0)
            if (state.stateType == StateType.SHIFT_REDUCE_CONFLICT || state.stateType == StateType.REDUCE_REDUCE_CONFLICT) {
                for (Map.Entry<Pair<Integer, String>, Integer> e2 : canonicalCollection.getAdjacencyList().entrySet()) {
                    Pair<Integer, String> k2 = e2.getKey();
                    Integer v2 = e2.getValue();

                    if (v2.equals(rowTable.stateIndex)) {
                        System.out.println("state index: " + rowTable.stateIndex);
                        System.out.println("symbol " + k2.getValue());
                        System.out.println("( " + k2.getValue() + ", " + k2.getKey() + " )" + " -> " + rowTable.stateIndex);
                        System.out.println("state " + state);

                        break;
                    }
                }
                parsingTable.entries = new ArrayList<>();
                return parsingTable;
            } else if (state.stateType == StateType.ACCEPT) {
                rowTable.shifts = null;
            } else if (state.stateType == StateType.REDUCE) {
                Item item = state.items.stream().filter(it -> it.dotPosition == it.rhs.size()).findAny().orElse(null);
                if (item != null) {
                    rowTable.shifts = null;
                    rowTable.reduceNonTerminal = item.lhs;
                    rowTable.reduceContent = item.rhs;
                }
            } else { // shift
                List<Pair<String, Integer>> goTos = new ArrayList<>();
                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getAdjacencyList().entrySet()) {
                    Pair<Integer, String> key = entry.getKey();
                    if (key.getKey() == rowTable.stateIndex) {
                        goTos.add(new Pair<>(key.getValue(), entry.getValue()));
                    }
                }
                rowTable.shifts = goTos;
            }
            parsingTable.entries.add(rowTable);
        }
        return parsingTable;
    }

    public void parse(Stack<String> inputStack, ParsingTable parsingTable, String filePath) throws IOException {
        Stack<Pair<String, Integer>> workStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        Stack<Integer> outputBand = new Stack<>();

        String lastSymbol = "";
        int stateIndex = 0;

        boolean sem = true;

        workStack.push(new Pair<>(lastSymbol, stateIndex));
        RowTable lastRow = null;
        String onErrorSymbol = null;

        try {
            do {
                System.out.println("input stack: " + inputStack);
                System.out.println("current state index: " + stateIndex);
                if (!inputStack.isEmpty()) {
                    // We keep the symbol before which an error might occur
                    onErrorSymbol = inputStack.peek();
                }
                // We update the last row from the table we worked with
                lastRow = parsingTable.entries.get(stateIndex);

                RowTable entry = parsingTable.entries.get(stateIndex);

                if (entry.action.equals(StateType.SHIFT)) {
                    // If the action is shift, we pop from the input stack
                    // We look at the last added state from the working stack
                    // Look into the parsing table at that state, and find out
                    // From it through what state, we can obtain the symbol popped from the input stack
                    String symbol = inputStack.pop();
                    System.out.println("shifts: " + entry.shifts);
                    Pair<String, Integer> state = entry.shifts.stream().filter(it -> it.getKey().equals(symbol)).findAny().orElse(null);
                    System.out.println("symbol: " + symbol + " having state: " + state);
                    if (state != null) {
                        stateIndex = state.getValue();
                        lastSymbol = symbol;
                        workStack.push(new Pair<>(lastSymbol, stateIndex));
                    } else {
                        throw new NullPointerException();
                    }
                } else if (entry.action.equals(StateType.REDUCE)) {
                    List<String> reduceContent = new ArrayList<>(entry.reduceContent);
                    System.out.println("im reducing");
                    while (reduceContent.contains(workStack.peek().getKey()) && !workStack.isEmpty()) {
                        System.out.println("in while");
                        reduceContent.remove(workStack.peek().getKey());
                        workStack.pop();
                    }

                    // We look into the row of the last state from the working stack
                    // We look through the shift values and look for the one that corresponds to the reduceNonTerminal
                    // Basically, we look through which state, from the current one, we can obtain that non-terminal
                    Pair<String, Integer> state = parsingTable.entries.get(workStack.peek().getValue()).shifts.stream()
                            .filter(it -> it.getKey().equals(entry.reduceNonTerminal)).findAny().orElse(null);

                    assert state != null;
                    stateIndex = state.getValue();
                    lastSymbol = entry.reduceNonTerminal;
                    workStack.push(new Pair<>(lastSymbol, stateIndex));

                    outputStack.push(entry.reduceProductionString());

                    // We "form" the production used for reduction and look for its production number
                    var index = new Pair<>(entry.reduceNonTerminal, entry.reduceContent);
                    int productionNumber = this.orderedProductions.indexOf(index);

                    outputBand.push(productionNumber);
                } else {
                    if (entry.action.equals(StateType.ACCEPT)) {
                        List<String> output = new ArrayList<>(outputStack);
                        Collections.reverse(output);
                        List<Integer> numberOutput = new ArrayList<>(outputBand);
                        Collections.reverse(numberOutput);

                        System.out.println("ACCEPTED");
                        writeToFile(filePath, "ACCEPTED");
                        System.out.println("Production strings: " + output);
                        writeToFile(filePath, "Production strings: " + output);
                        System.out.println("Production number: " + numberOutput);
                        writeToFile(filePath, "Production number: " + numberOutput);

                        ParsingTree parsingTree = new ParsingTree(grammar);
                        parsingTree.generateTreeFromSequence(numberOutput);
                        System.out.println("The output tree: ");
                        writeToFile(filePath, "The output tree: ");
                        parsingTree.printTree(parsingTree.getRoot(), filePath);


                        sem = false;
                    }

                }
            } while (sem);
        } catch (NullPointerException ex) {
            System.out.println("ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            System.out.println(lastRow);

            writeToFile(filePath, "ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            writeToFile(filePath, lastRow.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }
}


