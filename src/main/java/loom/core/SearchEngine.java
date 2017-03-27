package loom.core;

import java.util.*;

public class SearchEngine {

    private static final String SEPARATOR = " ";
    private static final int WORD_START_NUMBER = 2;
    private static final double WORD_CHANGED = 1;

    private List<String> inputList = new ArrayList<>();
    private List<String> resList = new ArrayList<>();
    private MinHash minHash = new MinHash(128);

    public SearchEngine(List<String> inputList) {
        this.inputList = inputList;
    }

    public ArrayList<String> startSearch() {
        //Split sentence on words and calculating the amount of the sentence.
        ArrayList<String[]> wordsList = new ArrayList<>();
        for (String line : inputList) {
            String[] str = line.split(SEPARATOR);
            wordsList.add(Arrays.copyOfRange(str, WORD_START_NUMBER, str.length));
        }

        //Calculating the difference in the sums of sentences and filtering sentences.
        for (int i = 0; i < wordsList.size(); i++) {
            String[] set1 = wordsList.get(i);
            for (int j = i + 1; j < wordsList.size(); j++) {
                String[] set2 = wordsList.get(j);
                byte[] sig1 = minHash.getSignature(set1);
                byte[] sig2 = minHash.getSignature(set2);
                double k = minHash.getSimilarity(sig1, sig2);
                double ind = 1.0 - (1.0 / set1.length * WORD_CHANGED);
                if (k >= ind) {
                    resList.add(inputList.get(i));
                    resList.add(inputList.get(j));
//                    resList.add("Similarity: " + k);
                    resList.add("The changing word was: " + getDiffWords(set1, set2));
                    resList.add("");
                }
            }
        }
        return (ArrayList<String>) resList;
    }

    private String getDiffWords(String[] str1, String[] str2) {
        Set<String> set1 = new HashSet(Arrays.asList(str1));
        Set<String> set2 = new HashSet(Arrays.asList(str2));
        return findWord(set1, set2) + ", " + findWord(set2, set1);
    }

    private String findWord(Set<String> set1, Set<String> set2) {
        for (String word: set1) {
            if(!set2.contains(word)){
               return word;
            }
        }
        return "";
    }
}
