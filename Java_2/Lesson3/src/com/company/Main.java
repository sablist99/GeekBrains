package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Первое задание. \nСписок элементов:");

        ArrayList<String> list = new ArrayList < String >();
        list.add("кот");
        list.add("собака");
        list.add("слон");
        list.add("червь");
        list.add("утка");
        list.add("боров");
        list.add("гусеница");
        list.add("утка");
        list.add("слон");
        list.add("свинья");
        list.add("лось");
        list.add("слон");
        list.add("боров");
        list.add("червь");
        list.add("гусеница");
        list.add("медведь");
        list.add("слон");
        System.out.println(list);
        System.out.println("Сколько раз встречаются элементы:");

        HashMap< String , Integer > hm = new HashMap();
        for (String l : list) {
            if (hm.containsKey(l)) {
                hm.put(l, hm.get(l) + 1);
            } else {
                hm.put(l, 1);
            }
        }
        System.out.println(hm);

        System.out.println("\nВторое задание.");

        Phonebook pb = new Phonebook();

        pb.Add("123", "First");
        pb.Add("456", "Second");
        pb.Add("789", "Third");
        pb.Add("098", "Fourth");
        pb.Add("321", "Fifth");
        pb.Add("654", "Second");
        pb.Add("000", "Third");

        System.out.println("Содержание:");
        System.out.println(pb);

        System.out.println("Поиск First:\n" + pb.Get("First"));
        System.out.println("Поиск Second:\n" + pb.Get("Second"));
        System.out.println("Поиск Third:\n" + pb.Get("Third"));
        System.out.println("Поиск Fourth:\n" + pb.Get("Fourth"));
        System.out.println("Поиск Fifth:\n" + pb.Get("Fifth"));


    }
}
