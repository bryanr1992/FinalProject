import java.util.HashMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class PieChartGraphDisplay extends HistogramAlphabet {

    private Color[] myColors;
    private int referenceAngle;

    public PieChartGraphDisplay(){
        super();
        this.myColors = null;
        this.referenceAngle = 360;
    }

    public PieChartGraphDisplay(Color[] colors,String gpa){

        super(gpa);
        this.myColors = colors;
        this.referenceAngle = 360;
    }

    public void draw(GraphicsContext gc, String gpa, double width, double height) {
        System.out.println("inside draw");
        Color strokeColor = Color.BLACK;

        int n = 0;

        double startAngle = 0;
        int colorSelector = 0;
        double xCenter = (width / 2) - 100;
        double yCenter = (height / 2) - 150;

        HashMap<Character,Integer> countMap = new HashMap<Character,Integer>();

        for (int i = 0; i < gpa.length(); i++ ){
            if(!countMap.containsKey(gpa)){
                countMap.put(gpa.charAt(i),1);
                n++;
                System.out.println(n);
            }
        }


        HashMap<Integer, Letters> map = getDescendingOrder(n);


        double x = 100, y = 150;

        gc.strokeText("Legend", x, y);
        y += 50;


        for (Integer key : map.keySet()) {


            Color fillColor = this.myColors[colorSelector];

            double probability = map.get(key).getProbability();
            String alphabet = map.get(key).getLetter();
            double angle = probability * this.referenceAngle;

            gc.setStroke(Color.BLACK);
            gc.strokeText(String.format("%s = %.4f", alphabet, (probability*100)) + "%", x, y);


            gc.setFill(fillColor);

            gc.setStroke(strokeColor);
            gc.strokeRect(x - 55, y - 20, 25, 25);
            gc.fillRect(x - 55, y - 20, 25, 25);

            gc.fillArc(xCenter, yCenter, 200, 200,
                    startAngle, angle, ArcType.ROUND);
            gc.setStroke(Color.WHITE);
            gc.strokeArc(xCenter, yCenter, 200, 200,
                    startAngle, angle, ArcType.ROUND);

            startAngle += angle;
            y += 30;
            if(y >= height - 75){
                x = 250;
                y = 200;
            }
            colorSelector++;
        }

    }

}

