package ru.cft.shift.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.shift.view.GameType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RecordWriter {
    private final File recordFile = getRecordFile();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RecordListener recordListener;
    private Record pendingToSaveRecord;


    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        try {
            records = !recordFile.exists() ? records : objectMapper.readValue(recordFile, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public void checkRecord(GameType gameType, int timeRecord) {
        var records = getAllRecords();
        Record currentLevelRecord = records.stream().filter(x -> x.gameType == gameType).findFirst().orElse(null);

        var isRecord = currentLevelRecord == null ? true : currentLevelRecord.recordTime > timeRecord;

        if (isRecord) {
            pendingToSaveRecord = new Record(gameType, timeRecord);
            requestName();
        }
    }

    public void writeRecord(String name) {
        pendingToSaveRecord.name = name;
        try {
            var records = getAllRecords();
            Record currentLevelRecord = records.stream().filter(x -> x.gameType == pendingToSaveRecord.gameType).findFirst().orElse(null);
            if (currentLevelRecord != null) {
                records.remove(currentLevelRecord);
            }
            records.add(pendingToSaveRecord);
            objectMapper.writeValue(recordFile, records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    private static File getRecordFile() {
        Properties prop = new Properties();
        File recordFile;
        try (InputStream propFile = new FileInputStream("src/main/java/ru/cft/shift/prop.properties")){
            prop.load(propFile);
            recordFile = new File(prop.getProperty("recordFile"));
            return recordFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void requestName() {
        if (recordListener != null) {
            recordListener.requestName();
        }
    }
}
