import domain.HashTable;
import domain.ProgramScanner;
import domain.ST;

public class Main {
    public static void main(String[] args) {
        HashTable<String> ht = new HashTable<>(10);
        ST<String> symTable = new ST<>(ht);

        // test add
//        System.out.println(symTable.addToST("heii"));
//        System.out.println(symTable.addToST("hiei"));
//        System.out.println(symTable.addToST("ihei"));
//        System.out.println(symTable.addToST("a"));
//        System.out.println(symTable.addToST("b"));
//        System.out.println(symTable.addToST("c"));
//        System.out.println(symTable.addToST("d"));
//        System.out.println(symTable.addToST("e"));
//        System.out.println(symTable.addToST("f"));
//
//        System.out.println(symTable);
//
//        //test remove
//        symTable.removeItem("heii");
//        System.out.println(symTable);

        ProgramScanner programScanner = new ProgramScanner("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD\\lab2\\src\\data\\p1.txt");
//        var l1 = programScanner.getOperators();
//        for (var s: l1) {
//            System.out.println(s);
//        }

        System.out.println(programScanner.isConstant("a"));
    }
}
