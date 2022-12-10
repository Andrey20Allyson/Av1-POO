package dataStorer;

import java.lang.reflect.Field;
import java.util.Date;

import string.manipuler.StringManipuler;

public class EnvironmentStorage {
    public static final int MAX_NUM_OF_REGISTERS = 48;
    private EnvironmentRegistry[] registres;
    private int length;

    /**
     * Uma classe que permite o amazenamento de EnvironmentRegistry de uma forma simples e prática.
     */
    public EnvironmentStorage() {
        this.registres = new EnvironmentRegistry[EnvironmentStorage.MAX_NUM_OF_REGISTERS];
        this.length = 0;
    }
    
    /**
     * Remove o ultimo valor da lista de registros.
     * 
     * @return o valor removido.
     */
    public EnvironmentRegistry pop() {
        return this.pop(-1);
    }

    /**
     * Remove o valor presente no indice, e diminui o tamanho da lista.
     * 
     * @param index - o índice cujo valor será removido.
     * @return o valor removido.
     */
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

    /**
     * Altera o valor da lista de registros presente no indice.
     * 
     * @param index - o índice onde o valor será alterado.
     * @param value - o novo valor a ser inserido.
     */
    public void set(int index, EnvironmentRegistry value) {
        index = parseToIndex(index);
        if (index == -1)
            return;

        this.registres[index] = value;
    }

    /**
     * Retorna o registro que se localiza no índice informado. 
     * 
     * @param index - índice do registro, se for menor que 0 o índice percorre a lista de trás para frente.
     * @return o registro localizado no índice.
     */
    public EnvironmentRegistry get(int index) {
        index = this.parseToIndex(index);
        if (index == -1)
            return null;

        return this.registres[index];
    }

    /**
     * Transforma um inteiro de qualquer valor em um inteiro cuja variação é de [-1, N - 1], sendo N o tamanho da lista de registros.
     * 
     * @param value - valor inteiro a ser tranformado.
     * @return um inteiro maior ou igual a -1, retorna -1 caso o valor passado não possa ser utilizado como índice.
     */
    protected int parseToIndex(int value) {
        int index = value < 0? this.length + value: value;
        return index > this.length - 1 || index < 0? -1: index; 
    }

    /**
     * Insere o registro passado na lista de registro e retorna o índice do mesmo.
     * 
     * @param registry - o registro a ser inserido.
     * @return o índice do item inserido.
     */
    public int push(EnvironmentRegistry registry) {
        this.registres[this.length] = registry;
        return this.length++;
    }
    
    /**
     * Instancia um novo registro com base nos argumentos passados, o armazena e retorna o seu índice logo após.
     * 
     * @param temperature - temperatura do ambiente.
     * @param carbonOxideQnt - quantidade de gás carbonico.
     * @param humidity - humidade.
     * @param time - quando a coleta foi feita.
     * @returns o índice do item inserido.
     */
    public int push(float temperature, float carbonOxideQnt, float humidity, Date time) {
        return this.push(new EnvironmentRegistry(temperature, carbonOxideQnt, humidity, time));
    }

    /**
     * Método fábrica que cria um novo registro, o armazena e o retorna logo após.
     * 
     * @param temperature - temperatura do ambiente.
     * @param carbonDioxideQnt - quantidade de gás carbonico.
     * @param humidity - humidade.
     * @param time - quando a coleta foi feita.
     * @return um novo registro.
     */
    public EnvironmentRegistry createRegistry(float temperature, float carbonDioxideQnt, float humidity, Date time) {
        EnvironmentRegistry registry = new EnvironmentRegistry(temperature, carbonDioxideQnt, humidity, time);

        this.push(registry);

        return registry;
    }

    /**
     * Ordena a lista de registros de forma crescente de acordo com a propriedade <strong>carbonDioxideQnt</strong> do registro.
     */
    public void sortByCarbonDioxideQnt() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.carbonDioxideQnt > rightValue.carbonDioxideQnt);
    }

    /**
     * Ordena a lista de registros de forma crescente de acordo com a propriedade <strong>temperature</strong> do registro.
     */
    public void sortByTemperature() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.temperature > rightValue.temperature);
    }

    /**
     * Ordena a lista de registros de forma decrescente de acordo com a propriedade <strong>humidity</strong> do registro.
     */
    public void sortByHumidity() {
        this.bubbleSort((leftValue, rightValue) -> leftValue.humidity < rightValue.humidity);
    }

    /**
     * Ordena a lista de registros de acordo com o retorno do callback utilizando o algoritmo Bubble Sort.
     * 
     * @param callback
     */
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

    @Override
    public String toString() {
        try {
            Field[] fields = EnvironmentRegistry.class.getDeclaredFields();

            String result = "";
            String base = "[ index: %s%s\n";
            String indexText = "";
            String insert = " ] --> | ";
            String[] fieldNames = new String[fields.length];
            String[][] valueTexts = new String[this.length][fields.length];

            int indexMaxLen = Integer.toString(this.length).length();
            int[] maxLengths = new int[fields.length];

            for (int i = 0; i < fieldNames.length; i++)
                fieldNames[i] = StringManipuler.separate(fields[i].getName());

            for (int i = 0; i < maxLengths.length; i++)
                maxLengths[i] = fieldNames[i].length();

            for (int i = 0; i < maxLengths.length; i++) {
                for (int j = 0; j < this.length; j++) {
                    Object value = fields[i].get(this.registres[j]);

                    String valueText = value instanceof Float? String.format("%.2f", value): value.toString();

                    int len = valueText.length();

                    valueTexts[j][i] = valueText;
                    
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
                    String text = valueTexts[i][j];
                    
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
