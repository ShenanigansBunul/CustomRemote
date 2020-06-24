package com.example.customremote;

import java.util.ArrayList;

public class Remote {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<RemoteButton> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<RemoteButton> buttons) {
        this.buttons = buttons;
    }

    public void addButton(RemoteButton btn){ this.buttons.add(btn);}

    public Remote(String name, int w, int h){
        this.name = name;
        this.width = w;
        this.height = h;
    }

    private String name;
    private int width, height;
    private ArrayList<RemoteButton> buttons = new ArrayList<>();

    public ArrayList<String> toArrayList() {
        ArrayList<String> r = new ArrayList<>();
        r.add(name);
        r.add(String.valueOf(this.width));
        r.add(String.valueOf(this.height));
        return r;
    }
}
