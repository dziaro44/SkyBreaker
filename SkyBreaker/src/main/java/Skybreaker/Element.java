package Skybreaker;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Element { //Klasa po której dziedziczą elementy inne niż piłka pojawiające się na ekranie
    static Integer pos_width = 100; //szerokość pojedynczej pozycji w której jest jakiś element
    static Integer pos_height = 50; //wysokość pojedynczej pozycji w której jest jakiś element
    double[] xValues = new double[4];
    double[] yValues = new double[4];
    boolean isInGame = true;
    Integer HitsToDestroy = 1;
    Image my_Image; //Każdy element ma pole my_Image
    int i_pos;
    int j_pos;

    public boolean isInGame() {return isInGame;}

    public double[] getXValues() {return xValues;}
    public double[] getYValues() {return yValues;}

    public void draw(GraphicsContext gc) {
        gc.drawImage(my_Image, xValues[3], yValues[3]);
    }

    public void hit() {
        HitsToDestroy--;
        if(HitsToDestroy == 0) isInGame = false;
    }

    public void goDown() {
        yValues[0]+=50;
        yValues[1]+=50;
        yValues[2]+=50;
        yValues[3]+=50;
        j_pos++;
    }
}
