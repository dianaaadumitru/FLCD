package domain;

import exceptions.ScannerException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramScanner {
//    private ArrayList<String> operators;
//    private ArrayList<String> separators;
//    private ArrayList<String> reservedWords;
//
//    private ST<String> symbolTable;
//    private final String pathSourceCode;
//
//    public ArrayList<String> getOperators() {
//        return operators;
//    }
//
//    public ArrayList<String> getSeparators() {
//        return separators;
//    }
//
//    public ArrayList<String> getReservedWords() {
//        return reservedWords;
//    }
//
//    public ProgramScanner(String pathSourceCode) {
//        this.pathSourceCode = pathSourceCode;
//        this.symbolTable = new ST<>(new HashTable<>(10));
//        readTokens();
//    }
//
//    private void readTokens() {
//        try {
//            Scanner scanner = new Scanner(new File("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD\\lab2\\src\\data\\token.in"));
//            operators = new ArrayList<>(List.of(scanner.nextLine().split(",")));
//            separators = new ArrayList<>(List.of(scanner.nextLine().split(",")));
//            separators.add("\n");
//            separators.add(",");
//            reservedWords = new ArrayList<>(List.of(scanner.nextLine().split(",")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String readCode() {
//        StringBuilder code = new StringBuilder();
//        Scanner scanner;
//        try {
//            scanner = new Scanner(new File(this.pathSourceCode));
//            while (scanner.hasNextLine()) {
//                code.append(scanner.nextLine()).append("\n");
//            }
//            return code.toString().replace("\t", "");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<String> tokenize() {
//        String code = this.readCode();
//        String separatorsString = this.separators.toString();
//
//        return Collections.list(new StringTokenizer(code, separatorsString, true)).stream()
//                .map(token -> (String) token)
//                .collect(Collectors.toList());
//    }
//
//    public boolean isIdentifier(String symbol) {
//        return symbol.matches("[a-zA-Z][a-zA-Z0-9]*");
//    }
//
//    public boolean isConstant(String symbol) {
//        if (symbol.matches("[a-zA-Z0-9]*")) {
//            return true;
//        }
//
//        return false;
//    }

    private String program;
    private List<String> tokens;
    private ST<String> symbolTable;
    private List<Pair<String, Pair<Integer, Integer>>> pif;
    private int currentLine;
    private int index;

    public ProgramScanner(String program, List<String> tokens) {
        this.program = program;
        this.tokens = tokens;
        this.pif = new ArrayList<>();
        this.symbolTable = new ST<>();
        this.currentLine = 1;
        this.index = 0;
    }

    public boolean skipWhiteSpace() {
        boolean changed = false;
        while (index < program.length() && Character.isWhitespace(program.charAt(index))) {
            if (program.charAt(index) == '\n') {
                currentLine++;
                changed = true;
            }
            index++;
        }
        return changed;
    }

    public boolean isIntConstant() {
        Pattern intRegex = Pattern.compile("^([-]?[1-9]\\d*|0)");
        Matcher matcher = intRegex.matcher(program.substring(index));
        while (matcher.find()) {
            String token = matcher.group();
            pif.add(new Pair<>("intConst", new Pair<>(-2, -2)));
            symbolTable.addToST(token);
            index += matcher.end();
            System.out.println("Found int constant: " + token);
            return true;
        }
        return true;
    }

    public boolean isStringConstant() throws ScannerException {
        //must begin with " and end with " and must contain only letters, digits, _ and space
        Pattern strRegex = Pattern.compile("\"([a-zA-z0-9_ ]*)\"");
        Matcher matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group();
            pif.add(new Pair<>("stringConst", new Pair<>(-3, -3)));
            symbolTable.addToST(token);
            index += matcher.end();
            System.out.println("Found string constant: " + token);
            return true;
        }

        //start with " but never closed
        strRegex = Pattern.compile("^\"");
        matcher = strRegex.matcher(program.substring(index));
        if (matcher.find())
            throw new ScannerException("Lexical error: quotes open but never closed closed on line " + currentLine);

        return false;
    }

    private boolean isTokenFromList() {
        for (String token : tokens) {
            if (program.startsWith(token, index)) {
                pif.add(new Pair<>(token, new Pair<>(-1, -1)));
                index += token.length();
                System.out.println("Found token from list: " + token);
                return true;
            }
        }
        return false;
    }

    private boolean isIdentifier() {
        Pattern idRegex = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*)");
        Matcher matcher = idRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group(1);
            System.out.println("Found identifier: " + token);
            Pair<Integer, Integer> pair = symbolTable.addToST(token);
            if (pair.getKey() != -1) {
                pif.add(new Pair<>("id", pair));
            }
            index += matcher.end();
            return true;
        }
        return false;
    }

    public void nextToken() throws ScannerException {
        while (true) {
            if (!skipWhiteSpace())
                break;
        }

        if (index == program.length())
            return;

        if (isTokenFromList() || isIdentifier() || isStringConstant())
            return;

        StringBuilder errorToken = new StringBuilder();
        while (index < program.length() && (!Character.isWhitespace(program.charAt(index)) || program.charAt(index) == '\n') && !tokens.contains(program.charAt(index) + "")) {
            errorToken.append(program.charAt(index));
            index++;
        }
        throw new ScannerException("Lexical error: " + errorToken + " on line " + currentLine + " can not be classified!");
    }

    public void scan() throws ScannerException {
        while (index < program.length()) {
            nextToken();
        }
    }

    public void writeToSTFile () throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab3\\src\\data\\ST.out");
        writer.write(symbolTable.toString());
        writer.close();
    }

    public void writeToPIFFile() throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab3\\src\\data\\PIF.out");
        StringBuilder str = new StringBuilder();
        pif.forEach(e -> str.append(e.getKey()).append(" -> ").append("(").append(e.getValue().getKey()).append(",")
                .append(e.getValue().getValue()).append(")").append('\n'));
        writer.write(str.toString());
        writer.close();
    }
}
