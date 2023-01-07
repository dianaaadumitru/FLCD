package parser_LR0;

import state.State;
import utils.Pair;

import java.util.*;

public class CanonicalCollection {
    public final List<State> states;
    private Map<Pair<Integer, String>, Integer> adjacencyList;


    public CanonicalCollection() {
        this.states = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public CanonicalCollection(List<State> states, Map<Pair<Integer, String>, Integer> adjacencyList) {
        this.states = states;
        this.adjacencyList = adjacencyList;
    }

    public void addState(State state) {
        this.states.add(states.size(), state);
    }

    public void connectStates(Integer indexFirstState, String symbol, Integer indexSecondState) {
        this.adjacencyList.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }

    public List<State> getStates() {
        return this.states;
    }

    public Map<Pair<Integer, String>, Integer> getAdjacencyList() {
        return this.adjacencyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanonicalCollection that = (CanonicalCollection) o;
        return Objects.equals(states, that.states) && Objects.equals(adjacencyList, that.adjacencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(states, adjacencyList);
    }
}