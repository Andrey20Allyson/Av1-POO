package main;

import java.util.Calendar;

import dataStorer.EnvironmentStorage;

public class Main {
    public static void main(String[] args) {
        EnvironmentStorage storage = new EnvironmentStorage();

        for (int i = 0; i < 12; i++) {
            var temp = Math.random() * 100;
            var carb = Math.random() * 100;
            var humi = Math.random() * 100;

            storage.createRegistry((float) temp, (float) carb, (float) humi, Calendar.getInstance().getTime());
        }

        String formattedTable = storage.toString();

        System.out.println(formattedTable);
    }
}