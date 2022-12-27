package ru.cft.shift.app;

import ru.cft.shift.controller.GameController;
import ru.cft.shift.model.Game;
import ru.cft.shift.view.HighScoresWindow;
import ru.cft.shift.view.MainWindow;
import ru.cft.shift.view.SettingsWindow;

public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        Game gameModel = new Game();
        GameController gameController = new GameController(mainWindow, gameModel);
        settingsWindow.setGameTypeListener(gameController);

        mainWindow.setNewGameMenuAction(e -> gameController.resetGame());
        mainWindow.setSettingsMenuAction(e -> gameController.openSettingsWindow(settingsWindow));
        mainWindow.setHighScoresMenuAction(e -> gameController.openHighScoresWindow(highScoresWindow));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        mainWindow.setCellListener(gameController);


        mainWindow.setVisible(true);

    }
}
