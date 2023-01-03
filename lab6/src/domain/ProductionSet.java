package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductionSet {
    private final HashMap<List<String>, List<List<String>>> productions;

    ProductionSet() {
        productions = new HashMap<>();
    }

    ProductionSet(HashMap<List<String>, List<List<String>>> productions) {
        this.productions = productions;
    }

    public List<List<String>> getProductionsOf(List<String> lhs) {
        return productions.getOrDefault(lhs, Collections.emptyList());
    }

    public List<List<String>> getProductionsOf(String lhs) {
        return getProductionsOf(List.of(lhs));
    }

    public HashMap<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    public void addProduction(List<String> lhs, List<String> rhs) {
        if (!productions.containsKey(lhs))
            productions.put(lhs, new ArrayList<>());
        productions.get(lhs).add(rhs);
    }

    public void addProductionIfAbsent(List<String> lhs) {
        productions.putIfAbsent(lhs, new ArrayList<>());
    }

    public ProductionSet copy() {
        return new ProductionSet(productions);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var lhs : productions.keySet()) {
            for (var rhs : productions.get(lhs)) {
                sb.append(lhs).append(" -> ").append(rhs).append("\n");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
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