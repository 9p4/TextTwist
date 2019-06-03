import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader; // File IO
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader; // More File IO
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; // ArrayList for storing file
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This class finds all of the permutations of a inputed string that are in the
 * dictionary, stores them in an ArrayList, and outputs these permutations as
 * keystrokes.
 *
 * @author Parth Patel and Sambhav Saggi
 * @version 2019-05-02
 */
class Main {
    // Set some static variables:
    static ArrayList<String> scrabble = new ArrayList<String>();
    static ArrayList<String> output = new ArrayList<String>();
    static String toCheck;

    /**
     * Open and read a file, and return the lines in the file as a list of Strings.
     * Stolen shamelessly from
     * https://alvinalexander.com/blog/post/java/how-open-read-file-java-string-array-list
     * 
     * @param filename The path to the file
     * @return Contents of the file, line by line
     */
    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> records = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line.substring(0, line.length()));
            }
            reader.close();
            return records;
        } catch (IOException e) {
            System.err.format("IOException occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Open and wrie to a file, clobbering it in the process
     * 
     * @param filename The path to the file
     * @param data     The arraylist of lines to write
     */
    public static void writeFile(String filename, ArrayList<String> data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            String newdata = "";
            for (int i = 0; i < data.size(); i++) {
                newdata = data.get(i) + "\n";
                writer.write(newdata);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error finding file");
            e.printStackTrace();
        }
    }

    /**
     * Starts the permutations without a prefix
     * @param str String to find permuations of
     */
    public static void permutation(String str) {
        permutation("", str);
    }

    /**
     * Recursion of the permuatations with a prefix
     * @param prefix The prefix of the permuatation
     * @param str The rest of the undone letters
     */
    private static void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) {
            if (binarySearch(scrabble, prefix) > -1) {
                wordIt(prefix);
            }
        } else {
            for (int i = 0; i < n; i++) {
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
            }
        }
    }

    /**
     * Searches a sorted arraylist for an item
     * @param items Sorted arraylist of items to look through
     * @param toFind String to find
     * @return Position of item in list
     */
    public static int binarySearch(ArrayList<String> items, String toFind) {
        int isThere = -1;
        int start = 0;
        int end = items.size();
        int center = (start + end) / 2;
        String cv;
        while (isThere == -1) {
            center = (start + end) / 2;
            cv = items.get(center);
            if (center == 0) {
                return -1;
            } else if ((items.get(center - 1).compareTo(toFind) < 0) && (cv.compareTo(toFind) > 0)) {
                return -1;
            } else if (cv.compareTo(toFind) < 0) {
                start = center;
            } else if (cv.compareTo(toFind) > 0) {
                end = center;
            } else if (cv.compareTo(toFind) == 0) {
                isThere = center;
            }
        }
        return isThere;
    }

    /**
     * Finishes the permuations of the worsd
     * @param toWord The word to check and add
     * @return The completed arraylist of words
     */
    public static ArrayList<String> wordIt(String toWord) {
        // Do stuff with the word here
        ArrayList<String> results = new ArrayList<String>();
        if (toWord.length() == 1) {
            results.add(toWord);
        }
        for (int i = 0; i < toWord.length(); i++) {
            String firstLetter = toWord.substring(i, i + 1);
            String lettersLeft = toWord.substring(0, i) + toWord.substring(i + 1);
            ArrayList<String> innerPermutations = wordIt(lettersLeft);
            for (int j = 0; j < innerPermutations.size(); j++) {
                results.add(firstLetter + innerPermutations.get(j));
                results.add(innerPermutations.get(j));
                // The following line strictly uses permutations of the same length as the
                // original word.
                // results.push(firstChar + innerPermutations[j]);
            }
        }

        output.add(toWord);
        return results;
    }

    /**
     * Starts the clean up of the arraylist by removing duplicate values
     */
    public static void cleanUp() {
        ArrayList<String> done = new ArrayList<String>();
        String e;
        for (int i = 0; i < output.size(); i++) {
            e = output.get(i);
            if (!done.contains(e)) {
                done.add(e);
            }
        }
        done = cleanUp(done);
        output = done;
    }

    /**
     * Finishes the clean up operations
     * @param toCheck Word to check
     * @return The completed arraylist
     */
    public static ArrayList<String> cleanUp(ArrayList<String> toCheck) {
        ArrayList<String> done = new ArrayList<String>();
        for (String i : toCheck) {
            if (scrabble.contains(i)) {
                done.add(i);
            }
        }
        return done;
    }
    
    /**
     * Types the arraylist using the Robot class
     */
    public static void typeIt() {
        try {
            TimeUnit.SECONDS.sleep(3);
            Robot robot = new Robot();
            robot.setAutoDelay(2);
            robot.setAutoWaitForIdle(true);
            for (String word : output) {
                for (int i = 0; i < word.length(); i++) {
                    int keyCode = KeyEvent.getExtendedKeyCodeForChar(word.charAt(i));
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                }
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

            }
        } catch (Exception e) {
            System.out.println("Unexpected error occured");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File scrabble_new_file = new File("./scrabble_new.txt");
        if (!scrabble_new_file.exists()) {
            // Create the new file with trimmed words
            ArrayList<String> scrabble_old = readFile("scrabble.txt");
            ArrayList<String> scrabble_new = new ArrayList<String>();
            for (int i = 0; i < scrabble_old.size(); i++) {
                if (scrabble_old.get(i).length() > 2 && scrabble_old.get(i).length() < 8) {
                    scrabble_new.add(scrabble_old.get(i));
                }
            }
            writeFile("scrabble_new.txt", scrabble_new);
        } else {
            scrabble = readFile("./scrabble_new.txt");
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Enter a word that you would like to get all of the permutations for,\nIf you want to quit type '-1' and press enter: ");
            String w = sc.nextLine();
            if (!w.equals("-1")) {
                permutation(w);
                ArrayList<String> otherPermutations = wordIt(w);
                output.addAll(otherPermutations);
                cleanUp();
                typeIt();
                output = new ArrayList<String>();
            } else {
                sc.close();
                System.exit(0);
            }
        }
    }
}
