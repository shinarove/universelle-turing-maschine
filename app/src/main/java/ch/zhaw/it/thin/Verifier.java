package ch.zhaw.it.thin;

import java.util.HashMap;
import java.util.Map;

public class Verifier {

    private final Map<Character, String> charCodes = new HashMap<>();
    private final Map<String, String> moveCodes = new HashMap<>();

    public Verifier() {
        char[] chars = {'0', '1', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int index = 1;
        for (char ch : chars) {
            charCodes.put(ch, "0".repeat(index));
            index++;
        }

        moveCodes.put("L", "0");
        moveCodes.put("R", "00");
    }


    public char convertBinaryToChar(String binary) throws IllegalArgumentException{
        for (Map.Entry<Character, String> entry : charCodes.entrySet()) {
            if (entry.getValue().equals(binary)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Invalid binary code: " + binary);
    }

    public String convertBinaryToMove(String binary) {
        for (Map.Entry<String, String> entry : moveCodes.entrySet()) {
            if (entry.getValue().equals(binary)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getState(String binary) {
        if (binary.matches("0+")) {
            int countZeros = binary.length();
            return "q" + (countZeros);
        }
        return null;
    }

}
