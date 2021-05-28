package Skybreaker;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static Skybreaker.SkybreakerMain.height;
import static Skybreaker.SkybreakerMain.width;

public class ThunderVert extends Element {
    public static int Arrows_width = 50;
    public static int Arrows_height = 50;
    public static Image ThunderVert = new Image("file:PNG/ThunderVert.png", width/20,height, false, false);
    public static Image lrArrow = new Image("file:PNG/udArrow.png", Arrows_width, Arrows_height, false, false);
    int my_timer;
    boolean amIHit = false;

    ThunderVert(int i, int j) {
        i_pos = i;
        j_pos = j;
        my_Image = lrArrow;
        xValues[0] = ((i - 1)*pos_width)+(pos_width/2)-(Arrows_width/2); //lewy dolny
        xValues[1] = ((i - 1)*pos_width)+(pos_width/2)+(Arrows_width/2); //prawy dolny
        xValues[2] = ((i - 1)*pos_width)+(pos_width/2)+(Arrows_width/2); //prawy gorny
        xValues[3] = ((i - 1)*pos_width)+(pos_width/2)-(Arrows_width/2); //lewy gorny
        yValues[0] = ((j - 1)*pos_height)+(pos_height/2)+(Arrows_height/2);
        yValues[1] = ((j - 1)*pos_height)+(pos_height/2)+(Arrows_height/2);
        yValues[2] = ((j - 1)*pos_height)+(pos_height/2)-(Arrows_height/2);
        yValues[3] = ((j - 1)*pos_height)+(pos_height/2)-(Arrows_height/2);
    }

    @Override
    public void hit() {
        amIHit = true;
    }

    public void drawThunderVert(GraphicsContext gc) {
        gc.drawImage(ThunderVert, xValues[3], 0);
    }
}