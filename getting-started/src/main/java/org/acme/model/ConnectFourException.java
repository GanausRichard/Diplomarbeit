package org.acme.model;

public class ConnectFourException extends Exception {
    public String message;

    public ConnectFourException(String message) {
        this.message = message;
    }
}
