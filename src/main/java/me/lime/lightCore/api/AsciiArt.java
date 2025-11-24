package me.lime.lightCore.api;

import java.util.HashMap;
import java.util.Map;

public class AsciiArt {
    
    private static final Map<Character, String[]> ASCII_LETTERS = new HashMap<>();
    
    static {
        // Initialize ASCII art for A-Z
        ASCII_LETTERS.put('A', new String[]{
            "    _    ",
            "   / \\   ",
            "  / _ \\  ",
            " / ___ \\ ",
            "/_/   \\_\\"
        });
        
        ASCII_LETTERS.put('B', new String[]{
            " ____  ",
            "|  _ \\ ",
            "| |_) |",
            "|  _ < ",
            "|_| \\_\\"
        });
        
        ASCII_LETTERS.put('C', new String[]{
            "  ____ ",
            " / ___|",
            "| |    ",
            "| |___ ",
            " \\____|"
        });
        
        ASCII_LETTERS.put('D', new String[]{
            " ____  ",
            "|  _ \\ ",
            "| | | |",
            "| |_| |",
            "|____/ "
        });
        
        ASCII_LETTERS.put('E', new String[]{
            " _____ ",
            "| ____|",
            "|  _|  ",
            "| |___ ",
            "|_____|"
        });
        
        ASCII_LETTERS.put('F', new String[]{
            " _____ ",
            "|  ___|",
            "| |_   ",
            "|  _|  ",
            "|_|    "
        });
        
        ASCII_LETTERS.put('G', new String[]{
            "  ____ ",
            " / ___|",
            "| |  _ ",
            "| |_| |",
            " \\____|"
        });
        
        ASCII_LETTERS.put('H', new String[]{
            " _   _ ",
            "| | | |",
            "| |_| |",
            "|  _  |",
            "|_| |_|"
        });
        
        ASCII_LETTERS.put('I', new String[]{
            " ___ ",
            "|_ _|",
            " | | ",
            " | | ",
            "|___|"
        });
        
        ASCII_LETTERS.put('J', new String[]{
            "     _ ",
            "    | |",
            " _  | |",
            "| |_| |",
            " \\___/ "
        });
        
        ASCII_LETTERS.put('K', new String[]{
            " _  __",
            "| |/ /",
            "| ' / ",
            "| . \\ ",
            "|_|\\_\\"
        });
        
        ASCII_LETTERS.put('L', new String[]{
            " _     ",
            "| |    ",
            "| |    ",
            "| |___ ",
            "|_____|"
        });
        
        ASCII_LETTERS.put('M', new String[]{
            " __  __ ",
            "|  \\/  |",
            "| |\\/| |",
            "| |  | |",
            "|_|  |_|"
        });
        
        ASCII_LETTERS.put('N', new String[]{
            " _   _ ",
            "| \\ | |",
            "|  \\| |",
            "| |\\  |",
            "|_| \\_|"
        });
        
        ASCII_LETTERS.put('O', new String[]{
            "  ___  ",
            " / _ \\ ",
            "| | | |",
            "| |_| |",
            " \\___/ "
        });
        
        ASCII_LETTERS.put('P', new String[]{
            " ____  ",
            "|  _ \\ ",
            "| |_) |",
            "|  __/ ",
            "|_|    "
        });
        
        ASCII_LETTERS.put('Q', new String[]{
            "  ___  ",
            " / _ \\ ",
            "| | | |",
            "| |_| |",
            " \\__\\_\\"
        });
        
        ASCII_LETTERS.put('R', new String[]{
            " ____  ",
            "|  _ \\ ",
            "| |_) |",
            "|  _ < ",
            "|_| \\_\\"
        });
        
        ASCII_LETTERS.put('S', new String[]{
            " ____  ",
            "/ ___| ",
            "\\___ \\ ",
            " ___) |",
            "|____/ "
        });
        
        ASCII_LETTERS.put('T', new String[]{
            " _____ ",
            "|_   _|",
            "  | |  ",
            "  | |  ",
            "  |_|  "
        });
        
        ASCII_LETTERS.put('U', new String[]{
            " _   _ ",
            "| | | |",
            "| | | |",
            "| |_| |",
            " \\___/ "
        });
        
        ASCII_LETTERS.put('V', new String[]{
            "__     __",
            "\\ \\   / /",
            " \\ \\ / / ",
            "  \\ V /  ",
            "   \\_/   "
        });
        
        ASCII_LETTERS.put('W', new String[]{
            "__        __",
            "\\ \\      / /",
            " \\ \\ /\\ / / ",
            "  \\ V  V /  ",
            "   \\_/\\_/   "
        });
        
        ASCII_LETTERS.put('X', new String[]{
            "__  __",
            "\\ \\/ /",
            " \\  / ",
            " /  \\ ",
            "/_/\\_\\"
        });
        
        ASCII_LETTERS.put('Y', new String[]{
            "__   __",
            "\\ \\ / /",
            " \\ V / ",
            "  | |  ",
            "  |_|  "
        });
        
        ASCII_LETTERS.put('Z', new String[]{
            " _____",
            "|__  /",
            "  / / ",
            " / /_ ",
            "/____|"
        });
        
        // Space character
        ASCII_LETTERS.put(' ', new String[]{
            "   ",
            "   ",
            "   ",
            "   ",
            "   "
        });
    }
    
    /**
     * Generate ASCII art from text (supports A-Z and spaces)
     * @param text The text to convert to ASCII art
     * @return Array of strings representing ASCII art lines
     */
    public static String[] generate(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        
        text = text.toUpperCase();
        String[] result = new String[]{"", "", "", "", ""};
        
        for (char c : text.toCharArray()) {
            String[] letter = ASCII_LETTERS.get(c);
            
            if (letter == null) {
                // Skip unsupported characters
                continue;
            }
            
            for (int i = 0; i < 5; i++) {
                result[i] += letter[i];
            }
        }
        
        return result;
    }
    
    /**
     * Generate ASCII art and return as a single string with newlines
     * @param text The text to convert to ASCII art
     * @return ASCII art as a single string
     */
    public static String generateString(String text) {
        String[] lines = generate(text);
        return String.join("\n", lines);
    }
}
