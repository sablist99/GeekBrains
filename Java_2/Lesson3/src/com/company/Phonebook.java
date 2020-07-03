package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Phonebook {
    HashMap <String, String> Number_Surname = new HashMap();

    public void Add(String number, String surname) {
        Number_Surname.put(number, surname);
    }

    public String Get(String surname) {
        String result = "";
        Set<Map.Entry<String, String>> set = Number_Surname.entrySet();
        for (Map.Entry<String, String> o : set)
            if (o.getValue() == surname)
                result += o.getValue() + " --> " + o.getKey() + "\n";
        if (result == "")
            return "Совпадений не найдено";
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        Set<Map.Entry<String, String>> set = Number_Surname.entrySet();
        for (Map.Entry<String, String> o : set)
            str += o.getValue() + " --> " + o.getKey() + "\n";
        return str;
    }
}
