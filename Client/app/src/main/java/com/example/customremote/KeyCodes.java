package com.example.customremote;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyCodes {
    public static Map<String,Integer> genSpecialChars(){
        Map<String, Integer> specialChars = new HashMap<>();
        specialChars.put("Shift",16);
        specialChars.put("Ctrl",17);
        specialChars.put("CapsLock",20);
        specialChars.put("NumLock",144);
        specialChars.put("Alt",18);
        specialChars.put("Backspace",8);
        specialChars.put("Enter",13);
        specialChars.put("Windows",91);
        specialChars.put("Delete",46);
        specialChars.put("PgUp",33);
        specialChars.put("PgDn",34);
        specialChars.put("Home",36);
        specialChars.put("End",35);
        specialChars.put("LeftArrow",37);
        specialChars.put("UpArrow",38);
        specialChars.put("RightArrow",39);
        specialChars.put("DownArrow",40);
        specialChars.put("F1",112);
        specialChars.put("F2",113);
        specialChars.put("F3",114);
        specialChars.put("F4",115);
        specialChars.put("F5",116);
        specialChars.put("F6",117);
        specialChars.put("F7",118);
        specialChars.put("F8",119);
        specialChars.put("F9",120);
        specialChars.put("F10",121);
        specialChars.put("F11",122);
        specialChars.put("F12",123);
        return specialChars;
    }
    public static ArrayList<String> genSpecialCharsArray(){
        ArrayList<String> specialCharsArray = new ArrayList<>();
        specialCharsArray.add("Shift");
        specialCharsArray.add("Ctrl");
        specialCharsArray.add("CapsLock");
        specialCharsArray.add("NumLock");
        specialCharsArray.add("Alt");
        specialCharsArray.add("Backspace");
        specialCharsArray.add("Enter");
        specialCharsArray.add("Windows");
        specialCharsArray.add("Delete");
        specialCharsArray.add("PgUp");
        specialCharsArray.add("PgDn");
        specialCharsArray.add("Home");
        specialCharsArray.add("End");
        specialCharsArray.add("LeftArrow");
        specialCharsArray.add("UpArrow");
        specialCharsArray.add("RightArrow");
        specialCharsArray.add("DownArrow");
        specialCharsArray.add("F1");
        specialCharsArray.add("F2");
        specialCharsArray.add("F3");
        specialCharsArray.add("F4");
        specialCharsArray.add("F5");
        specialCharsArray.add("F6");
        specialCharsArray.add("F7");
        specialCharsArray.add("F8");
        specialCharsArray.add("F9");
        specialCharsArray.add("F10");
        specialCharsArray.add("F11");
        specialCharsArray.add("F12");
        return specialCharsArray;
    }
    public static int codeOf(String a){
        return genSpecialChars().get(a);
    }
    public static String valueAt(int x){
        return genSpecialCharsArray().get(x);
    }
}
