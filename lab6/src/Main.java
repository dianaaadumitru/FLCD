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
        Grammar grammar = new Grammar("./res/data/g2.txt");

        Parser parser = new Parser(grammar);
        parser.canonicalCollection();
//        System.out.println(parser.getProductionsWithDot().toString());
    }
}
