package parser_LR0;

import utils.Pair;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    public static final String enrichedGrammarStartingSymbol = "S0";
    private final List<String> nonterminals;
    private final List<String> terminals;
    boolean isEnriched;
    private String startSymbol;
    private final Map<List<String>, List<List<String>>> productions;

    public Grammar(String fileName) {
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        readFromFile(fileName);
    }

    public Grammar(List<String> nonTerminals, List<String> terminals, String startingSymbol, Map<List<String>, List<List<String>>> productions) {
        this.nonterminals = nonTerminals;
        this.terminals = terminals;
        this.startSymbol = startingSymbol;
        this.productions = productions;
        this.isEnriched = true;
    }

    public boolean isEnriched() {
        return isEnriched;
    }

    public Map<List<String>, List<List<String>>> getProductions() {
        return productions;
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
                if (!productions.containsKey(lhs)) {
                    productions.put(lhs, new ArrayList<>());
                }
                productions.get(lhs).add(rhs);
            }

            this.isEnriched = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkCFG() {
        // check if start symbol appears in lhs of productions
        boolean startSymbolExists = false;
        for (var key : productions.keySet()) {
            for (var symbol : key) {
                if (symbol.equals(startSymbol)) {
                    startSymbolExists = true;
                    break;
                }
            }
        }
        if (!startSymbolExists)
            return false;

        // key that contains more than one word not ok
        for (var key : productions.keySet()) {
            if (key.size() != 1) {
                return false;
            }
            for (var symbol : key) {
                if (!nonterminals.contains(symbol)) {
                    return false;
                }
            }
        }
        // check if all rhs of productions appear in set of nonterminals or set of terminals
        for (var values : productions.values()) {
            for (var value : values) {
                for (var symbol : value) {
                    if (!nonterminals.contains(symbol) && !terminals.contains(symbol))
                        return false;
                }
            }
        }
        return true;
    }

    public Grammar getEnrichedGrammar() {
        if (isEnriched) {
            return this;
        }

        Grammar enrichedGrammar = new Grammar(nonterminals, terminals, enrichedGrammarStartingSymbol, productions);


        enrichedGrammar.nonterminals.add(enrichedGrammarStartingSymbol);
        enrichedGrammar.productions.putIfAbsent(List.of(enrichedGrammarStartingSymbol), new ArrayList<>());
        enrichedGrammar.productions.get(List.of(enrichedGrammarStartingSymbol)).add(List.of(startSymbol));
        return enrichedGrammar;
    }

    public List<List<String>> getProductionsForNonTerminal(String nonTerminal) {
        return productions.get(List.of(nonTerminal));
    }

    public List<Pair<String, List<String>>> getOrderedProductions() {
        List<Pair<String, List<String>>> orderedProductions = new ArrayList<>();
        this.productions.forEach(
                (lhs, rhs) -> rhs.forEach(
                        (prod) -> orderedProductions.add(new Pair<>(lhs.get(0), prod))
                )
        );
        return orderedProductions;
    }
}
