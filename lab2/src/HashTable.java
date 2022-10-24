import java.util.ArrayList;
import java.util.List;

public class HashTable<T> {
    private final int size;

    private final List<List<T>> items;

    private final double loadFactor = 0.75;

    private int actualSize;

    public HashTable(int size) {
        this.size = size;
        this.items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.items.add(new ArrayList<>());
        }
    }

    public int hash(T key) {
        int keySum = 0;
        for (int i = 0; i < String.valueOf(key).length(); i++) {
            keySum += String.valueOf(key).charAt(i);
        }
        return keySum % size;
    }

    public Pair<Integer, Integer> addToHT(T key) {
        int listPos = hash(key);
        if (contain(key)) {
            return getPosition(key);
        }
        items.get(listPos).add(key);
        return getPosition(key);
    }

    public boolean contain(T key) {
        return items.get(hash(key)).contains(key);
    }

    public Pair<Integer, Integer> getPosition(T key) {
        int listPos = hash(key);
        int index = 0;
        for (T item: items.get(listPos)) {
            if (item == key) {
                return new Pair<>(listPos, index);
            }
            index++;
        }
        return new Pair<>(-1, -1);
    }

    public boolean removeItem(T key) {
        int listPos = hash(key);
        return items.get(listPos).remove(key);
    }

    @Override
    public String toString() {
        return "HashTable{" +
                "size=" + size +
                ", items=" + items +
                '}';
    }
}
