package parser_LR0;

import parsingTable.ParsingTable;
import parsingTable.RowTable;
import state.Item;
import state.StateType;
import utils.Pair;
import state.State;

import java.util.*;

public class Parser {
    private final Grammar grammar;
    private final Grammar workingGrammar;

    private final Set<Item> allItems;

    private final List<Pair<String, List<String>>> orderedProductions;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.allItems = new HashSet<>();

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
                if (nonTerminal != null) {
                    for (List<String> prod : grammar.getProductionsForNonTerminal(nonTerminal)) {
                        Item currentItem = new Item(nonTerminal, prod, 0);
                        if (!containsItem(allItems, currentItem)) {
                            newClosure.add(currentItem);
                            allItems.add(currentItem);
                        }
                    }
                }
            }
            currentClosure = newClosure;

        } while (!currentClosure.equals(oldClosure));

        return new State(currentClosure);
    }

    public State goTo(State state, String element) {
        Set<Item> result = new LinkedHashSet<>();
        for (Item item : state.items) {
            String nonTerminal = item.rhs.get(item.dotPosition);
            if (Objects.equals(nonTerminal, element)) {
                Item nextItem = new Item(item.lhs, item.rhs, item.dotPosition + 1);
                result.addAll(closure(nextItem).items);
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

    private boolean containsItem(Set<Item> closureList, Item item) {
        for (Item it : closureList) {
            if (it.equals(item))
                return true;
        }
        return false;
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
                    if(key.getKey() == rowTable.stateIndex){
                        goTos.add(new Pair<>(key.getValue(), entry.getValue()));
                    }
                }
                rowTable.shifts = goTos;
            }
            parsingTable.entries.add(rowTable);
        }
        return parsingTable;
    }
}


