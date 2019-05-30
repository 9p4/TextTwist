import java.io.BufferedReader; // File IO
import java.io.File;
import java.io.FileReader; // More File IO
//import java.util.Scanner;  // User input
import java.util.ArrayList; // ArrayList for storing file
import java.io.FileWriter;
import java.io.BufferedWriter;

class Main {
    // Set some static variables:
    static ArrayList<String> scrabble;
    //static ArrayList<String> output = new ArrayList<String>();
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
                records.add(line.substring(0, line.length()-1));
            }
            reader.close();
            return records;
        }
        catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
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
            if (linearSearch(scrabble, toCheck) > -1) {
                wordIt(toCheck);
            }
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

    public static void wordIt(String toWord) {
        // Do stuff with the word here
        System.out.println(toWord);
    }
    
    public static int linearSearch(ArrayList<String> items, String toFind) {
        return linearSearch(items, toFind, 0, items.size());
    }

    public static int linearSearch(ArrayList<String> items, String toFind, int beginIndex, int endIndex) {
        int isThere = -1;
        for (int i=beginIndex; i<endIndex; i++) {
            if (items.get(i).equals(toFind)) {
                isThere = i;
                return isThere;
            }
        }
        return isThere;
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
        for (int i=2; i<=inputArr.length; i++) {
            //almostdone.addAll(printCombination(inputArr, inputArr.length, i));
            printCombination(inputArr, inputArr.length, i+1);
        }
        /*
        for (String e : almostdone) {
            if (!done.contains(e)) {
                done.add(e);
            }
        }
        System.out.println(done.toString());
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
    }
}