package loom.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IOFile {
    private String input_file;
    private String output_file;

    public IOFile(String input_file, String output_file) {
        this.input_file = input_file;
        this.output_file = output_file;
    }

    /**
     * Read input file to inputList
     */
    public List<String> readFromFile() {
        List<String> resList = new ArrayList<>();
        try {
            resList = Files.readAllLines(Paths.get(input_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resList;
    }

    /**
     * Save result list to file
     */
    public String saveToFile(ArrayList<String> resList) {
        try {
           Files.write(Paths.get(output_file), resList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output_file;
    }
}
