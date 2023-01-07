package state;

import parser_LR0.Grammar;

import java.util.*;

public class State {
    public final Set<Item> items;
    public StateType stateType;

    public State(Set<Item> items) {
        this.items = items;

        this.stateType = (items.size() == 1 && items.iterator().next().rhs.size() == items.iterator().next().dotPosition && items.iterator().next().lhs.equals(Grammar.enrichedGrammarStartingSymbol))
                ? StateType.ACCEPT
                : (items.size() == 1 && items.iterator().next().rhs.size() == items.iterator().next().dotPosition)
                ? StateType.REDUCE
                : (items.size() > 0 && items.stream().allMatch(it -> it.rhs.size() > it.dotPosition))
                ? StateType.SHIFT
                : (items.size() > 1 && items.stream().allMatch(it -> it.rhs.size() == it.dotPosition))
                ? StateType.REDUCE_REDUCE_CONFLICT
                : StateType.SHIFT_REDUCE_CONFLICT;
    }

    public List<String> getSymbolsSucceedingTheDot() {
        Set<String> symbols = new HashSet<>();
        for (Item item : items) {
            if (item.dotPosition >= 0 && item.dotPosition < item.rhs.size()) {
                symbols.add(item.rhs.get(item.dotPosition));
            }
        }
        return new ArrayList<>(symbols);
    }

    @Override
    public String toString() {
        return String.format("%s", items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(items, state.items) && stateType == state.stateType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, stateType);
    }
}