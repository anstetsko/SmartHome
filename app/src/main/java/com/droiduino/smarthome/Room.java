package com.droiduino.smarthome;

public class Room {
    private String pin;
    private String name;
    private boolean checked;

    Room(String pin, String name, Boolean checked){
        this.pin = pin;
        this.name = name;
        this.checked = checked;
    }

    @Override
    public String toString(){
        return this.name;
    }
    public String getPin() {
        return this.pin;
    }

    public String getName() {
        return this.name;
    }

    public Boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}