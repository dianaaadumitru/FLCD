package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private final Grammar grammar;

    private final List<List<Pair<String, List<String>>>> canonicalCollection;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        canonicalCollection = new ArrayList<>();
    }

//    private List<String> getAllSymbols() {
//        return Stream.concat(grammar.getNonterminals().stream(), grammar.getTerminals().stream()).collect(Collectors.toList());
//    }
//
//    public void canonicalCollection() {
//        var state0 = this.closure(List.of(new Pair<>("S'", List.of("." + grammar.getStartSymbol()))));
//        canonicalCollection.add(state0);
//        boolean done = false;
//        int indexState = 0;
//        int startParsing = -1;
//        while (!done) {
//            startParsing++;
//            var canonicalCollectionCopy = new ArrayList<>(canonicalCollection);
//            for (int i = startParsing; i < canonicalCollectionCopy.size(); i++) {
//                for (var state : canonicalCollectionCopy.get(i)) {
//                    for (var symbol : getAllSymbols()) {
//                        var result = gotoLR(state, symbol);
//                        if (!result.isEmpty()) {
//
//                            indexState++;
//                            System.out.println("s" + indexState + " = goto(" + symbol + ") = " + result);
//                            if (!existsInCanonicalCollection(canonicalCollectionCopy, result)) {
//                                canonicalCollection.add(result);
//                            }
//
//                        }
//                    }
//                }
//            }
//            if (canonicalCollectionCopy.equals(canonicalCollection)) {
//                done = true;
//            }
//        }
//    }
//
//    /**
//     * goto(s, X) = closure({[A → αX.β]|[A → α.Xβ] ∈ s})
//     *
//     * @param state  current state
//     * @param symbol terminal or nonterminal
//     * @return a closure
//     */
//    private List<Pair<String, List<String>>> gotoLR(Pair<String, List<String>> state, String symbol) {
//        List<Pair<String, List<String>>> items = new ArrayList<>();
//        // find the dot position
//        int index = state.getValue().get(0).indexOf('.');
//
//        // check if dot is not at end
//        if (index < state.getValue().get(0).length() - 1) {
//            if (state.getValue().get(0).substring(index + 1, index + 2).equals(symbol)) {
//                //a.Sb => aS.b .abS => a.bS
//                String newItem;
//                if (index != 0) {
//                    newItem = state.getValue().get(0).substring(0, index) + state.getValue().get(0).charAt(index + 1) + "." + state.getValue().get(0).substring(index + 2);
//                } else {
//                    newItem = state.getValue().get(0).charAt(1) + "." + state.getValue().get(0).substring(2);
//                }
//                items.add(new Pair<>(state.getKey(), List.of(newItem)));
//            }
//
//        }
//        return closure(items);
//    }
//
//
//    /**
//     * I-state
//     * repeat
//     * for any [A -> α.Bβ] in C do
//     * for any B -> γ in P do
//     * if [B -> .γ] 2/ C then
//     * C = C U [B -> .γ]
//     * end if
//     * end for
//     * end for
//     * until C stops changing
//     * :return: C = closure(I);
//     */
//    public List<Pair<String, List<String>>> closure(List<Pair<String, List<String>>> closureList) {
//        boolean notDone = true;
//        var newList = new ArrayList<>(closureList);
//        var oldList = new ArrayList<>(closureList);
//        while (notDone) {
//            for (var production : closureList) {
//                // find the dot position
//                int index = production.getValue().get(0).indexOf('.');
//
//                // check if dot is not at end
//                if (index < production.getValue().get(0).length() - 1) {
//                    List<String> productionOfStartSymbol;
//                    String nextSymbol;
//                    if (production.getValue().get(0).charAt(index + 1) == ' ') {
//                        nextSymbol = production.getValue().get(0).substring(index + 2, index + 3);
//                    } else {
//                        nextSymbol = production.getValue().get(0).substring(index + 1, index + 2);
//                    }
//                    productionOfStartSymbol = grammar.productionForNonTerminal(nextSymbol);
//                    if (!productionOfStartSymbol.isEmpty()) {
//
//
//                        for (var production2 : productionOfStartSymbol) {
//                            var prodSet = production2.split("\\|");
//                            for (var production3 : prodSet) {
//                                String productionWithoutSpaces = removeSpaceFromProduction(production3).strip();
//                                var value = new Pair<>(nextSymbol, List.of("." + productionWithoutSpaces));
//                                boolean contain = existsInList(newList, value);
//                                if (!contain) {
//                                    newList.add(value);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (newList.equals(oldList)) {
//                notDone = false;
//            }
//
//            oldList = new ArrayList<>(newList);
//        }
//
//        return newList;
//    }
//
//    private boolean existsInList(List<Pair<String, List<String>>> givenList, Pair<String, List<String>> value) {
//        for (var elem : givenList) {
//            if (elem.getKey().equals(value.getKey()) && elem.getValue().equals(value.getValue())) return true;
//        }
//        return false;
//    }
//
//    private boolean existsInCanonicalCollection(List<List<Pair<String, List<String>>>> canonicalCollection, List<Pair<String, List<String>>> result) {
//        boolean ok = false;
//        for (var elem : canonicalCollection) {
//            int noOfEqualElems = 0;
//            for (var sym : result) {
//                if (existsInList(elem, sym)) {
//                    noOfEqualElems++;
//                }
//            }
//            if (noOfEqualElems == result.size()) {
//                ok = true;
//                break;
//            }
//        }
//        return ok;
//    }
//
//    private String removeSpaceFromProduction(String production) {
//        return production.replace(" ", "");
//    }


    public List<Pair<String, List<String>>> closureLR(List<Pair<String, List<String>>> input) {
        //exemplu S -> .A B
        Map<List<String>, List<String>> P = new HashMap<>();
        List<String> lineListT = Arrays.asList(input.split("->"));
        List<String> lineList =
                lineListT.stream().map(String::trim).collect(Collectors.toList());

        //lineList : [S, .A B]
        List<String> key = Arrays.asList(lineList.get(0).strip().split(" \\| "));
        List<String> value = new ArrayList<>();
        String[] token = lineList.get(1).split("\\|");
        //split la lista dupa |
        for (var str : token) {
            value.add(str.strip());
        }
        P.put(key, value);
        int size = 0, index;
        String nonT;
        //pana aici o sa fie {[S]:[.A B]}
        while (size < P.size()) {
            size = P.size();
            Map<List<String>, List<String>> filteredP = new HashMap<>(P);
            for (Map.Entry element : filteredP.entrySet()) {
                value = (List<String>) element.getValue();
                // value o sa fie o lista de strings [.A B]
                for (String s : value) {
                    index = s.indexOf('.');
                    //gasim indexul punctului
                    if (index != -1 && index < s.length() - 1) {
                        //System.out.println(s);// daca exista punct, si nu e pe ultima pozitie
                        nonT = s.substring(index + 1).split(" ")[0]; // eliminam punctul
                        //System.out.println(nonT);
                        // o sa fie primul element acuma nonT
                        Map<List<String>, List<String>> filteredB = grammar.filterP(nonT);
                        //System.out.println(filteredB);
                        for (Map.Entry elementB : filteredB.entrySet()) {
                            List<String> keyB = (List<String>) elementB.getKey();
                            List<String> valueB = (List<String>) elementB.getValue();
                            if (!P.containsKey(keyB)) {
                                P.put(keyB, valueB.stream().map(x -> "." + x).collect(Collectors.toList()));
                            }
                        }
                    }
                }
            }
        }
        return P;
    }
}


