package ru.cft.shift.controller;

import ru.cft.shift.model.Record;
import ru.cft.shift.model.*;
import ru.cft.shift.view.*;

public class GameController implements GameTypeListener, CellEventListener,RecordNameListener, RecordListener, GameInteractionListener {

    private final MainWindow mainView;
    private final Game gameModel;


    public GameController(MainWindow mainView, Game gameModel) {
        this.mainView = mainView;
        this.gameModel = gameModel;
        this.gameModel.setGameInteractionListener(this);
        this.gameModel.recordWriter.setListener(this);
        initGameType(this.gameModel.defaultGameType);

    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        initGameType(gameType);
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        var cell = this.gameModel.getCell(x, y);
        switch (buttonType) {
            case LEFT_BUTTON -> this.gameModel.handleClickCell(cell);
            case RIGHT_BUTTON -> this.gameModel.switchFlagCell(cell);
            case MIDDLE_BUTTON -> this.gameModel.tryOpenNeighbourCells(cell);
        }

    }

    @Override
    public void onSetCellImage(int x, int y, GameImage image) {
        this.mainView.setCellImage(x, y, image);
    }

    @Override
    public void onLose() {
        var loseView = new LoseWindow(mainView);
        loseView.setNewGameListener(e -> resetGame());
        loseView.setExitListener(e -> mainView.dispose());
        loseView.setVisible(true);
    }

    @Override
    public void onWin() {
        var winView = new WinWindow(mainView);
        winView.setNewGameListener(e -> resetGame());
        winView.setExitListener(e -> mainView.dispose());
        winView.setVisible(true);
    }

    @Override
    public void onSetNumberOfMines(int bombCount) {
        this.mainView.setBombsCount(bombCount);
    }

    @Override
    public void onSetTimerValue(int timeSeconds) {
        this.mainView.setTimerValue(timeSeconds);
    }

    @Override
    public void onRecordNameEntered(String name) {
        gameModel.recordWriter.writeRecord(name);
    }

    @Override
    public void requestName() {
        var recordsView = new RecordsWindow(mainView);
        recordsView.setNameListener(this);
        recordsView.setVisible(true);
    }


    public void resetGame() {
        initGameType(this.gameModel.currentGameType);
    }

    public void openSettingsWindow(SettingsWindow settingsView) {
        settingsView.setGameType(this.gameModel.currentGameType);
        settingsView.setVisible(true);
    }

    public void openHighScoresWindow(HighScoresWindow highScoresView){
        var records = gameModel.recordWriter.getAllRecords();
        for (Record record : records){
            switch (record.gameType){
                case NOVICE -> highScoresView.setNoviceRecord(record.name, record.recordTime);
                case MEDIUM -> highScoresView.setMediumRecord(record.name, record.recordTime);
                case EXPERT -> highScoresView.setExpertRecord(record.name, record.recordTime);
            }
        }
        highScoresView.setVisible(true);
    }

    private void initGameType(GameType gameType) {
        GameBoardParameters gameParams = this.gameModel.setCurrentGameType(gameType);
        this.mainView.createGameField(gameParams.height, gameParams.width);
        this.gameModel.resetGame();
    }
}
