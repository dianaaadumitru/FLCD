import domain.FiniteAutomata;

import java.util.Scanner;

public class Main {

    private static String menu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMenu:\n");
        sb.append("1. Display the states.\n");
        sb.append("2. Display the alphabet.\n");
        sb.append("3. Display the initial state.\n");
        sb.append("4. Display the final states.\n");
        sb.append("5. Display the transitions.\n");
        sb.append("6. Check if a sequence is accepted by the FA.\n");
        sb.append("0. Exit.\n");

        return sb.toString();
    }

    private static boolean checkIfAccepted(FiniteAutomata finiteAutomata) {
        System.out.println("sequence: ");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        System.out.println(word);
        return finiteAutomata.checkIfAccepted(word);
    }

    public static void main(String[] args) {
        FiniteAutomata finiteAutomata = new FiniteAutomata("C:\\Users\\diana\\Desktop\\uni work\\5th sem\\flcd\\FLCD2\\lab4\\src\\data\\FA.in");
        boolean done = false;
        while (!done) {
            System.out.println(menu());
            System.out.println(">>");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> System.out.println(finiteAutomata.getStates().toString());
                case 2 -> System.out.println(finiteAutomata.getAlphabet().toString());
                case 3 -> System.out.println(finiteAutomata.getInitialState().toString());
                case 4 -> System.out.println(finiteAutomata.getFinalStates().toString());
                case 5 -> System.out.println(finiteAutomata.displayTransitions());
                case 6 -> System.out.println(checkIfAccepted(finiteAutomata));
                case 0 -> done = true;
            }
        }
    }
}
