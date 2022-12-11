package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private Grammar grammar;

    private List<Pair<String, List<String>>> productionsWithDot;

    private List<List<Pair<String, List<String>>>> canonicalCollection;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        productionsWithDot = new ArrayList<>();
        canonicalCollection = new ArrayList<>();
    }

    public List<Pair<String, List<String>>> getProductionsWithDot() {
        return productionsWithDot;
    }

    private List<String> getAllSymbols()
    {
        return Stream.concat(grammar.getNonterminals().stream(), grammar.getTerminals().stream()).collect(Collectors.toList());
    }
    public void canonicalCollection() {
        System.out.println("closure:");
        System.out.println(closure(List.of(new Pair<>("S", List.of("a. A")))));
        System.out.println(grammar.productionForNonTerminal("A"));
        productionsWithDot.add(new Pair<>("S'", List.of("." + grammar.getStartSymbol())));
        var state0 = this.closure(productionsWithDot);
        canonicalCollection.add(state0);
        boolean done = false;
        while(!done)
        {
            var canonicalCollectionCopy = new ArrayList<>(canonicalCollection);
            for(var state: canonicalCollectionCopy)
            {
                for(var symbol: getAllSymbols())
                {
                    var result = gotoLR(state, symbol);
                    System.out.println(result);
                    if(!result.isEmpty())
                    {
                        if(!existsInCanonicalCollection(canonicalCollectionCopy,result))
                        {
                            canonicalCollection.add(result);
                        }

                    }
                }
            }
            if(canonicalCollectionCopy.equals(canonicalCollection))
            {
                done = true;
            }
        }
        //System.out.println(gotoLR(state0, "c"));;

    }

    /**
     * goto(s, X) = closure({[A → αX.β]|[A → α.Xβ] ∈ s})
     * @param state
     * @param symbol all symbols terminals and nonterminals
     * @return a closure
     */
    private List<Pair<String, List<String>>> gotoLR(List<Pair<String, List<String>>> state, String symbol) {
        List<Pair<String, List<String>>> items = new ArrayList<>();
        for (var currentState: state) {
                // find the dot position
                int index = currentState.getValue().get(0).indexOf('.');

                // check if dot is not at end
                if (index < currentState.getValue().get(0).length() - 1) {
                    if (currentState.getValue().get(0).substring(index + 1, index + 2).equals(symbol)) {
                        //a.Sb => aS.b
                        String newItem;
                        if (index != 0){
                            newItem = currentState.getValue().get(0).substring(0, index - 1) + currentState.getValue().get(0).substring(index + 1, index + 2) + "." + currentState.getValue().get(0).substring(index + 2);
                        } else {
                            newItem = currentState.getValue().get(0).substring(1, 2) + "." + currentState.getValue().get(0).substring(2);
                        }
                        items.add(new Pair<>(currentState.getKey(), List.of(newItem)));
                    }

                }
        }
        return closure(items);
    }


    /**
     * I-state
     * repeat
     *      for any [A -> α.Bβ] in C do
     *          for any B -> γ in P do
     *              if [B -> .γ] 2/ C then
     *                  C = C U [B -> .γ]
     *              end if
     *          end for
     *      end for
     * until C stops changing
     * :return: C = closure(I);
     */
    public List<Pair<String, List<String>>> closure(List<Pair<String, List<String>>> closureList) {
        boolean notDone = true;
        var newList = new ArrayList<>(closureList);
        var oldList = new ArrayList<>(closureList);
        while (notDone) {
            for (var production : closureList) {
                // find the dot position
                int index = production.getValue().get(0).indexOf('.');

                // check if dot is not at end
                if (index < production.getValue().get(0).length() - 1) {
                    List<String> productionOfStartSymbol = new ArrayList<>();
                    if(production.getValue().get(0).substring(index + 1, index + 2).equals(" ")){
                        productionOfStartSymbol = grammar.productionForNonTerminal(production.getValue().get(0).substring(index + 2, index + 3));
                    }
                    else
                    {
                    productionOfStartSymbol = grammar.productionForNonTerminal(production.getValue().get(0).substring(index + 1, index + 2));
                    }
                    if (!productionOfStartSymbol.isEmpty()) {


                        for (var production2 : productionOfStartSymbol) {
                            var prodSet = production2.split("\\|");
                            for(var production3: prodSet) {
                                var value = new Pair<>(grammar.getStartSymbol(), List.of("." + production3));
                                boolean contain = existsInList(newList, value);
                                if (!contain) {
                                    newList.add(value);
                                }
                            }
                        }
                    }
                }
            }
            if (newList.equals(oldList)) {
                notDone = false;
            }

            oldList = new ArrayList<>(newList);
        }

        return newList;
    }

    private boolean existsInList(List<Pair<String, List<String>>> givenList, Pair<String, List<String>> value) {
        for (var elem: givenList) {
            if (elem.getKey().equals(value.getKey()) && elem.getValue().equals(value.getValue()))
                return true;
        }
        return false;
    }

    private boolean existsInCanonicalCollection(List<List<Pair<String, List<String>>>> canonicalCollection, List<Pair<String, List<String>>> result)
    {
        for(var elem: canonicalCollection)
        {
            for(var sym: result) {
                if (existsInList(elem, sym)) {
                    return true;
                }
            }
        }
        return false;
    }
}


