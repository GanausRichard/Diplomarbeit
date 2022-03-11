package org.acme.model;

public class ConnectFourException extends Exception {
    public String message;

    //class constructor: an exception message has to be passed to create a new connect four exception
    public ConnectFourException(String message) {
        this.message = message;
    }
}
