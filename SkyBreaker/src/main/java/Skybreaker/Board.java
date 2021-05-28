package Skybreaker;

import javafx.scene.image.Image;

public class Board {

    //wymiary okna
    static final double width = 1000;
    static final double height = 800;
    //Przycisk ekranu końcowego (obrazek)
    public static Image endGameButton = new Image("file:PNG/EndGameButton.png", width/5,height/8, false, false);
    public static Image gameBackground = new Image("file:PNG/gameBackground.png", width,height, false, false);

    public static int level = 1;
    public static boolean endGame = false;
    public int ballsCapacity;
    public int blocksAtStart;
    public int thrownBalls = 0;
    public boolean gameInAction = false;
    public boolean allBallInGame = false;
    public boolean win = false;
    public boolean gameStarted = false;
    public double ThrowBall_x;
    public double ThrowBall_y;
    public boolean showMenu = true;
    public boolean showScores = false;
    public boolean showCredits = false;
    int timer;
    int score = 0; //Wynik gry


    public Board(int ballsCapacity, int blocksAtStart) {
        this.ballsCapacity = ballsCapacity;
        this.blocksAtStart = blocksAtStart;
    }

    public void startGame() {
        gameStarted = true;
        gameInAction = true;
        thrownBalls = 1;
    }


}
