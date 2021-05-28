package Skybreaker;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block extends Element {
    static final double block_width = 100;
    static final double block_height = 50;
    static final ArrayList<Image> Block_Images_a = new ArrayList<>(List.of(
            new Image("file:PNG/01-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/03-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/05-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/07-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/09-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/11-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/13-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/15-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/17-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/19-Breakout-Tiles.png", block_width, block_height, false, false)));
    static final ArrayList<Image> Block_Images_b = new ArrayList<>(List.of(
            new Image("file:PNG/02-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/04-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/06-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/08-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/10-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/12-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/14-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/16-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/18-Breakout-Tiles.png", block_width, block_height, false, false),
            new Image("file:PNG/20-Breakout-Tiles.png", block_width, block_height, false, false)));
    boolean amIHit = false;


    public Block(int i, int j, int level) { //Uniwersalna funkcja która zawsze wyśrodkowuje obrazek w jeden z 10 pozycji
        i_pos = i;
        j_pos = j;
        xValues[0] = ((i - 1)*pos_width)+(pos_width/2)-(block_width/2); //lewy dolny
        xValues[1] = ((i - 1)*pos_width)+(pos_width/2)+(block_width/2); //prawy dolny
        xValues[2] = ((i - 1)*pos_width)+(pos_width/2)+(block_width/2); //prawy gorny
        xValues[3] = ((i - 1)*pos_width)+(pos_width/2)-(block_width/2); //lewy gorny
        yValues[0] = ((j - 1)*pos_height)+(pos_height/2)+(block_height/2);
        yValues[1] = ((j - 1)*pos_height)+(pos_height/2)+(block_height/2);
        yValues[2] = ((j - 1)*pos_height)+(pos_height/2)-(block_height/2);
        yValues[3] = ((j - 1)*pos_height)+(pos_height/2)-(block_height/2);
        HitsToDestroy = new Random().nextInt(2*level)+1; // random z 1,...,2lvl +1
    }

    @Override
    public void draw(GraphicsContext gc) {
        int image_pos = (HitsToDestroy/5);
        if(!amIHit) {
            gc.drawImage(Block_Images_a.get(image_pos%10), xValues[3], yValues[3]);
        } else {
            gc.drawImage(Block_Images_b.get(image_pos%10), xValues[3], yValues[3]);
        }

        gc.setFill(Color.MAGENTA);
        gc.fillText(HitsToDestroy.toString(),(xValues[3]+xValues[1])/2, (yValues[1]+yValues[2]+15)/2);
        gc.setFill(Color.BLACK);
    }

    @Override
    public void hit() {
        super.hit();
        amIHit = true;
    }


}
