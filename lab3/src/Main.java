import domain.HashTable;
import domain.ProgramScanner;
import domain.ST;
import exceptions.ScannerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab3\\src\\data\\p3.txt"));
            StringBuilder program = new StringBuilder();
            while (scanner.hasNextLine()) {
                program.append(scanner.nextLine()).append('\n');
            }
            scanner.close();

            Scanner scannerTokens = new Scanner(new File("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab3\\src\\data\\token.in"));
            List<String> tokens = new ArrayList<>();

            while (scannerTokens.hasNextLine()) {
                tokens.add(scannerTokens.nextLine());
            }
            scannerTokens.close();

            ProgramScanner programScanner = new ProgramScanner(program.toString(), tokens);
            try {
                programScanner.scan();
                programScanner.writeToPIFFile();
                programScanner.writeToSTFile();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                programScanner.writeToPIFFile();
                programScanner.writeToSTFile();
                return;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        catch (IOException e) {
            System.out.println("Cannot write into output files");
        }
    }
}
