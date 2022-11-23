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

    private String startSymbol;
    private final List<Pair<String, List>> productions;

    private final String fileName;

    public Grammar(String fileName) {
        this.fileName = fileName;
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new ArrayList<>();
        readFromFile(fileName);
    }

    public List<String> getNonterminals() {
        return nonterminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<Pair<String, List>> getProductions() {
        return productions;
    }

    public String displayProductions() {
        StringBuilder sb = new StringBuilder();
        for (var production: productions) {
            sb.append(production.getKey()).append("->");
            for (var prodSet: production.getValue()) {
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
                prodSet = Arrays.asList(productionPair[1].split("/"));
                productions.add(new Pair<>(productionPair[0], prodSet));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
