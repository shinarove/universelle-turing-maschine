package ch.zhaw.it.thin;

public class Tape {


    private int pointer = 0;
    private final StringBuilder leftEmptyTape = new StringBuilder("_______________");
    private final StringBuilder rightEmptyTape = new StringBuilder("_______________");
    private final StringBuilder tape = new StringBuilder();

    public Tape(String inputString) {
        tape.append(inputString);
    }

    public char read() {
        return tape.charAt(pointer);
    }

    public void write(char c, String move) {
        tape.replace(pointer, pointer + 1, c + "");
        if (move.equals("R")) {
            if (pointer == tape.length() - 1) {
                tape.append("_");
                rightEmptyTape.deleteCharAt(0);
            }
            pointer++;
        } else if (move.equals("L")) {
            if (pointer == 0) {
                tape.insert(0, "_");
                leftEmptyTape.deleteCharAt(0);
            } else {
                pointer--;
            }
        }
        if (rightEmptyTape.length() > 15) {
            rightEmptyTape.delete(0, rightEmptyTape.length() - 15);
        }
        if (leftEmptyTape.length() > 15) {
            leftEmptyTape.delete(0, leftEmptyTape.length() - 15);
        }
        if (rightEmptyTape.length() < 15) {
            rightEmptyTape.append("_");
        }
        if (leftEmptyTape.length() < 15) {
            leftEmptyTape.append("_");
        }
    }

    public String getTape() {
        return tape.toString();
    }

    @Override
    public String toString() {
        String pointerOnTape = " ".repeat(leftEmptyTape.length() + pointer) + "V\n";
        String tapeWithEmpty = leftEmptyTape.toString() + tape + rightEmptyTape;
        return pointerOnTape + tapeWithEmpty;
    }
}
