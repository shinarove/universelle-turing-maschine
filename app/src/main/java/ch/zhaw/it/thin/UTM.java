package ch.zhaw.it.thin;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;

public class UTM {

    private final Input input = new Input();
    private final Output output = new Output();
    private final Verifier verifier = new Verifier();
    private final Set<String> states = new HashSet<>();
    private final Set<Character> inputAlphabet = new HashSet<>();
    private final Set<Character> tapeAlphabet = new HashSet<>();
    private final List<Transition> transitions = new ArrayList<>();

    private int countCalculation = 1;

    public UTM(){
        inputAlphabet.add('0');
        inputAlphabet.add('1');
    }

    public void start(){
        boolean running = true;
        do {
            output.println("Von wo soll die Turing-Maschine gelesen werden?");
            output.println("(S)ystem.in oder (F)ile");
            char option = input.readLine().charAt(0);
            switch (option) {
                case 'S' -> running = systemIn();
                case 'F' -> running = file();
                default -> output.println("Ungültige Eingabe, bitte erneut versuchen.\n");
            }
        } while (running);
    }


    private boolean systemIn() {
        output.println("\nBitte Gödelnummer eingeben oder kodierte Turingmaschine: ");
        String inputString = input.readLine();
        if (inputString.isEmpty()){
            output.println("Keine Eingabe!\n");
        } else if (inputString.matches("[01]+111[01]+")){
            String[] parts = inputString.split("111");
            if (parts.length != 2) {
                output.println("Mehrfaches Vorkommen des Substring: \"111\" erkannt!\n");
            } else {
                prepareTuringMachine(parts[0]);
                runTuringMachine(parts[1]);
            }
        } else if (inputString.matches("\\d+")){
            String convertedInput = new BigInteger(inputString).toString(2);
            if (convertedInput.matches("[01]+111[01]+")) {
                String[] parts = convertedInput.split("111");
                if (parts.length != 2) {
                    output.println("Mehrfaches Vorkommen des Substring: \"111\" erkannt!\n");
                } else {
                    prepareTuringMachine(parts[0]);
                    runTuringMachine(parts[1]);
                }
            } else {
                output.println("Ungültige Gödelnummer!\n");
            }
        } else {
            output.println("Ungültige Eingabe!\n");
        }
        return stillRunning();
    }

    /**
     * Reads the Turing machine from a file and runs it.
     * The default file path is "src/main/resources/turing-maschine.txt".
     * @return true if the user wants to run the program again, false otherwise
     */
    private boolean file(){
        String defaultFilePath = "app/src/main/resources/turing-maschine.txt";
        output.println("\nGelesene Datei: " + defaultFilePath);
        Path path = Path.of(defaultFilePath);

        try {
            Scanner fileScanner = new Scanner(path.toFile());
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.matches("[01]+111[01]+")){
                    String[] parts = line.split("111");
                    if (parts.length != 2) {
                        output.println("Mehrfaches Vorkommen des Substring: \"111\" erkannt!\n");
                    } else {
                        prepareTuringMachine(parts[0]);
                        runTuringMachine(parts[1]);
                    }
                } else {
                    output.println("Ungültige Zeile in der Datei: " + line + "\n");
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            output.println("Datei nicht gefunden: " + defaultFilePath);
        }

        return stillRunning();
    }

    private void prepareTuringMachine(String turingMachine){
        if (turingMachine.charAt(0) == '1') {
            turingMachine = turingMachine.substring(1);
            if (turingMachine.charAt(0) == '1') {
                output.println("Mehrfaches Vorkommen einer eins am Anfang.\n");
                return;
            }
        }
        String[] transitions = turingMachine.split("11");
        for (String transition : transitions) {
            String[] transitionParts = transition.split("1");
            if (transitionParts.length != 5) {
                output.println("Zu viele Elemente in dieser Übergangsfunktion: " + transition + "\n");
                return;
            }
            try {
                String startingState = Objects.requireNonNull(verifier.getState(transitionParts[0]));
                char readSymbol = verifier.convertBinaryToChar(transitionParts[1]);
                String nextState = Objects.requireNonNull(verifier.getState(transitionParts[2]));
                char writeSymbol = verifier.convertBinaryToChar(transitionParts[3]);
                String move = Objects.requireNonNull(verifier.convertBinaryToMove(transitionParts[4]));

                states.add(startingState);
                states.add(nextState);

                tapeAlphabet.add(readSymbol);
                tapeAlphabet.add(writeSymbol);

                this.transitions.add(new Transition(startingState, readSymbol, nextState, writeSymbol, move));
            } catch (NullPointerException | IllegalArgumentException e) {
                output.println("Ungültige Übergangsfunktion: " + transition + "\n");
                return;
            }
        }
    }

    private void runTuringMachine(String inputString) {
        Tape tape = new Tape(inputString);
        printTuringMachineInformation(tape);

        output.println("Wie möchtest du die Turing-Maschine laufen lassen?");
        output.println("(S)chrittweise oder (A)utomatisch");
        if (input.readLine().charAt(0) == 'S') {
            runStepByStep(tape);
        } else {
            runAutomatically(tape);
        }
    }

    private void printTuringMachineInformation(Tape tape) {
        output.println("\nZustände der Turing-Maschine:");
        output.print("{");
        int countDown = states.size();
        for (String state : states) {
            output.print(state);
            if (countDown > 1) {
                output.print(", ");
            }
            countDown--;
        }
        output.println("}");

        output.println("Eingabealphabet der Turing-Maschine:");
        output.print("{");
        countDown = inputAlphabet.size();
        for (char symbol : inputAlphabet) {
            output.print(symbol + "");
            if (countDown > 1) {
                output.print(", ");
            }
            countDown--;
        }
        output.println("}");

        output.println("Bandalphabet der Turing-Maschine:");
        output.print("{");
        countDown = tapeAlphabet.size();
        for (char symbol : tapeAlphabet) {
            output.print(symbol + "");
            if (countDown > 1) {
                output.print(", ");
            }
            countDown--;
        }
        output.println("}");

        output.println("Startzustand: Übergangsfunktionen der Turing-Maschine:");
        for (Transition transition : transitions) {
            output.println(transition.toString());
        }

        output.println("\nAnfangsband:");
        output.println(tape.toString());
    }

    private void runStepByStep(Tape tape) {
        String currentState = "q1";
        do {
            output.println("Schritt " + countCalculation + ":");
            String nextState = makeStep(currentState, tape.read(), tape, true);
            if (nextState == null && currentState.equals("q2")) {
                output.println("\nTuring-Maschine hält im akzeptierenden Zustand q2 an.");
                output.println("Anzahl der benötigten Berechnungen: " + (countCalculation - 1));
                output.println(tape.toString());
                break;
            } else if (nextState == null) {
                output.println("\nTuring-Maschine hält im nicht akzeptierenden Zustand: " + currentState + " an.");
                output.println("Anzahl der benötigten Berechnungen: " + (countCalculation - 1));
                output.println(tape.toString());
                break;
            } else {
                currentState = nextState;
                output.println(tape.toString());
            }
        } while (!Objects.equals(input.readLine(), "Q"));
    }

    private void runAutomatically(Tape tape) {
        String currentState = "q1";
        while (true) {
            String nextState = makeStep(currentState, tape.read(), tape, false);
            if (nextState == null && currentState.equals("q2")) {
                output.println("\nTuring-Maschine hält im akzeptierenden Zustand q2 an.");
                output.println("Anzahl der benötigten Berechnungen: " + (countCalculation - 1));
                output.println(tape.toString());
                break;
            } else if (nextState == null) {
                output.println("\nTuring-Maschine hält im nicht akzeptierenden Zustand: " + currentState + " an.");
                output.println("Anzahl der benötigten Berechnungen: " + (countCalculation - 1));
                output.println(tape.toString());
                break;
            } else {
                currentState = nextState;
            }
        }
    }

    private String makeStep(String currentState, char readSymbol, Tape tape, boolean showInfo) {
        countCalculation++;
        for (Transition transition : transitions) {
            if (transition.startingState().equals(currentState) && transition.read() == readSymbol) {
                if (showInfo) {
                    output.println("Übergangsfunktion: " + transition);
                }
                tape.write(transition.write(), transition.move());
                return transition.nextState();
            }
        }
        return null;
    }

    private boolean stillRunning(){
        output.println("\nNochmals? (J)a oder (N)ein");
        return input.readLine().charAt(0) == 'J';
    }
}
