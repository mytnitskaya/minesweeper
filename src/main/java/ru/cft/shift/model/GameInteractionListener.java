package ru.cft.shift.model;

import ru.cft.shift.view.GameImage;

public interface GameInteractionListener {
    void onSetCellImage(int x, int y, GameImage image);
    void onLose();
    void onWin();
    void onSetNumberOfMines(int bombCount);
    void onSetTimerValue(int timeSeconds);
}
