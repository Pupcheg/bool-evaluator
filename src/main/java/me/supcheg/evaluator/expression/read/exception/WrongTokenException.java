package me.supcheg.evaluator.expression.read.exception;

public class WrongTokenException extends SyntaxException {

    public WrongTokenException(String message, int start, int end) {
        super(message, start, end);
    }

    public WrongTokenException(int start, int end) {
        super("Wrong token from " + start + " to " + end, start, end);
    }

}
