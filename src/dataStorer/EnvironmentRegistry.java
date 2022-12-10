package dataStorer;

import java.util.Date;

public class EnvironmentRegistry {
    public float temperature;
    public float carbonDioxideQnt;
    public float humidity;
    public Date time;

    public EnvironmentRegistry(float temperature, float carbonDioxideQnt, float humidity, Date time) {
        this.temperature = temperature;
        this.carbonDioxideQnt = carbonDioxideQnt;
        this.humidity = humidity;
        this.time = time;
    }

    public String[] getKeys() {
        var fields = this.getClass().getDeclaredFields();

        var keys = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            keys[i] = fields[i].getName();
        }

        return keys;
    }

    public Object[] getValues() {
        try {
            var fields = this.getClass().getFields();

            var values = new Object[fields.length];

            for (int i = 0; i < fields.length; i++)
                values[i] = fields[i].get(this);

            return values;

        } catch (Exception e) {
            return null;
        }
    }

    public Object getValue(String key) {
        try {
            var field = this.getClass().getField(key);

            return field.get(this);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("temp: %.2f | CO2 quantity: %.2f | humidity: %.2f | registry time: %s", this.temperature, this.carbonDioxideQnt, this.humidity, this.time.toString());
    }
}
