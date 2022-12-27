package ru.cft.shift.model;

import java.util.ArrayList;

public class Cell {

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int x;
    public int y;
    public boolean isFlagged;
    public boolean isMined;
    public boolean isOpened;
    public int numberOfMinesAround;
    public ArrayList<Cell> neighbours = new ArrayList<>();

    public void updateNumberOfMines(){
        numberOfMinesAround = (int)neighbours.stream().filter(x -> x.isMined).count();
    }

}
