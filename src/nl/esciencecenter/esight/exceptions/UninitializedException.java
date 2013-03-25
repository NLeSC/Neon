package nl.esciencecenter.esight.exceptions;

public class UninitializedException extends Exception {
    public UninitializedException() {
        super("");
    }

    public UninitializedException(String string) {
        super(string);
    }

    private static final long serialVersionUID = 7346571330247360360L;

}
