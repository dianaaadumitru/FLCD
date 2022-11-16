package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FiniteAutomata {
    private final List<String> states;
    private final List<String> alphabet;
    private final List<String> initialState;
    private final List<String> finalStates;
    private final List<Pair<Pair<String, String>, String>> transitions;

    private final String file;

    public FiniteAutomata(String file) {
        this.file = file;
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.initialState = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.transitions = new ArrayList<>();
        readFromFile(file);
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public List<Pair<Pair<String, String>, String>> getTransitions() {
        return transitions;
    }

    public List<String> getInitialState() {
        return initialState;
    }

    public String displayTransitions() {
        StringBuilder sb = new StringBuilder();
        for (var transition: transitions) {
            sb.append(transition.getKey().getKey()).append("->").append(transition.getKey().getValue()).append("->").append(transition.getValue()).append("\n");
        }

        return sb.toString();
    }

    public void readFromFile(String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            String[] statesStringArray = scanner.nextLine().split(",");
            states.addAll(Arrays.asList(statesStringArray));

            String[] alphabetStringArray = scanner.nextLine().split(",");
            alphabet.addAll(Arrays.asList(alphabetStringArray));

            String[] initialStateStringArray = scanner.nextLine().split(",");
            initialState.addAll(Arrays.asList(initialStateStringArray));

            String[] finalStatesStringArray = scanner.nextLine().split(",");
            finalStates.addAll(Arrays.asList(finalStatesStringArray));

            while (scanner.hasNextLine()) {
                String[] transitionPair = scanner.nextLine().split(",");
                transitions.add(new Pair<>(new Pair<>(transitionPair[0], transitionPair[1]), transitionPair[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> stringToList(String string) {
        return List.of(string.split(""));
    }

    public boolean checkIfAccepted(String givenWord) {
        List<String> word = stringToList(givenWord);
        String currentState = initialState.get(0);
        for (String letter : word) {
            boolean found = false;
            for (var transition : transitions) {
                if (transition.getKey().getKey().equals(currentState) && transition.getKey().getValue().equals(letter)) {
                    currentState = transition.getValue();
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        for (String finalState : finalStates) {
            if (finalState.equals(currentState))
                return true;
        }
        return false;
    }
}
