package domain;

import java.util.ArrayList;
import java.util.Arrays;
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
        productionsWithDot.add(new Pair<>("S'", Arrays.asList("." + grammar.getStartSymbol())));


        closure(productionsWithDot);
    }

    private void gotoLR() {

    }


    private void closure(List<Pair<String, List<String>>> closureList)
    /*
    I-state
                repeat
                for any [A -> α.Bβ] in C do
                for any B -> γ in P do
                if [B -> .γ] 2/ C then
        C = C U [B -> .γ]
        end if
        end for
        end for
        until C stops changing
            :return: C = closure(I);
     */ {
        boolean notDone = true;
        var copy = new ArrayList<>(productionsWithDot);
        while (notDone) {
            for (var production : closureList) {
                if (production.getValue().get(0).charAt(0) == '.') {

                    List<String> productionOfStartSymbol = grammar.productionForNonTerminal(grammar.getStartSymbol());

                    for (var production2 : productionOfStartSymbol) {
                        copy.add(new Pair<>(grammar.getStartSymbol(), Arrays.asList("." + production2)));
                    }

                    notDone = false;
                }
            }
        }
        productionsWithDot = new ArrayList<>(copy);
    }
}

//
//    Map<List<String>, List<List<String>>> P = new HashMap<>();
//    List<String> lineListT = Arrays.asList(input.split("->"));
//    List<String> lineList =
//            lineListT.stream().map(String::trim).collect(Collectors.toList());
//    //lineList : [S, .A B]
//    List<String> key = Arrays.asList(lineList.get(0).strip().split(" \\| "));
//    List<List<String>> value = new ArrayList<>();
//    List<String> token = List.of(lineList.get(1).split("\\|"));
//        for(var str:token){
//                List<String> prod = Arrays.asList(str.strip().split(" "));
//        value.add(prod);
//        }
//        //split la lista dupa |
//        P.put(key, value);
//        int size = 0;
//        int index = -1;
//        String nonT;
//        pana aici o sa fie {[S]:[[.A, B]}
//pana aici e ok

