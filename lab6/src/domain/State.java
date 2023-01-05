package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State {
    public final Set<Item> items;

    public State(Set<Item> items) {
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
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
}