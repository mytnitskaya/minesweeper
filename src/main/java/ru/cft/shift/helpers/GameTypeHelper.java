package ru.cft.shift.helpers;

import ru.cft.shift.model.GameBoardParameters;
import ru.cft.shift.view.GameType;

public class GameTypeHelper {

    public static GameBoardParameters getBoardParameters(GameType gameType) {
        switch (gameType) {
            case NOVICE -> {
                return new GameBoardParameters(9,9,10);
            }
            case MEDIUM -> {
                return new GameBoardParameters(16,16,40);
            }
            case EXPERT -> {
                return new GameBoardParameters(30,16,99);
            }
            default -> {
                return null;
            }
        }
    }



}
