package ru.cft.shift.model;

import ru.cft.shift.helpers.GameTypeHelper;
import ru.cft.shift.view.GameImage;
import ru.cft.shift.view.GameType;


public class Game {

    final public GameType defaultGameType = GameType.NOVICE;
    private Cell[][] cells;
    private GameBoardParameters gameParams;
    private boolean isFirstMove = true;
    private GameInteractionListener gameInteractionListener;
    private int numberOfFlags;
    private int numberOfOpenedCells;
    private final GameTimer gameTimer = new GameTimer();


    public GameType currentGameType;
    public RecordWriter recordWriter = new RecordWriter();


    public GameBoardParameters setCurrentGameType(GameType gameType) {
        this.currentGameType = gameType;
        this.gameParams = GameTypeHelper.getBoardParameters(this.currentGameType);

        return this.gameParams;
    }

    public void resetGame() {
        gameTimer.resetTimer();
        isFirstMove = true;
        numberOfFlags = 0;
        numberOfOpenedCells = 0;
        updateLabelNumberOfMines();
        cells = GameGenerator.generateCells(this.gameParams);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void handleClickCell(Cell clickedCell) {
        if (isFirstMove) {
            GameGenerator.generateMines(clickedCell, cells, gameParams.numberOfMines);
            this.gameTimer.startTimer();
            isFirstMove = false;
        }
        openCell(clickedCell);
    }

    public void switchFlagCell(Cell cell) {
        if (cell.isOpened) {
            return;
        }

        if (!cell.isFlagged) {
            if (numberOfFlags >= gameParams.numberOfMines) {
                return;
            }
            numberOfFlags++;
        } else {
            numberOfFlags--;
        }

        cell.isFlagged = !cell.isFlagged;
        setCellImage(cell, cell.isFlagged ? GameImage.MARKED : GameImage.CLOSED);
        updateLabelNumberOfMines();
    }

    public void tryOpenNeighbourCells(Cell cell) {
        if (!cell.isOpened) {
            return;
        }
        int numberOfFlagsAround = (int) cell.neighbours.stream().filter(e -> e.isFlagged).count();
        if (numberOfFlagsAround == cell.numberOfMinesAround) {
            for (Cell neighbour : cell.neighbours) {
                openCell(neighbour);
            }
        }
    }

    public void setGameInteractionListener(GameInteractionListener gameInteractionListener) {
        this.gameInteractionListener = gameInteractionListener;
        gameTimer.setGameInteractionListener(gameInteractionListener);
    }


    private void openCell(Cell cell) {
        if (cell.isOpened || cell.isFlagged) {
            return;
        }
        cell.isOpened = true;

        if (cell.isMined) {
            setCellImage(cell, GameImage.BOMB);
            onLose();
            return;
        }

        setCellImage(cell, getImageNumberOfMinesAround(cell.numberOfMinesAround));
        numberOfOpenedCells++;
        if (cell.numberOfMinesAround == 0) {
            for (Cell neighbour : cell.neighbours) {
                if (neighbour.isFlagged){
                    switchFlagCell(neighbour);
                }
                openCell(neighbour);
            }
        }

        if (isAllCellsExceptMinesOpened()) {
            onWin();
        }
    }

    private void setCellImage(Cell cell, GameImage image) {
        if (gameInteractionListener != null) {
            gameInteractionListener.onSetCellImage(cell.x, cell.y, image);
        }
    }

    private GameImage getImageNumberOfMinesAround(int numberOfMinesAround) {
        switch (numberOfMinesAround) {
            case 0 -> {
                return GameImage.EMPTY;
            }
            case 1 -> {
                return GameImage.NUM_1;
            }
            case 2 -> {
                return GameImage.NUM_2;
            }
            case 3 -> {
                return GameImage.NUM_3;
            }
            case 4 -> {
                return GameImage.NUM_4;
            }
            case 5 -> {
                return GameImage.NUM_5;
            }
            case 6 -> {
                return GameImage.NUM_6;
            }
            case 7 -> {
                return GameImage.NUM_7;
            }
            case 8 -> {
                return GameImage.NUM_8;
            }
            default -> {
                return GameImage.CLOSED;
            }
        }
    }

    private void onLose() {
        openAllMines();
        gameTimer.stopTimer();
        if (gameInteractionListener != null) {
            gameInteractionListener.onLose();
        }
    }

    private void openAllMines() {
        for (Cell[] cellRow : cells) {
            for (Cell currentCell : cellRow) {
                if (!currentCell.isOpened && currentCell.isMined && !currentCell.isFlagged) {
                    setCellImage(currentCell, GameImage.BOMB_ICON);
                    currentCell.isOpened = true;
                    continue;
                }
                if (currentCell.isFlagged && !currentCell.isMined) {
                    setCellImage(currentCell, GameImage.MARKED_FALSE);
                    currentCell.isOpened = true;
                }
            }
        }
    }

    private void onWin() {
        gameTimer.stopTimer();
        recordWriter.checkRecord(currentGameType, gameTimer.currentTimerSeconds);
        if (gameInteractionListener != null) {
            gameInteractionListener.onWin();
        }
    }

    private boolean isAllCellsExceptMinesOpened() {
        int numberOfNotMinedCells = gameParams.height * gameParams.width - gameParams.numberOfMines;
        return numberOfNotMinedCells == numberOfOpenedCells;
    }

    private void updateLabelNumberOfMines() {
        if (gameInteractionListener != null) {
            gameInteractionListener.onSetNumberOfMines(getNumberOfMines());
        }
    }

    private int getNumberOfMines() {
        return gameParams.numberOfMines - numberOfFlags;
    }
}
