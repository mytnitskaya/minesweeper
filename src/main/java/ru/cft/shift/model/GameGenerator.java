package ru.cft.shift.model;

import java.util.Random;

public class GameGenerator {

    private static void addTopAndLeftNeighboursForCell(Cell cell, Cell[][] cells, GameBoardParameters gameParams) {
        if (cell.x > 0 && cell.y > 0) {
            Cell cellLeftTop = cells[cell.x - 1][cell.y - 1];
            cell.neighbours.add(cellLeftTop);
            cellLeftTop.neighbours.add(cell);
        }
        if (cell.x > 0 && cell.y < gameParams.width - 1) {
            Cell cellRightTop = cells[cell.x - 1][cell.y + 1];
            cell.neighbours.add(cellRightTop);
            cellRightTop.neighbours.add(cell);
        }
        if (cell.x > 0) {
            Cell cellCentreTop = cells[cell.x - 1][cell.y];
            cell.neighbours.add(cellCentreTop);
            cellCentreTop.neighbours.add(cell);
        }
        if (cell.y > 0) {
            Cell cellLeftCentre = cells[cell.x][cell.y - 1];
            cell.neighbours.add(cellLeftCentre);
            cellLeftCentre.neighbours.add(cell);
        }
    }

    public static Cell[][] generateCells(GameBoardParameters gameParams) {
        Cell[][] cells = new Cell[gameParams.height][gameParams.width];

        for (int row = 0; row < gameParams.height; row++) {
            for (int column = 0; column < gameParams.width; column++) {
                cells[row][column] = new Cell(row, column);
                addTopAndLeftNeighboursForCell(cells[row][column], cells, gameParams);
            }
        }

        return cells;
    }

    public static void generateMines(Cell exceptCell, Cell[][] cells, int numberOfMines) {
        Random random = new Random();
        int currentNumberOfMines = 0;
        while (currentNumberOfMines < numberOfMines) {
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells[0].length);
            Cell cell = cells[x][y];
            if (cell != exceptCell && !cell.isMined) {
                cell.isMined = true;
                currentNumberOfMines++;
            }
        }
        updateNumberOfMinesAroundCells(cells);
    }

    private static void updateNumberOfMinesAroundCells(Cell[][] cells) {
        for (Cell[] cellRow: cells){
            for (Cell cell: cellRow){
                cell.updateNumberOfMines();
            }
        }
    }
}
