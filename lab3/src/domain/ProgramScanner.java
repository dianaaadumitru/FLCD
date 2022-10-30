package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class ProgramScanner {
    private ArrayList<String> operators;
    private ArrayList<String> separators;
    private ArrayList<String> reservedWords;

    private ST<String> symbolTable;
    private final String pathSourceCode;

    public ArrayList<String> getOperators() {
        return operators;
    }

    public ArrayList<String> getSeparators() {
        return separators;
    }

    public ArrayList<String> getReservedWords() {
        return reservedWords;
    }

    public ProgramScanner(String pathSourceCode) {
        this.pathSourceCode = pathSourceCode;
        this.symbolTable = new ST<>(new HashTable<>(10));
        readTokens();
    }

    private void readTokens() {
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD\\lab2\\src\\data\\token.in"));
            operators = new ArrayList<>(List.of(scanner.nextLine().split(",")));
            separators = new ArrayList<>(List.of(scanner.nextLine().split(",")));
            separators.add("\n");
            separators.add(",");
            reservedWords = new ArrayList<>(List.of(scanner.nextLine().split(",")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String readCode() {
        StringBuilder code = new StringBuilder();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(this.pathSourceCode));
            while (scanner.hasNextLine()) {
                code.append(scanner.nextLine()).append("\n");
            }
            return code.toString().replace("\t", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> tokenize() {
        String code = this.readCode();
        String separatorsString = this.separators.toString();

        return Collections.list(new StringTokenizer(code, separatorsString, true)).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }

    public boolean isIdentifier(String symbol) {
        return symbol.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    public boolean isConstant(String symbol) {
        if (symbol.matches("[a-zA-Z0-9]*")) {
            return true;
        }

        return false;
    }

}
