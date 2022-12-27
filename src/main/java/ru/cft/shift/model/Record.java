package ru.cft.shift.model;

import ru.cft.shift.view.GameType;

public class Record {

    public Record(GameType gameType, int recordTime){
        this.gameType = gameType;
        this.recordTime = recordTime;
    }

    public Record(){

    }

    public GameType gameType;
    public String name;
    public int recordTime;
}
