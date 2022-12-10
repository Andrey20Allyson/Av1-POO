package test;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import dataStorer.EnvironmentRegistry;
import dataStorer.EnvironmentStorage;

public class Test {
    public static void main(String[] args) {
        EnvironmentStorage localStorage = new EnvironmentStorage();

        test("data creation", () ->  {
            for (int i = 0; i < 48; i++) {
                var temp = Math.random() * 100;
                var carb = Math.random() * 100;
                var humi = Math.random() * 100;

                localStorage.createRegistry((float) temp, (float) carb, (float) humi, Calendar.getInstance().getTime());
            }
        });

        test("sort by temperature", 5f, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            storage.createRegistry(10, 0, 0, null);
            storage.createRegistry(5, 0, 0, null);

            storage.sortByTemperature();

            return storage.get(0).temperature;
        });

        test("sort by carbon dioxide", 5f, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            storage.createRegistry(0, 10, 0, null);
            storage.createRegistry(0, 5, 0, null);

            storage.sortByCarbonDioxideQnt();

            return storage.get(0).carbonDioxideQnt;
        });

        test("sort by humidity", 50f, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            storage.createRegistry(0, 0, 10, null);
            storage.createRegistry(0, 0, 50, null);
            storage.createRegistry(0, 0, 30, null);

            storage.sortByHumidity();

            return storage.get(0).humidity;
        });

        test("push() shold put item in the last index", true, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            var registry0 = new EnvironmentRegistry(0, 0, 10, null);
            var registry1 = new EnvironmentRegistry(0, 0, 50, null);
            var registry2 = new EnvironmentRegistry(0, 0, 30, null);

            storage.push(registry0);
            storage.push(registry1);
            storage.push(registry2);

            return storage.get(-1) == registry2;
        });

        test("push() shold return the index of the inserted item", 1, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            var registry0 = new EnvironmentRegistry(0, 0, 10, null);
            var registry1 = new EnvironmentRegistry(0, 0, 50, null);
            int index;

            storage.push(registry0);
            index = storage.push(registry1);

            return index;
        });

        test("pop() shold remove the last item of the list", true, () -> {
            EnvironmentStorage storage = new EnvironmentStorage();

            var registry0 = new EnvironmentRegistry(0, 0, 10, null);
            var registry1 = new EnvironmentRegistry(0, 0, 50, null);
            var registry2 = new EnvironmentRegistry(0, 0, 30, null);

            storage.push(registry0);
            storage.push(registry1);
            storage.push(registry2);

            var removedRegistry = storage.pop();

            return removedRegistry == registry2;
        });

        test("test toString", () -> {
            localStorage.toString();
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

        withError = testException != null;

        passed = withError? false: expect == null? result == null: expect.equals(result);

        message += testResult.formatted(passed ? "Passed": "Failed", name, duration);

        if (withError) {
            message += errorString.formatted(testException);

            for (StackTraceElement trace : testException.getStackTrace())
                message += stackTraceString.formatted(trace.toString());
        }

        System.out.print(message);
    }

    private static final String testResult =          "> [%s] -> \"%s\" finished in %sms\n";
    private static final String errorString =         "> [Error]   %s\n";
    private static final String stackTraceString =    "> |-[Stack] %s\n";
}
