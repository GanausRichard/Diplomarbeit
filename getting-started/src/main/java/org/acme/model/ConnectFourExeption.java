package org.acme.model;

public class ConnectFourExeption extends Exception {
    public String message;

    public ConnectFourExeption(String message) {
        this.message = message;
    }
}
