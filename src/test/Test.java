package test;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import dataStorer.EnvironmentStorage;

public class Test {
    public static void main(String[] args) {
        EnvironmentStorage storage = new EnvironmentStorage();

        test("data creation", null, () -> {

            
            for (int i = 0; i < 48; i++) {
                var temp = Math.random() * 100000;
                var carb = Math.random() * 10000;
                var humi = Math.random() * 1000;

                storage.createRegisty((float) temp, (float) carb, (float) humi, Calendar.getInstance().getTime());
            }

            return null;
        });

        test("sort by temperature", null, () -> {
            storage.sortByTemperature();
            
            return null;
        });

        test("get table", null, () -> {
            System.out.println(storage.toTable());

            return null;
        });
    }

    public static void test(String name, Object expect, TestCallback testCallback) {
        var init = Instant.now();

        var result = testCallback.call();

        var end = Instant.now();
        System.out.println("\"%s\" has finished in: %sms".formatted(name, Date.from(end).getTime() - Date.from(init).getTime()));
        if (expect != null) {
            System.out.println("passed = %s".formatted(expect.equals(result)));
        }
    }
}
