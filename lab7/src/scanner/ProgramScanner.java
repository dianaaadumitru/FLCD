package scanner;

import exceptions.ScannerException;
import utils.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramScanner {

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
        if (matcher.find()) {
            String token = matcher.group(1);

            Pair<Integer, Integer> pair = symbolTable.addToST(token);
            pif.add(new Pair<>(token, pair));
            index += matcher.end();
            return true;
        }


        return false;
    }

    public boolean isStringConstant() throws ScannerException {
        //must begin with " and end with " and must contain only letters, digits, _ and space
        Pattern strRegex = Pattern.compile("\"([a-zA-z0-9_ ]*)\"");
        Matcher matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group(1);
            Pair<Integer, Integer> pair = symbolTable.addToST(token);
            pif.add(new Pair<>(token, pair));
            index += matcher.end();
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
            Pair<Integer, Integer> pair = symbolTable.addToST(token);
            if (pair.getKey() != -1) {
                pif.add(new Pair<>(token, pair));
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

        if (isIntConstant() || isTokenFromList() || isIdentifier() || isStringConstant())
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

    public void writeToSTFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(symbolTable.toString());
        writer.close();
    }

    public void writeToPIFFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        StringBuilder str = new StringBuilder();
        pif.forEach(e -> str.append(e.getKey()).append(" -> ").append("(").append(e.getValue().getKey()).append(",")
                .append(e.getValue().getValue()).append(")").append('\n'));
        writer.write(str.toString());
        writer.close();
    }
}
