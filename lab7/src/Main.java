import parser_LR0.CanonicalCollection;
import parser_LR0.Grammar;
import parser_LR0.Parser;
import parsingTable.ParsingTable;
import tests.Tests;

import java.util.List;
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
        sb.append("6. LR(0).\n");
        sb.append("7. Parsing table.\n");
        sb.append("8. Tests.\n");
        sb.append("0. Exit.\n");

        return sb.toString();
    }

    private static List<List<String>> displayProdForNonterminal(Grammar grammar) {
        System.out.print("given production: ");
        Scanner scanner = new Scanner(System.in);
        String nonterm = scanner.next();
        return grammar.getProductionsForNonTerminal(nonterm);
    }

    private static void getParsingTable(Parser parser) {
        CanonicalCollection canonicalCollection = parser.canonicalCollection();
        ParsingTable parsingTable = parser.createParsingTable(canonicalCollection);
        if (parsingTable.entries.size() == 0) {
            System.out.println("We have conflicts in the parsing table");
        } else {
            System.out.println(parsingTable);
        }
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar("./res/data/g3.txt");
        var parser = new Parser(grammar);
        Tests tests = new Tests();
        boolean done = false;
        while (!done) {
            System.out.println(menuGrammar());
            System.out.println(">>");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> System.out.println(grammar.getNonterminals());
                case 2 -> System.out.println(grammar.getTerminals());
                case 3 -> System.out.println(grammar.getProductions());
                case 4 -> System.out.println(displayProdForNonterminal(grammar));
                case 5 -> System.out.println(grammar.checkCFG());
                case 6 -> System.out.println(parser.canonicalCollection().states);
                case 7 -> getParsingTable(parser);
                case 8 -> tests.runAllTests();

                case 0 -> done = true;
            }
        }
    }
}
