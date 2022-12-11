package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Grammar {
    private final List<String> nonterminals;
    private final List<String> terminals;
    private final List<Pair<String, List<String>>> productions;
    private final String fileName;
    private String startSymbol;

    public Grammar(String fileName) {
        this.fileName = fileName;
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new ArrayList<>();
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

    public List<Pair<String, List<String>>> getProductions() {
        return productions;
    }

    public String displayProductions() {
        StringBuilder sb = new StringBuilder();
        for (var production : productions) {
            sb.append(production.getKey()).append("->");
            for (var prodSet : production.getValue()) {
                sb.append(prodSet).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        return sb.toString();
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
                String[] productionPair = scanner.nextLine().split("->");
                List<String> prodSet;
                prodSet = Arrays.asList(productionPair[1].split("\\|"));
                productions.add(new Pair<>(productionPair[0], prodSet));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> productionForNonTerminal(String nonTerminal) {
        List<String> productionsForNonTerminal = new ArrayList<>();
        for (var production : productions) {
            if (production.getKey().equals(nonTerminal)) {
                StringBuilder sb = new StringBuilder();
                for (var prod : production.getValue()) {
                    sb.append(prod).append("|");
                }
                sb.deleteCharAt(sb.length() - 1);
                productionsForNonTerminal.add(sb.toString());
            }
        }

        return productionsForNonTerminal;
    }

    public String displayProdForNonTerm(String nonTerminal) {
        StringBuilder sb = new StringBuilder();
        List<String> prod = this.productionForNonTerminal(nonTerminal);
        for (var prodSet : prod) {
            sb.append(nonTerminal).append("->");
            sb.append(prodSet);
            sb.append("\n");
        }
        return sb.toString();
    }

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
