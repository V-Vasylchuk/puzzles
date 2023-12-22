package com.vvs.puzzles.exeption;

public class DownloadException extends RuntimeException {
    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
