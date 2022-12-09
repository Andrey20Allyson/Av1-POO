package dataStorer;

import java.lang.reflect.Field;
import java.util.Date;

import string.manipuler.StringManipuler;

public class EnvironmentStorage {
    public static final int MAX_NUM_OF_REGISTERS = 48;
    private EnvironmentRegistry[] registres;
    private int length;

    public EnvironmentStorage() {
        this.registres = new EnvironmentRegistry[EnvironmentStorage.MAX_NUM_OF_REGISTERS];
        this.length = 0;
    }
    
    public EnvironmentRegistry pop() {
        return this.pop(-1);
    }

    public EnvironmentRegistry pop(int index) {
        index = parseToIndex(index);
        if (index == -1)
            return null;

        EnvironmentRegistry registry = this.registres[index];

        this.registres[index] = null;
        --this.length;

        for (int i = index; i < this.length; i++) {
            this.registres[i] = this.registres[i + 1]; 
        }

        return registry;
    }

    public void set(int index, EnvironmentRegistry value) {
        index = parseToIndex(index);
        if (index == -1)
            return;

        this.registres[index] = value;
    }

    public EnvironmentRegistry get(int index) {
        index = this.parseToIndex(index);
        if (index == -1)
            return null;

        return this.registres[index];
    }

    private int parseToIndex(int value) {
        int index = value < 0? this.length + value: value;
        return index > this.length - 1 || index < 0? -1: index; 
    }

    public void push(EnvironmentRegistry registry) {
        this.registres[this.length] = registry;
        this.length++;
    }
    
    public void push(float temperature, float carbonOxideQnt, float humidity, Date time) {
        this.push(new EnvironmentRegistry(temperature, carbonOxideQnt, humidity, time));
    }

    public EnvironmentRegistry createRegisty(float temperature, float carbonOxideQnt, float humidity, Date time) {
        EnvironmentRegistry registry = new EnvironmentRegistry(temperature, carbonOxideQnt, humidity, time);

        this.push(registry);

        return registry;
    }

    public void sortByCarbonDioxideQnt() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.carbonDioxideQnt > rightValue.carbonDioxideQnt);
    }

    public void sortByTemperature() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.temperature > rightValue.temperature);
    }

    public void sortByHumidity() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.humidity < rightValue.humidity);
    }

    public void bubbleSort(SortCallback<EnvironmentRegistry> callback) {
        boolean isSorted;

        for (int i = this.length - 1; i > 0; i--) {
            isSorted = true;
            for (int j = 0; j < i; j++) {
                var leftVal = this.registres[j];
                var rightVal = this.registres[j + 1];

                if (callback.call(leftVal, rightVal)) {
                    this.registres[j] = rightVal;
                    this.registres[j + 1] = leftVal;
                    isSorted = false;
                }
            }
            if (isSorted) break;
        }
    }

    public String toTable() {
        try {
            Field[] fields = EnvironmentRegistry.class.getDeclaredFields();

            String result = "";
            String base = "[ index: %s%s\n";
            String indexText = "";
            String insert = " ] --> | ";
            String[] fieldNames = new String[fields.length];

            int indexMaxLen = Integer.toString(this.length).length();
            System.out.println(indexMaxLen);
            int[] maxLengths = new int[fields.length];

            for (int i = 0; i < fieldNames.length; i++)
                fieldNames[i] = StringManipuler.separate(fields[i].getName());

            for (int i = 0; i < maxLengths.length; i++)
                maxLengths[i] = fieldNames[i].length();

            for (int i = 0; i < maxLengths.length; i++) {
                for (int j = 0; j < this.length; j++) {
                    Object value = fields[i].get(this.registres[j]);
                    int len;
                    if (value instanceof Float) {
                        len = String.format("%.2f", value).length();
                    } else {
                        len = value.toString().length();
                    }
                    
                    if (maxLengths[i] < len)
                        maxLengths[i] = len;
                }
            }

            for (int i = 0; i < fields.length; i++) {
                String name = fieldNames[i];
                insert += name + StringManipuler.repeat(" ", maxLengths[i] - name.length());

                indexText = "x";
                indexText += StringManipuler.repeat(" ", indexMaxLen - indexText.length());

                insert += " | ";
            }

            result += base.formatted(indexText, insert);

            for (int i = 0; i < this.length; i++) {
                insert = " ] --> | ";

                for (int j = 0; j < fields.length; j++) {
                    Object value = fields[j].get(this.registres[i]);
                    String text;

                    if (value instanceof Float) {
                        text = String.format("%.2f", value);
                    } else {
                        text = value.toString();
                    }
                    
                    insert += text + StringManipuler.repeat(" ", maxLengths[j] - text.length());

                    indexText = Integer.toString(i); 
                    indexText += StringManipuler.repeat(" ", indexMaxLen - indexText.length());

                    insert += " | ";
                }

                result += base.formatted(indexText, insert);
            }

            return result;

        } catch (Exception e) {
            System.out.println(e.getMessage());

            for (var trace : e.getStackTrace())
                System.out.println(trace);
            
            return null;
        }
    }
}
