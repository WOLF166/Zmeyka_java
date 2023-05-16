package com.wolf.zmeyka.Model;

public class Record {
    int id, record;
    String name;

    public Record(int id, int record, String name) {
        this.id = id;
        this.record = record;
        this.name = name;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
