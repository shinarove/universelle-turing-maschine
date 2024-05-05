package ch.zhaw.it.thin;

public class Tape {


    private int pointer = 0;
    private final String emptyTape = "_______________";
    private StringBuilder pointerOnTape = new StringBuilder();
    private StringBuilder tape = new StringBuilder();

    public Tape(String inputString) {
        tape.append(emptyTape);
        tape.append(inputString);
        tape.append(emptyTape);
    }

    public char read() {
        return tape.charAt(pointer + emptyTape.length());
    }

    public void write(char c, String move) {
        int index = pointer + emptyTape.length();
        tape.replace(index, index + 1, c + "");
        if (move.equals("R")) {
            pointer++;
        } else if (move.equals("L")) {
            if (pointer == 0) {
                tape.insert(0, "_");
            } else {
                pointer--;
            }
        }
    }

    @Override
    public String toString() {
        String pointerOnTape = " ".repeat(emptyTape.length() + pointer) + "V\n";
        return pointerOnTape + tape.toString();
    }
}
