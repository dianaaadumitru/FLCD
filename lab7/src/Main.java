import exceptions.ScannerException;
import parser_LR0.CanonicalCollection;
import parser_LR0.Grammar;
import parser_LR0.Parser;
import parsingTable.ParsingTable;
import scanner.ProgramScanner;
import tests.Tests;

import java.io.*;
import java.util.*;

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
        sb.append("7. Parsing tree for g1.\n");
        sb.append("8. Parsing tree for g2.\n");
        sb.append("9. Tests.\n");
        sb.append("0. Exit.\n");

        return sb.toString();
    }

    private static String menuParser() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n1. Parse p1.txt\n");
        sb.append("2. Parse p2.txt\n");
        sb.append("3. Parse p3.txt\n");
        sb.append("0. Exit.\n");

        return sb.toString();
    }

    private static List<List<String>> displayProdForNonterminal(Grammar grammar) {
        System.out.print("given production: ");
        Scanner scanner = new Scanner(System.in);
        String nonterm = scanner.next();
        return grammar.getProductionsForNonTerminal(nonterm);
    }

    private static void emptyFile(String file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    private static Stack<String> readFirstElemFromFile(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<String>();
        ArrayList<String> normal = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\\s+");
                normal.add(split[0]);
                line = reader.readLine();
            }
            for (int i = normal.size() - 1; i >= 0; i--) {
                wordStack.add(normal.get(i));
            }
            return wordStack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getParsingTreeForG1() throws IOException {
        emptyFile("res/data/out1.txt");
        Grammar grammar = new Grammar("./res/data/g3.txt");
        Parser parser = new Parser(grammar);
        CanonicalCollection canonicalCollection = parser.canonicalCollection();
        ParsingTable parsingTable = parser.createParsingTable(canonicalCollection);
        if (parsingTable.entries.size() == 0) {
            System.out.println("We have conflicts in the parsing table");
        } else {
            System.out.println(parsingTable);
        }

        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        try {
            reader = new BufferedReader(new FileReader("res/data/seq.txt"));
            String line = reader.readLine();
            if (line != null) {
                Arrays.stream(new StringBuilder(line).reverse().toString().split("")).forEach(wordStack::push);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        parser.parse(wordStack, parsingTable, "res/data/out1.txt");
    }

    private static ProgramScanner createProgramScanner(String filename) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder program = new StringBuilder();
        while (scanner.hasNextLine()) {
            program.append(scanner.nextLine()).append('\n');
        }
        scanner.close();

        Scanner scannerTokens = null;
        try {
            scannerTokens = new Scanner(new File("res/data/token.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> tokens = new ArrayList<>();

        while (true) {
            assert scannerTokens != null;
            if (!scannerTokens.hasNextLine()) break;
            tokens.add(scannerTokens.nextLine());
        }
        scannerTokens.close();

        return new ProgramScanner(program.toString(), tokens);
    }

    private static void getParsingTreeForG2() {
        Grammar grammar = new Grammar("./res/data/g2.txt");
        Parser parser = new Parser(grammar);
        CanonicalCollection canonicalCollection = parser.canonicalCollection();
        ParsingTable parsingTable = parser.createParsingTable(canonicalCollection);
        if (parsingTable.entries.size() == 0) {
            System.out.println("We have conflicts in the parsing table");
        } else {
            System.out.println(parsingTable);
        }

        boolean done = false;
        while (!done) {
            System.out.println(menuParser());
            System.out.print(">>");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    ProgramScanner programScanner = createProgramScanner("res/data/p1.txt");
                    emptyFile("res/data/out2.txt");
                    emptyFile("res/data/p1PIF.txt");
                    try {
                        programScanner.scan();
                        programScanner.writeToPIFFile("res/data/p1PIF.txt");

                        Stack<String> wordStack = readFirstElemFromFile("res/data/p1PIF.txt");

                        parser.parse(wordStack, parsingTable, "res/data/out2.txt");
                    } catch (ScannerException | IOException e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    ProgramScanner programScanner = createProgramScanner("res/data/p2.txt");
                    emptyFile("res/data/out2.txt");
                    emptyFile("res/data/p2PIF.txt");
                    try {
                        programScanner.scan();
                        programScanner.writeToPIFFile("res/data/p2PIF.txt");

                        Stack<String> wordStack = readFirstElemFromFile("res/data/p2PIF.txt");

                        parser.parse(wordStack, parsingTable, "res/data/out2.txt");
                    } catch (ScannerException | IOException e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> {
                    ProgramScanner programScanner = createProgramScanner("res/data/p3.txt");
                    emptyFile("res/data/out2.txt");
                    emptyFile("res/data/p3PIF.txt");
                    try {
                        programScanner.scan();
                        programScanner.writeToPIFFile("res/data/p3PIF.txt");

                        Stack<String> wordStack = readFirstElemFromFile("res/data/p3PIF.txt");

                        parser.parse(wordStack, parsingTable, "res/data/out2.txt");
                    } catch (ScannerException | IOException e) {
                        e.printStackTrace();
                    }
                }

                case 0 -> done = true;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar("./res/data/g3.txt");
        Parser parser = new Parser(grammar);
        Tests tests = new Tests();
        boolean done = false;
        while (!done) {
            System.out.println(menuGrammar());
            System.out.print(">>");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> System.out.println(grammar.getNonterminals());
                case 2 -> System.out.println(grammar.getTerminals());
                case 3 -> System.out.println(grammar.getProductions());
                case 4 -> System.out.println(displayProdForNonterminal(grammar));
                case 5 -> System.out.println(grammar.checkCFG());
                case 6 -> System.out.println(parser.canonicalCollection().states);
                case 7 -> getParsingTreeForG1();
                case 8 -> getParsingTreeForG2();
                case 9 -> tests.runAllTests();

                case 0 -> done = true;
            }
        }
    }
}
