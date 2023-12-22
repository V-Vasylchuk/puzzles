package com.vvs.puzzles.exeption;

public class InputException extends RuntimeException {
    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }
}
