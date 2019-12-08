import javafx.scene.canvas.GraphicsContext;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class HistogramAlphabet {

    private HashMap<Character, Integer> map;
    private double totalFreq;

    public HistogramAlphabet(){
        this.totalFreq = 1;
    }

    public HistogramAlphabet(String gpa){
        map = parseNovel(gpa);
        this.totalFreq = totalFrequency();
    }

    public HashMap<Character,Integer> parseNovel(String gpa){
        String chars = gpa.toLowerCase();
        HashMap<Character,Integer> tempMap = new HashMap<Character,Integer>();


                for(int i = 0; i < gpa.length(); i++) {
                    char letter = chars.charAt(i);
                    if (Character.isLetter(letter)) {
                        if (tempMap.containsKey(letter)) {
                            tempMap.put(letter, tempMap.get(letter) + 1);
                        } else {

                            tempMap.put(letter, 1);
                        }
                    }
                }

        return tempMap;

    }

    public int totalFrequency(){
        int freq = 0;

        for(Character letter : map.keySet()){
            freq += map.get(letter);
        }

        return freq;
    }

    public HashMap<Integer, Letters> getDescendingOrder(int n){
        ArrayList<Letters> letters = new ArrayList<Letters>();
        ArrayList<Letters> nLetters = new ArrayList<Letters>(n+1);
        HashMap<Integer, Letters> returnMap = new HashMap<Integer,Letters>(n+1);
        double sumOfProbability = 0;

        for(Character letter : map.keySet()) {
            double frequency = map.get(letter);
            double probability = frequency/this.totalFreq;

            letters.add(new Letters(String.valueOf(letter),probability));
        }

        Collections.sort(letters, Comparator.reverseOrder());

        for(int i = 0; i < letters.size() && i < n; i ++){
            sumOfProbability += letters.get(i).getProbability();
            nLetters.add(i, letters.get(i));
        }

        nLetters.add(new Letters("Remaining", 1-sumOfProbability));
        Collections.sort(nLetters, Comparator.reverseOrder());

        for(int i = 0; i < nLetters.size(); i++){
            returnMap.put(i,nLetters.get(i));
        }

        return returnMap;
    }

    public abstract void draw(GraphicsContext gc, String gpa, double width, double height);

}
