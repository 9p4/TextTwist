
import java.io.BufferedReader; // File IO
import java.io.File;
import java.io.FileReader; // More File IO
//import java.util.Scanner;  // User input
import java.util.ArrayList; // ArrayList for storing file
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.concurrent.TimeUnit;
/**
 * This class finds all of the permutations of a inputed string that are in the dictionary, stores them in an ArrayList, and outputs these permutations as keystrokes.
 *
 * @author Parth Patel and Sambhav Saggi
 * @version 5/2/2019
 */
class Main {
    // Set some static variables:
    static ArrayList<String> scrabble = new ArrayList<String>();
    static ArrayList<String> output = new ArrayList<String>();
    static String toCheck;
    
    /**
    * Open and read a file, and return the lines in the file as a list
    * of Strings.
    * (Demonstrates Java FileReader, BufferedReader, and Java5.)
    * Stolen shamelessly from https://alvinalexander.com/blog/post/java/how-open-read-file-java-string-array-list
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
        }
        catch (IOException e) {
            System.err.format("IOException occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }
    public static void writeFile(String filename, ArrayList<String> data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            String newdata = "";
            for (int i = 0; i<data.size(); i++) {
                newdata = data.get(i) + "\n";
                writer.write(newdata);
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Error finding file");
            e.printStackTrace();
        }
    }
    public static void combinationUtil(String arr[], String data[], int start, int end, int index, int r) {
        toCheck = "";
        if (index == r) {
            for (int j=0; j<r; j++) {
                toCheck = toCheck + data[j];
            }
            // Using linear search because for some reason, binary search is not producing all of the results
            //if (linearSearch(scrabble, toCheck) > -1) {
                output.add(toCheck);
            //}
        } else {
            for (int i=start; (i <= end && end-i+1 >= r-index); i++) {
                data[index] = arr[i];
                combinationUtil(arr, data, i+1, end, index+1, r);
            }
        }
    }
  
    public static void printCombination(String arr[], int n, int r) { 
    String[] data = new String[r];
        combinationUtil(arr, data, 0, n-1, 0, r);
    }

    public static void permutation(String str) { 
        permutation("", str); 
    }

    private static void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) {
            if (binarySearch(scrabble, prefix) > -1) {
                wordIt(prefix);
            }
        } else {
            for (int i = 0; i < n; i++) {
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
            }
        }
    }

    /*
    public static ArrayList<String> getCombinations(String input) {
        ArrayList<String> output = new ArrayList<String>();
        
        //We will follow these rules:
        //Every word has at least one vowel.
        //Every syllable has one vowel.
        //Q is always followed by a u (queen).
        //Double the consonants f, l, and s at the end of a one-syllable word //that has just one vowel (stiff, spell, pass).
        
        input = input.toLowerCase();
        ArrayList<String> fragments = new ArrayList<String>();
        ArrayList<String> characters = new ArrayList<String>();
        for (int i=0;i<input.length(); i++) {
            characters.add(input.substring(i,i+1));
        }
        characters = insertionSort(characters);
        String c;
        // Process the characters into fragments
        for (int i=0; i<characters.size(); i++) {
            c = characters.get(i);
            System.out.println(c);
            if (c.equals("q")) {
                int uPos = linearSearch(characters, "u");
                if (uPos != -1) {
                    fragments.add("qu");
                }
            } else if (c.equals("f") || c.equals("l") || c.equals("s")) {
                int dPos = linearSearch(characters, c, i+1, characters.size());
                if (dPos != -1) {
                    String doubled = c + c;
                    fragments.add(doubled);
                }
            } else {
                fragments.add(c);
            }
        }
        // Convert fragments into word
        String e, n;
        for (String fragment : fragments) {
            if (fragment.length() == 1) {
                e = fragment.substring(0,1);
            } else if (fragment.length() == 2) {
                e = fragment.substring(1, 2);
            } else {
                e = "";
            }
            n = new String(e);
            output.add(n);
        }

        return fragments;
        
    }
    */
    public static int binarySearch(ArrayList<String> items, String toFind) {
        int isThere = -1;
        int start = 0;
        int end = items.size();
        int center = (start+end)/2;
        String cv;
        while (isThere == -1) {
            center = (start+end)/2;
            cv = items.get(center);
            if ((items.get(center-1).compareTo(toFind) < 0) && (cv.compareTo(toFind) > 0)) {
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

    public static ArrayList<String> wordIt(String toWord) {
        // Do stuff with the word here
        ArrayList<String> results = new ArrayList<String>();
        if (toWord.length() == 1){
            results.add(toWord);
        }
        for(int i = 0; i < toWord.length(); i++){
            String firstLetter = toWord.substring(i, i+1);
            String lettersLeft = toWord.substring(0, i) + toWord.substring(i + 1);
            ArrayList<String> innerPermutations = wordIt(lettersLeft);
            for (int j = 0; j < innerPermutations.size(); j++) {
                results.add(firstLetter + innerPermutations.get(j));
                results.add(innerPermutations.get(j));
                	// The following line strictly uses permutations of the same length as the original word.
                  // results.push(firstChar + innerPermutations[j]);
            }
        }
        
        output.add(toWord);
        return results;
    }

    public static void cleanUp() {
        ArrayList<String> done = new ArrayList<String>();
        String e;
        for (int i = 0; i<output.size(); i++) {
            e = output.get(i);
            if (!done.contains(e)) {
                done.add(e);
            }
        }
        done = cleanUp(done);
        output = done;
    }
    public static ArrayList<String> cleanUp(ArrayList<String> toCheck){
        ArrayList<String> done = new ArrayList<String>();
        for(String i: toCheck){
            if (scrabble.contains(i)){ 
                done.add(i);
            }
        }
        return done;
    }
    public static void typeIt() {
        try
        
        {
            TimeUnit.SECONDS.sleep(3);
            Robot robot = new Robot();
            robot.setAutoDelay(20);
            robot.setAutoWaitForIdle(true);
            for(String word: output){
                for (int i = 0; i<word.length();i++) 
                
                {     
                
                   int keyCode = KeyEvent.getExtendedKeyCodeForChar(word.charAt(i));     
                
                   robot.keyPress(keyCode);     
                
                   robot.keyRelease(keyCode); 
                   

                   
                }
                   robot.keyPress(KeyEvent.VK_ENTER);
                   robot.keyRelease(KeyEvent.VK_ENTER);
                
                //TimeUnit.SECONDS.sleep(1);
                }
        } catch (Exception e) {           
        
             System.out.println("Unexpected error occured");           
        
             e.printStackTrace();       
        
        }
    }
    
    public static int linearSearch(ArrayList<String> items, String toFind) {
        return linearSearch(items, toFind, 0, items.size());
    }

    public static int linearSearch(ArrayList<String> items, String toFind, int beginIndex, int endIndex) {
        for (int i=beginIndex; i<endIndex; i++) {
            if (items.get(i).equals(toFind)) {
                return i;
            }
        }
        return -1;
    }
    
    /*
    public static ArrayList<String> insertionSort(ArrayList<String> data) {
        int n = data.size();
        for (int i = 1; i < n; ++i) {
            String key = data.get(i);
            int j = i - 1;
            while (j >= 0 && data.get(j).compareTo(key) == 1) {
                data.set(j+1, data.get(j));
                j = j - 1;
            }
            data.set(j+1, key); 
        }
        return data;
    }
    */

    public static void main(String[] args) {
        File scrabble_new_file = new File("./scrabble_new.txt");
        if (!scrabble_new_file.exists()) {
            // Create the new file with trimmed words
            ArrayList<String> scrabble_old = readFile("scrabble.txt");
            ArrayList<String> scrabble_new = new ArrayList<String>();
         for (int i = 0; i<scrabble_old.size(); i++) {
                if (scrabble_old.get(i).length()>2 && scrabble_old.get(i).length()<8) {
                    scrabble_new.add(scrabble_old.get(i));
                }
            }
            writeFile("scrabble_new.txt", scrabble_new);
        } else {
            scrabble = readFile("./scrabble_new.txt");
        }

        String inputArr[] = {"r", "e", "p", "r", "o", "o", "f"};
        //ArrayList<String> almostdone = new ArrayList<String>();
        //ArrayList<String> done = new ArrayList<String>();
        //for (int i=2; i<=inputArr.length; i++) {
            //almostdone.addAll(printCombination(inputArr, inputArr.length, i));
        //    printCombination(inputArr, inputArr.length, i+1);
        //}
        for (int i=0; i<inputArr.length; i++) {
            for (int j=0; j<i; j++) {
                String[] newInputArr = new String[j];
            }
            
        }
        //System.out.println(scrabble.toString());
        boolean go = true;
        while(go){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter a word that you would like to get all of the permutations for,  \nif you want to quit type 'no' and press enter ");
            String w = sc.nextLine();
            if(!w.equals("no")){
                permutation(w);
                ArrayList<String> otherPermutations = wordIt(w);
                output.addAll(otherPermutations);
                cleanUp();
                typeIt();
            } 
            else if(w.equals("no")){
                go = false;
            }
            sc.close();
            output = new ArrayList<String>();
        }
        /*
        permutation(w);
        ArrayList<String> t = wordIt(w);
        output.addAll(t);
        cleanUp();
        System.out.println(output.toString());
        try
        
        {
            Robot robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
            for(String word: output){
                for (int i = 0; i<word.length();i++) 
                
                {     
                
                   int keyCode = KeyEvent.getExtendedKeyCodeForChar(word.charAt(i));     
                
                   robot.keyPress(keyCode);     
                
                   robot.keyRelease(keyCode); 
                   

                   
                }
                   robot.keyPress(KeyEvent.VK_ENTER);
                   robot.keyRelease(KeyEvent.VK_ENTER);
                
                //TimeUnit.SECONDS.sleep(1);
                }
        } catch (Exception e) {           
        
             System.out.println("Unexpected error occured");           
        
             e.printStackTrace();       
        
        }
        */
        /*
        ERR
        FOE
        FOR
        FRO
        ORE
        PER
        PRO
        REF
        REP
        ROE
        FORE
        POOR
        PORE
        ROOF
        ROPE
        PROOF
        ROPER
        POORER
        ROOFER
        PROOFER
        REPROOF
        */
        /* 
        TODO: Loop to do all combinations (by removing letters 1,2,3,4...)
        While loop and scanner input
        Automatic typing
        */
    }
}
