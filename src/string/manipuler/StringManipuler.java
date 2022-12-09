package string.manipuler;

public class StringManipuler {
    public static String repeat(String text, int times) {
        String result = "";

        for (int i = 0; i < times; i++) result += text;
        
        return result;
    }

    public static String separate(String text) {
        String newText = "";
        
        for (int j = 0; j < text.length(); j++) {
            char word = text.charAt(j);
            if (Character.isUpperCase(word)) {
                newText += " " + Character.toLowerCase(word);
            } else if (word == '_') {
                newText += " ";
            } else {
                newText += word;
            }
        }

        return newText;
    }
}
