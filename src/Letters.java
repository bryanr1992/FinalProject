
public class Letters implements Comparable<Letters>{

    private String letter;
    private double probability;
    private int comparator;

    public Letters(){
        this.letter = "a";
        this.probability = 100.00;
    }

    public Letters(String aLetter, double probability){

        this.letter = aLetter;
        this.probability = probability;
        this.comparator = 0;
    }

    public void setLetter(String aLetter){
        this.letter = aLetter;
    }

    public void setProbability(double probability){
        this.probability = probability;
    }

    public String getLetter(){
        return this.letter;
    }

    public double getProbability(){
        return this.probability;
    }

    @Override
    public String toString(){
        return String.format("%s = %.4f", this.letter,(this.probability)*100);
    }

    @Override
    public int compareTo(Letters aLetter) {

        if(this.probability < aLetter.probability){
            this.comparator = -1;
        } else if (this.probability > aLetter.probability) {
            this.comparator = 1;
        }

        return comparator;
    }
}


