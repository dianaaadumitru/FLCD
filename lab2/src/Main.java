public class Main {
    public static void main(String[] args) {
        HashTable<String> ht = new HashTable<>(10);
        System.out.println(ht.addToHT("heii"));
        System.out.println(ht.addToHT("hiei"));
        System.out.println(ht.addToHT("ihei"));
        System.out.println(ht.addToHT("heii"));

        System.out.println(ht);
    }
}
