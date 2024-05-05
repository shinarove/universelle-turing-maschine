package ch.zhaw.it.thin;

public record Transition(String startingState, char read, String nextState, char write, String move){

    @Override
    public String toString(){
        return ("(" + startingState + ", " + read + ") -> (" + nextState + ", " + write + ", " + move + ")");
    }
}
