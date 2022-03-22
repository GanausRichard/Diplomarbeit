package org.acme.model;

public class Settings { //these settings get set by the player at the start of the game
    public String gameMode; //the game mode can either be pve or pvp (human vs robot or human vs human)
    public int endNode; //sets how deep the search tree should be (difficulty)
    public String name1; //name of player 1
    public String name2; //name of player 2 (only in pvp mode)

}
