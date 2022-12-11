package main;

import java.util.Calendar;

import dataStorer.EnvironmentStorage;

public class Main {
    public static void main(String[] args) {
        // Isso é apenas um código base.
        EnvironmentStorage storage = new EnvironmentStorage();

        // Numero de registros a serem criados.
        final int NUM_OF_REGISTERS = 48;

        // Cria N registros aleatórios.
        for (int i = 0; i < NUM_OF_REGISTERS; i++) {
            var temp = Math.random() * 100;
            var carb = Math.random() * 100;
            var humi = Math.random() * 100;

            storage.createRegistry((float) temp, (float) carb, (float) humi, Calendar.getInstance().getTime());
        }

        // Ordena os registros pela temperatura.
        storage.sortByTemperature();

        // Lista de campos permitidos.
        final String[] ALLOWED = {
            "temperature",
            // "carbonDioxideQnt",
            // "humidity",
            "time",
        };

        // Variaveis de inicio e fim da tabela.
        final int START = 0;
        final int END = -1;

        // Gera a String em formato de tabela.
        String formattedTable = storage.toString(START, END, ALLOWED);

        // Exibe os dados na tela.
        System.out.println(formattedTable);
    }
}