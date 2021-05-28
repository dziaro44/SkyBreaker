package Skybreaker;

import javafx.scene.image.Image;


public class Star extends Element{ //Klasa gwiazdek (zbieranie gwiazdek daje piłki)
    static Integer star_width = 50;
    static Integer star_height = 50;
    static Image StarImage = new Image("file:PNG/59-Breakout-Tiles.png", star_width, star_height, false, false);

    Star(int i, int j) { //Uniwersalna funkcja która zawsze wyśrodkowuje obrazek w jeden z 10 pozycji
        i_pos = i;
        j_pos = j;
        my_Image = StarImage;
        HitsToDestroy = 1;
        xValues[0] = ((i - 1)*pos_width)+(pos_width/2)-(star_width/2); //lewy dolny
        xValues[1] = ((i - 1)*pos_width)+(pos_width/2)+(star_width/2); //prawy dolny
        xValues[2] = ((i - 1)*pos_width)+(pos_width/2)+(star_width/2); //prawy gorny
        xValues[3] = ((i - 1)*pos_width)+(pos_width/2)-(star_width/2); //lewy gorny
        yValues[0] = ((j - 1)*pos_height)+(pos_height/2)+(star_height/2);
        yValues[1] = ((j - 1)*pos_height)+(pos_height/2)+(star_height/2);
        yValues[2] = ((j - 1)*pos_height)+(pos_height/2)-(star_height/2);
        yValues[3] = ((j - 1)*pos_height)+(pos_height/2)-(star_height/2);
    }

}
