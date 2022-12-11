import domain.Grammar;
import domain.Parser;

import java.util.Scanner;

public class Main {
    private static String menuGrammar() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMenu:\n");
        sb.append("1. Display the nonterminals.\n");
        sb.append("2. Display the terminals.\n");
        sb.append("3. Display the productions.\n");
        sb.append("4. Display the productions for a given nonterminal.\n");
        sb.append("5. Check if it is a CFG.\n");
        sb.append("0. Exit.\n");

        return sb.toString();
    }

    private static String displayProdForNonterminal(Grammar grammar) {
        System.out.print("given production: ");
        Scanner scanner = new Scanner(System.in);
        String nonterm = scanner.next();
        return grammar.displayProdForNonTerm(nonterm);
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab6\\src\\data\\g1.txt");
        boolean done = false;
//        while (!done) {
//            System.out.println(menuGrammar());
//            System.out.println(">>");
//            Scanner scanner = new Scanner(System.in);
//            int choice = scanner.nextInt();
//            switch (choice) {
//                case 1 -> System.out.println(grammar.getNonterminals().toString());
//                case 2 -> System.out.println(grammar.getTerminals().toString());
//                case 3 -> System.out.println(grammar.displayProductions());
//                case 4 -> System.out.println(displayProdForNonterminal(grammar));
//                case 5 -> System.out.println(grammar.checkIfCFG());
//
//                case 0 -> done = true;
//            }
//        }
        Parser parser = new Parser(grammar);
        parser.canonicalCollection();
        System.out.println(parser.getProductionsWithDot().toString());
    }
}
