package test;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import dataStorer.EnvironmentStorage;

public class Test {
    public static void main(String[] args) {
        EnvironmentStorage storage = new EnvironmentStorage();

        test("data creation", () ->  {
            for (int i = 0; i < 48; i++) {
                var temp = Math.random() * 100000;
                var carb = Math.random() * 10000;
                var humi = Math.random() * 1000;

                storage.createRegisty((float) temp, (float) carb, (float) humi, Calendar.getInstance().getTime());
            }
        });

        test("sort by temperature", () -> {
            storage.sortByTemperature();
        });

        test("sort by carbon dioxide", () -> {
            storage.sortByCarbonDioxideQnt();
        });

        test("get table", () -> {
            System.out.println(storage.toTable());
        });
    }
    
    public static void test(String name, VoidReturnTestCallback testCallback) {
        test(name, null, () -> {
            testCallback.call();
            return null;
        });
    }

    public static <T> void test(String name, T expect, TestCallback<T> testCallback) {
        boolean withError, passed;
        long duration;
        Instant init, end;

        T result = null;
        Exception testException = null;
        String message = "";

        init = Instant.now();

        try {
            result = testCallback.call();
        } catch (Exception exception) {
            testException = exception;
        }

        end = Instant.now();

        duration = Date.from(end).getTime() - Date.from(init).getTime();

        message += line;
        message += apresentationString.formatted(name);
        message += durationString.formatted(duration);

        withError = testException != null;

        if (withError) {
            message += errorString.formatted(testException);

            for (StackTraceElement trace : testException.getStackTrace())
                message += stackTraceString.formatted(trace.toString());
        }
        
        passed = withError? false: expect == null? result == null: expect.equals(result);

        message += checkingString.formatted(passed);
        message += line;

        System.out.print(message);
    }

    private static final String line =                "------------------------------\n";
    private static final String apresentationString = "> [Name]     : \"%s\"\n";
    private static final String durationString =      "> [Duration] : %sms\n";
    private static final String errorString =         "> [Error]    : %s\n";
    private static final String stackTraceString =    "> > [Stack]  : %s\n";
    private static final String checkingString =      "> [Passed]   : %s\n";
}
