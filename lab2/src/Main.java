public class Main {
    public static void main(String[] args) {
        HashTable<String> ht = new HashTable<>(10);
        ST<String> symTable = new ST<>(ht);

        // test add
        System.out.println(symTable.addToST("heii"));
        System.out.println(symTable.addToST("hiei"));
        System.out.println(symTable.addToST("ihei"));
        System.out.println(symTable.addToST("heii"));

        System.out.println(symTable);

        //test remove
        symTable.removeItem("heii");
        System.out.println(symTable);
    }
}
