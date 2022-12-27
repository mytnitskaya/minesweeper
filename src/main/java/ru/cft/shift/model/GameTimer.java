package ru.cft.shift.model;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    final private int DELAY = 0;
    final private int PERIOD = 1000;
    private Timer timer;
    private GameInteractionListener gameInteractionListener;

    public long startTime;
    public int currentTimerSeconds;


    public void startTimer() {
        if (gameInteractionListener == null) {
            return;
        }
        timer = new Timer();
        startTime = new Date().getTime();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTimerSeconds = (int) (new Date().getTime() - startTime) / 1000;
                gameInteractionListener.onSetTimerValue(currentTimerSeconds);
            }
        };
        timer.schedule(timerTask, DELAY, PERIOD);

    }

    public void stopTimer() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public void resetTimer(){
        stopTimer();
        currentTimerSeconds = 0;

        if (gameInteractionListener != null) {
            gameInteractionListener.onSetTimerValue(0);
        }
    }

    public void setGameInteractionListener(GameInteractionListener gameInteractionListener) {
        this.gameInteractionListener = gameInteractionListener;
    }
}
