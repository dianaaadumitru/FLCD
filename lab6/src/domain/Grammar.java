package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private final List<String> nonterminals;
    private final List<String> terminals;
    private ProductionSet productions;
    private String fileName;
    private String startSymbol;
    private Map<List<String>, List<List<String>>> productions2;

    public static final String enrichedGrammarStartingSymbol = "S0";
    boolean isEnriched;

    public Grammar(String fileName) {
        this.fileName = fileName;
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new ProductionSet();
        this.productions2 = new HashMap<>();
        readFromFile(fileName);
    }

    public Grammar(List<String> nonterminals, List<String> terminals, String startSymbol, ProductionSet productions) {
        this.nonterminals = nonterminals;
        this.terminals = terminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
        this.isEnriched = true;
    }

    public Grammar(List<String> nonTerminals, List<String> terminals, String startingSymbol, Map<List<String>, List<List<String>>> productions) {
        this.nonterminals = nonTerminals;
        this.terminals = terminals;
        this.startSymbol = startingSymbol;
        this.productions2 = productions;
        this.isEnriched = true;
    }

    public boolean isEnriched() {
        return isEnriched;
    }

    public Map<List<String>, List<List<String>>> getProductions2() {
        return productions2;
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
                if (!productions2.containsKey(lhs)) {
                    productions2.put(lhs, new ArrayList<>());
                }
                productions2.get(lhs).add(rhs);
            }

            this.isEnriched = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> productionForNonTerminal(String nonTerminal) {
        return productions.getProductionsOf(nonTerminal);
    }

    public boolean checkCFG() {
        // check if start symbol appears in lhs of productions
        boolean startSymbolExists = false;
        for (var key : productions.getProductions().keySet()) {
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
        boolean contains = false;
        for (var key : productions.getProductions().keySet()) {
            if (key.size() != 1) {
                return false;
            }
            for (var symbol: key) {
                if (!nonterminals.contains(symbol)) {
                    return false;
                }
            }
        }
        // check if all rhs of productions appear in set of nonterminals or set of terminals
        for (var values : productions.getProductions().values()) {
            for (var value : values) {
                for (var symbol: value) {
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
//        var newNonTerminals = new ArrayList<>(nonterminals);
//        newNonTerminals.add(enrichedGrammarStartingSymbol);
//        Grammar newGrammar = new Grammar(newNonTerminals, terminals, enrichedGrammarStartingSymbol, productions.copy());
//        newGrammar.productions.addProduction(List.of(enrichedGrammarStartingSymbol), Collections.singletonList(startSymbol));
//        return newGrammar;

//        Grammar enrichedGrammar = new Grammar(nonterminals, terminals, enrichedGrammarStartingSymbol, productions);
//        enrichedGrammar.nonterminals.add(enrichedGrammarStartingSymbol);
//        enrichedGrammar.productions.addProductionIfAbsent(List.of(enrichedGrammarStartingSymbol));
//        enrichedGrammar.productions.getProductionsOf(List.of(enrichedGrammarStartingSymbol)).add(List.of(startSymbol));

//        Grammar enrichedGrammar = new Grammar(nonterminals, terminals, enrichedGrammarStartingSymbol, productions);

        Grammar enrichedGrammar = new Grammar(nonterminals, terminals, enrichedGrammarStartingSymbol, productions2);


        enrichedGrammar.nonterminals.add(enrichedGrammarStartingSymbol);
        enrichedGrammar.productions2.putIfAbsent(List.of(enrichedGrammarStartingSymbol), new ArrayList<>());
        enrichedGrammar.productions2.get(List.of(enrichedGrammarStartingSymbol)).add(List.of(startSymbol));
        return enrichedGrammar;
    }

    public List<List<String>> getProductionsForNonTerminal(String nonTerminal) {
        return productions2.get(List.of(nonTerminal));
    }
}
