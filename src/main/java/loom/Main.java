package loom;


import loom.core.IOFile;
import loom.core.SearchEngine;

import java.util.ArrayList;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/input_file.txt";
    private static final String OUTPUT_FILE = "src/main/resources/output_file.txt";

    public static void main(String[] args) {
        System.out.println("Start...");
        IOFile ioFile = new IOFile(INPUT_FILE, OUTPUT_FILE);
        SearchEngine searchEngine = new SearchEngine(ioFile.readFromFile());

        System.out.println("Searching...");
        ArrayList<String> resList = searchEngine.startSearch();

        System.out.println("Save to file...");
        ioFile.saveToFile(resList);

        System.out.println("The result is saved. Look to the file on path: " + OUTPUT_FILE);
    }

}
