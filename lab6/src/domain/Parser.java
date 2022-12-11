package domain;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Grammar grammar;

    private List<Pair<String, List<String>>> productionsWithDot;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        productionsWithDot = new ArrayList<>();
    }

    public List<Pair<String, List<String>>> getProductionsWithDot() {
        return productionsWithDot;
    }

    public void canonicalCollection() {
        productionsWithDot.add(new Pair<>("S'", List.of("." + grammar.getStartSymbol())));
        var state0 = this.closure(productionsWithDot);

    }

    private void gotoLR() {

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

                    List<String> productionOfStartSymbol = grammar.productionForNonTerminal(production.getValue().get(0).substring(index + 1, index + 2));
                    if (!productionOfStartSymbol.isEmpty()) {
                    var prodSet = productionOfStartSymbol.get(0).split("\\|");

                        for (var production2 : prodSet) {
                            var value = new Pair<>(grammar.getStartSymbol(), List.of("." + production2));
                            boolean contain = existsInList(newList, value);
                            if (!contain) {
                                System.out.println("here");
                                newList.add(value);
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
}


