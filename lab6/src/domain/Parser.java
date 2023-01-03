package domain;

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

        orderedProductions = this.grammar.getProductions().getOrderedProductions();
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

        } while (!oldClosure.equals(currentClosure));

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
//        canonicalCollection.addState(
//                closure(
//                        new Item(
//                                workingGrammar.getStartSymbol(),
//                                workingGrammar.getProductions().getProductionsOf(workingGrammar.getStartSymbol()).get(0),
//                                0)
//                )
//        );

        canonicalCollection.addState(
                closure(
                        new Item(
                                workingGrammar.getStartSymbol(),
                                workingGrammar.getProductionsForNonTerminal(workingGrammar.getStartSymbol()).get(0),
                                0)
                )
        );

        int i = 0;
        while (i < canonicalCollection.states.size()) {
            for (String symbol : canonicalCollection.states.get(i).getSymbolsSucceedingTheDot()) {
                State newState = goTo(canonicalCollection.states.get(i), symbol);

                if (newState.getItems().size() != 0) {
                    int indexInStates = canonicalCollection.states.indexOf(newState);
                    if (indexInStates == -1) {
                        canonicalCollection.addState(newState);
                    }
                }
            }
            i++;
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
}


