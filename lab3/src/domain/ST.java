package domain;

public class ST<T> {

    private final HashTable<T> items;

    public ST(HashTable<T> items) {
        this.items = items;
    }

    public HashTable<T> getItems() {
        return items;
    }

    public Pair<Integer, Integer> addToST(T key) {
        return items.addToHT(key);
    }

    public Pair<Integer, Integer> getPosition(T key) {
        return items.getPosition(key);
    }

    public boolean removeItem(T key) {
        return items.removeItem(key);
    }

    @Override
    public String toString() {
        return "domain.ST{" +
                "items=" + items +
                '}';
    }
}
