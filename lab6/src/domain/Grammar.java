package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private final List<String> nonterminals;
    private final List<String> terminals;
    private final ProductionSet productions;
    private final String fileName;
    private String startSymbol;

    public Grammar(String fileName) {
        this.fileName = fileName;
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new ProductionSet();
        readFromFile(fileName);
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public List<String> getNonterminals() {
        return nonterminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public ProductionSet getProductions() {
        return productions;
    }

    public void readFromFile(String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            String[] nonterminalsStringArray = scanner.nextLine().split(",");
            nonterminals.addAll(Arrays.asList(nonterminalsStringArray));

            String[] terminalsStringArray = scanner.nextLine().split(",");
            terminals.addAll(Arrays.asList(terminalsStringArray));

            startSymbol = scanner.nextLine();

            while (scanner.hasNextLine()) {
                var production = scanner.nextLine().split("->");
                var lhs = Arrays.stream(production[0].trim().split(" ")).collect(Collectors.toList());
                var rhs = Arrays.stream(production[1].trim().split(" ")).collect(Collectors.toList());
                productions.addProduction(lhs, rhs);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> productionForNonTerminal(String nonTerminal) {
        return productions.getProductionsOf(nonTerminal);
    }

//    public String displayProdForNonTerm(String nonTerminal) {
//        StringBuilder sb = new StringBuilder();
//        List<String> prod = this.productionForNonTerminal(nonTerminal);
//        for (var prodSet : prod) {
//            sb.append(nonTerminal).append("->");
//            sb.append(prodSet);
//            sb.append("\n");
//        }
//        return sb.toString();
//    }

    private List<String> stringToList(String string) {
        return List.of(string.split(" "));
    }

    public boolean checkIfCFG() {
        // check if start symbol appears in lhs of productions
        boolean startSymbolExists = false;
        for (var pair : productions) {
            if (pair.getKey().equals(startSymbol)) {
                startSymbolExists = true;
                break;
            }
        }
        if (!startSymbolExists)
            return false;

        // check if all lhs of productions are of length 1 and appear in nonterminals
        for (var pair : productions) {
            // string that contains more than one word not ok
            if (pair.getKey().contains(" "))
                return false;
            if (!nonterminals.contains(pair.getKey()))
                return false;
            // check if all rhs of productions appear in set of nonterminals or set of terminals
            for (var rhs : pair.getValue()) {
                List<String> rhsAsList = stringToList(rhs);
                for (String character : rhsAsList) {
                    if (!nonterminals.contains(character) && !terminals.contains(character))
                        return false;
                }
            }
        }
        return true;
    }
}
