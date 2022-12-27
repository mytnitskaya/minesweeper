package ru.cft.shift.model;

public class GameBoardParameters {

    public GameBoardParameters(int width, int height, int numberOfMines){
        this.width = width;
        this.height = height;
        this.numberOfMines = numberOfMines;
    }

    public int width;
    public int height;
    public int numberOfMines;


}
