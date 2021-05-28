package Skybreaker;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

import static Skybreaker.Sounds.*;



public class SkybreakerMain extends Application {

    //HIGHSCORES
    static File Scores = new File("Highscores.txt");
    static FileWriter ScoresWrite;
    static FileReader ScoresRead;
    static BufferedReader ScoresBuffReader;
    static BufferedWriter ScoresBuffWrite;
    static ArrayList<Integer> HighScores = new ArrayList<>();
    static {
        try {
            if(!Scores.exists()) {
                Scores.createNewFile();
            }

            ScoresRead = new FileReader("Highscores.txt");
            ScoresBuffReader = new BufferedReader(ScoresRead);

            String inString;
            while((inString = ScoresBuffReader.readLine()) != null) {
                HighScores.add(Integer.parseInt(inString));
            }
            ScoresWrite = new FileWriter("Highscores.txt");
            ScoresBuffWrite = new BufferedWriter(ScoresWrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //wymiary okna
    static final double width = 1000;
    static final double height = 800;
    //Przycisk ekranu końcowego (obrazek)





    int numOfStarsCaptured = 0; //Liczba złapanych gwiazdek podczas tej rundy
    static ArrayList<Ball> Balls = new ArrayList<>(); //Piłki
    static ArrayList<Block> Blocks = new ArrayList<>(); //Bloki
    static ArrayList<Star> Stars = new ArrayList<>(); //Gwiazdki
    static ArrayList<ThunderHoriz> ThundersHoriz = new ArrayList<>(); //l-r-Arrows
    static HashSet<ThunderHoriz> ActiveHorizThunders = new HashSet<>(); //Active l-r-Arrows
    static ArrayList<ThunderVert> ThundersVert = new ArrayList<>(); //u-d-Arrows
    static HashSet<ThunderVert> ActiveVertThunders = new HashSet<>(); //Active u-d-Arrows


     static Set<Integer> RandomBlocks = new HashSet<>(); //Bloki losowane randomowo na "sufit" po zakończonej rundzie
     static Set<Integer> PossibleElementsPos = new HashSet<>(); //Możliwe pozycje innych elementów

    //------------------------------------------------------------------------------------------------------------------
    void getRandomTop() { //losujemy 10 razy i_post od 1 do 10 i dodajemy do zbioru RandomBlocks

        for(int i = 0 ; i < 7 ; i++) {
            RandomBlocks.add(new Random().nextInt(10)+1);
        }

        for(int i = 1 ; i <= 10 ; i++) { //Dodawanie gwiazdki na losowej pozycji na której nie ma bloku
            if(!RandomBlocks.contains(i)) {
                PossibleElementsPos.add(i);
            }
        }
        int StarPos = new Random().nextInt(PossibleElementsPos.size());
        int i = 0;
        for(Integer pos : PossibleElementsPos) {
            if(i == StarPos) {
                Stars.add(new Star(pos,1));
                PossibleElementsPos.remove(pos);
                break;
            }
            i++;
        }
        int ThunderHorizPos = new Random().nextInt(PossibleElementsPos.size());
        i = 0;
        for(Integer pos : PossibleElementsPos) {
            if(i == ThunderHorizPos) {
                if(new Random().nextInt(7) == 0) { //25% na pojawienie się bloku
                    ThundersHoriz.add(new ThunderHoriz(pos,1));
                    PossibleElementsPos.remove(pos);
                }
                break;
            }
            i++;
        }
        int ThunderVertPos = new Random().nextInt(PossibleElementsPos.size());
        i = 0;
        for(Integer pos : PossibleElementsPos) {
            if(i == ThunderVertPos) {
                if(new Random().nextInt(7) == 0) { //25% na pojawienie się bloku
                    ThundersVert.add(new ThunderVert(pos,1));
                    PossibleElementsPos.remove(pos);
                }
                break;
            }
            i++;
        }

        PossibleElementsPos.clear();



        for(Integer i_pos : RandomBlocks) { //Dodajemy bloki ze zbioru RandomBlocks a nastepnie czyscimy zbior
            Blocks.add(new Block(i_pos,1, Board.level));
        }
        RandomBlocks.clear();

    }
    //------------------------------------------------------------------------------------------------------------------
    void DrawAll(GraphicsContext gc) {
        for(Block bl : Blocks) { //Rysujemy Bloki
            if(bl.isInGame()) {
                bl.draw(gc);
            }
        }

        for(Star st : Stars) { //Rysujemy gwiazdki
            if(st.isInGame()) {
                st.draw(gc);
            }
        }

        for(ThunderHoriz th : ThundersHoriz) {
            if(th.isInGame()) {
                th.draw(gc);
            }
        }

        for(ThunderVert tv : ThundersVert) {
            if(tv.isInGame()) {
                tv.draw(gc);
            }
        }

        for(ThunderHoriz th : ActiveHorizThunders) {
            if(th.my_timer-- > 0) {
                th.drawThunderHoriz(gc);
            }
        }

        for(ThunderVert tv : ActiveVertThunders) {
            if(tv.my_timer-- > 0) {
                tv.drawThunderVert(gc);
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
     void removeDestroyedElements() { //usuwanie rozbitych elementów
         for (int i = 0; i < Blocks.size(); i++) { //Usuwanie bloków
             if (!Blocks.get(i).isInGame) {
                 //noinspection SuspiciousListRemoveInLoop
                 Blocks.remove(i);
             }
         }

         for (int i = 0; i < Stars.size(); i++) {
             if (!Stars.get(i).isInGame()) {
                 //noinspection SuspiciousListRemoveInLoop
                 Stars.remove(i);
             }
         }

         for (int i = 0; i < ThundersHoriz.size(); i++) {
             if (ThundersHoriz.get(i).amIHit) { //Remove ThunderHoriz
                 //noinspection SuspiciousListRemoveInLoop
                 ThundersHoriz.remove(i);
             }
         }

         for (int i = 0; i < ThundersVert.size(); i++) {
             if (ThundersVert.get(i).amIHit) { //Remove ThunderVert
                 //noinspection SuspiciousListRemoveInLoop
                 ThundersVert.remove(i);
             }
         }
     }
    //------------------------------------------------------------------------------------------------------------------
    void drawBackground(GraphicsContext gc) {
        for(int i=0;i<10;i++) {
            for(int j=0;j<16;j++) {
                if(i%2==j%2)
                    gc.drawImage(Block.Block_Images_a.get(5), i * 100, j*50);
                else
                    gc.drawImage(Block.Block_Images_a.get(8), i * 100, j*50);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
     void GameInAction(GraphicsContext gc, Stage stage, Board board) {
         gc.drawImage(Board.gameBackground, 0, 0);
        //ilosc wypuszczonych pilek
         gc.fillText("Score: " + board.score, 0.9*width, 0.95*height); //WYNIK
        gc.fillText(board.thrownBalls + "/" + board.ballsCapacity, width / 2, 760);
         gc.getCanvas().setOnMouseClicked(e ->  {});
         if(board.thrownBalls<board.ballsCapacity) {
             board.timer++;
             if(board.timer == 10) {
                 board.timer=0;
                 if (board.thrownBalls != 0) {
                     Balls.add(new Ball());
                 }
                 Balls.get(Balls.size() - 1).throwBall(board.ThrowBall_x, board.ThrowBall_y);
                 board.thrownBalls++;
                 if (board.thrownBalls == board.ballsCapacity) board.allBallInGame = true;
             }
         }

        //jezeli pilka w grze to zmieniamy pozycje, sprawdzamy odbicia i rysujemy
        for(int i = 0 ; i < 5 ; i++) { //sprawdzamy po ruchu o 1/5 klatki
            for(Ball ball : Balls) {
                if(ball.isInGame()) {
                    ball.moveOneFifth();
                    if(ball.checkWallsBounce() && ball.isInGame)
                        wallSound();
                    ball.draw(gc);
                    boolean BallHit = false;
                    for(Block bl : Blocks) { //Kolizja z blokami (krawędzie)
                        if(bl.isInGame()) {
                            if (ball.checkCollisionWithBlockEdge(bl)) {
                                bl.hit();
                                if(!bl.isInGame()) {board.score+=66;} //WYNIK
                                blockSound();
                                BallHit = true;
                                break;
                            }
                        }
                    }
                    if(!BallHit) {
                        for(Block bl : Blocks) { //Kolizja z blokami (narożniki)
                            if (bl.isInGame()) {
                                if (ball.checkCollisionWithBlockVertex(bl)) {
                                    bl.hit();
                                    if(!bl.isInGame()) {board.score+=66;} //WYNIK
                                    blockSound();
                                    break;
                                }
                            }
                        }
                    }
                    for(Star st : Stars) { //Kolizja z gwiazdkami
                        if(st.isInGame()) {
                            if(ball.checkCollisionWithStar(st)) {
                                st.hit();
                                starSound();
                                numOfStarsCaptured++; //DODAJEMY PILKE PO ZBICIU GWIAZDKI
                            }
                        }
                    }
                    for(ThunderHoriz th : ThundersHoriz) { //Kolizja z ThunderHoriz
                        if(ball.checkCollisionWithTh(th)) {
                            thunderSound();
                            th.hit();
                            th.my_timer = 10; //Odnawiamy trwanie thunder
                            ActiveHorizThunders.add(th);

                            for(Block bl : Blocks) {
                                if(bl.isInGame) {
                                    if(bl.j_pos == th.j_pos) {
                                        bl.hit();
                                        if(!bl.isInGame()) {board.score+=66;} //WYNIK
                                    }
                                }
                            }
                        }
                    }
                    for(ThunderVert tv : ThundersVert) { //Kolizja z ThunderVert
                        if(ball.checkCollisionWithTv(tv)) {
                            thunderSound();
                            tv.hit();
                            tv.my_timer = 10; //Odnawiamy trwanie thunder
                            ActiveVertThunders.add(tv);

                            for(Block bl : Blocks) {
                                if(bl.isInGame) {
                                    if(bl.i_pos == tv.i_pos) {
                                        bl.hit();
                                        if(!bl.isInGame()) {board.score+=66;} //WYNIK
                                    }
                                }
                            }
                        }
                    }



                }
            }
        }


        //jezeli element nie zbity to rysujemy
        DrawAll(gc);

        //czy wszystkie pilki spadly
        if(board.allBallInGame) {
            boolean endCheck = true;
            for(Ball ball : Balls) {
                if (ball.isInGame()) {
                    endCheck = false;
                    break;
                }
            }
            if (endCheck) {
                board.gameInAction = false;
                Board.level++;
            }

        }
    }
    //------------------------------------------------------------------------------------------------------------------
     void GameStopped(GraphicsContext gc, Stage stage, Board board) {
         gc.drawImage(Board.gameBackground, 0, 0);
         gc.fillText("Score: " + board.score, 0.9*width, 0.95*height); //WYNIK
        //pierwsza fala blokow przed startem gry i pilki
        if(!board.gameStarted) {
            Balls.clear();
            Balls.add(new Ball()); //1 piłka na start
            Blocks.clear();
            getRandomTop();
            board.gameStarted = true;
        }

        //spelnione gdy jestesmy tuz po zakonczonej "fali", spadly nam wszystkie pilki
        if(board.allBallInGame) {
            Balls.clear();
            ActiveVertThunders.clear();
            ActiveHorizThunders.clear();
            board.ballsCapacity += numOfStarsCaptured;
            numOfStarsCaptured = 0;
            Balls.add(new Ball()); //Pierwsza piłka rysowana na pozycji startowej
            board.allBallInGame = false;

            //usuwanie rozbitych elementow z arraylisty blocks
            removeDestroyedElements();
            for (Block bl : Blocks) {
                bl.goDown(); //sprawdzamy od razu czy bloki doszly juz do dna ekranu - end game
                if(bl.yValues[0] == SkybreakerMain.height && bl.isInGame()) {
                    Board.endGame = true;
                    //-------
                    if(HighScores.size() < 10) { //DODAWANIE UZYSKANEGO WYNIKU
                        HighScores.add(board.score);
                        HighScores.sort(Collections.reverseOrder());
                    }
                    else if(HighScores.get(HighScores.size()-1) < board.score) { //Ostatni najmniejszy wynik jest mniejszy od nowego wyniku, wymieniamy go
                        HighScores.remove(HighScores.size()-1);
                        HighScores.add(board.score);
                        HighScores.sort(Collections.reverseOrder());
                    }
                    //-------
                    return;
                }
            }
            for(Star st : Stars) {
                st.goDown();
            }
            for(ThunderHoriz th : ThundersHoriz) {
                th.goDown();
            }
            for(ThunderVert tv : ThundersVert) {
                tv.goDown();
            }
            //losowe bloki w górnym pietrze
            getRandomTop();
        }

        //rysujemy wszystko
         DrawAll(gc);


        //rysujemy pilke na startowej pozycji, resetujemy ustawienia Ball i Board
        Balls.get(0).draw(gc);
        board.allBallInGame = false;
        board.thrownBalls = 0;

        //napis
        gc.setFill(Color.WHITE);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Click", width / 2, 760);


        //start gry, pierwsza wystrzelona pilka
        gc.getCanvas().setOnMouseClicked(e ->  {
            board.startGame();
            if(board.thrownBalls == board.ballsCapacity) board.allBallInGame = true;
            board.ThrowBall_x = e.getX() - width / 2;
            board.ThrowBall_y = e.getY() - (height-Balls.get(0).getR());
            Balls.get(0).throwBall(board.ThrowBall_x, board.ThrowBall_y);
            board.timer = 0;
        });
    }
    //------------------------------------------------------------------------------------------------------------------
    void GameFinished(GraphicsContext gc, Stage stage, Board board) {
        gc.drawImage(Board.gameBackground, 0, 0);
        gc.setFill(Color.PURPLE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("You Lost, Better Luck Next Time!", width / 2, height/4);
        gc.fillText("Your Score: "+ board.score, width/2, 0.3*height);
        gc.drawImage(Board.endGameButton, width/5, height/2);
        gc.fillText("Back to menu", 3*width/10, height/2 + height/16 +10);
        gc.drawImage(Board.endGameButton, 3*width/5, height/2);
        gc.fillText("Quit", 7*width/10, height/2 + height/16 + 10);

        gc.getCanvas().setOnMouseClicked(e ->  {
            if(e.getX() >= width/5 && e.getX() <= 2*width/5 && e.getY() >= height/2 && (e.getY() <= height/2 + height/8)   ) { //kursor w prostokacie Play Again
                board.showMenu = true;
            }
            if(e.getX() >= 3*width/5 && e.getX() <= 4*width/5 && e.getY() >= height/2 && (e.getY() <= height/2 + height/8)   ) { //kursor w prostokacie Quit
                stage.close();
            }

        });
    }
    //------------------------------------------------------------------------------------------------------------------
    void GameMenu(GraphicsContext gc, Stage stage, Board board) {
         drawBackground(gc);
         Font f = gc.getFont();
        gc.setFont(Font.loadFont("file:Font/KaushanScript-Regular.otf", 50));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Sky Breaker", width / 2, height/4);
        gc.setFont(f);
        gc.setFill(Color.PURPLE);
        gc.drawImage(Board.endGameButton, width/5, height/2);
        gc.fillText("New Game", 3*width/10, height/2 + height/16 + 10 );

        gc.drawImage(Board.endGameButton, 3*width/5, height/2);
        gc.fillText("Quit", 7*width/10, height/2 + height/16 + 10);

        gc.drawImage(Board.endGameButton, width/5, height/4*3);
        gc.fillText("Scoreboard", 3*width/10, height/4*3 + height/16 + 10 );

        gc.drawImage(Board.endGameButton, 3*width/5, height/4*3);
        gc.fillText("Credits", 7*width/10, height/4*3 + height/16 + 10 );

        gc.getCanvas().setOnMouseClicked(e ->  {
            if(e.getX() >= width/5 && e.getX() <= 2*width/5 && e.getY() >= height/2 && (e.getY() <= height/2 + height/8)   ) { //kursor w prostokacie Play Again
                Stars.clear();
                Blocks.clear();
                ThundersHoriz.clear();
                ThundersVert.clear();
                ActiveHorizThunders.clear();
                ActiveVertThunders.clear();
                board.gameStarted = false;
                board.ballsCapacity = 1;
                board.blocksAtStart = 7;
                Board.endGame = false;
                Board.level = 1;
                board.showMenu = false;
                board.score = 0;
            }
            if(e.getX() >= 3*width/5 && e.getX() <= 4*width/5 && e.getY() >= height/2 && (e.getY() <= height/2 + height/8)   ) { //kursor w prostokacie Quit
                stage.close();
            }
            if(e.getX() >= width/5 && e.getX() <= 2*width/5 && e.getY() >= height/4*3 && (e.getY() <= height/4*3 + height/8)) { //kursor w ScoreBoard
                board.showMenu = false;
                board.showScores = true;
            }
            if(e.getX() >= 3*width/5 && e.getX() <= 3*width/5 + Board.endGameButton.getWidth() && e.getY() >= height/4*3 && (e.getY() <= height/4*3 + Board.endGameButton.getHeight())) { //kursor w Credits
                board.showMenu = false;
                board.showCredits = true;
            }


        });
    }
    //------------------------------------------------------------------------------------------------------------------
    void ScoreBoard(GraphicsContext gc, Stage stage, Board board) {

         drawBackground(gc);
         Font f = gc.getFont();
         gc.setFill(Color.WHITE);
         gc.setFont(Font.loadFont("file:Font/KaushanScript-Regular.otf", 50));
         gc.fillText("High Scores", width/2, height/8);
         gc.setFont(f);
         gc.setFill(Color.CHOCOLATE);
         for(int i = 0 ; i < HighScores.size() ; i++) {
             gc.fillText((i + 1) +".   "+ HighScores.get(i), width/2, height/8 + (i+1)*height/16);
         }

        gc.drawImage(Board.endGameButton, width/2 - Board.endGameButton.getWidth()/2, 7*height/8 - Board.endGameButton.getHeight()/2 - 10);
        gc.setFill(Color.BURLYWOOD);
        gc.fillText("Back to menu", width/2, 7*height/8);
        gc.getCanvas().setOnMouseClicked(e ->  {
            if(e.getX() >= width/2 - Board.endGameButton.getWidth()/2 && e.getX() <= width/2 - Board.endGameButton.getWidth()/2 + Board.endGameButton.getWidth() && e.getY() >=7*height/8 - Board.endGameButton.getHeight()/2 && (e.getY() <= 7*height/8 - Board.endGameButton.getHeight()/2 + Board.endGameButton.getHeight())   ) { //Back to Menu
                board.showMenu = true;
                board.showScores = false;
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------------
    void Credits(GraphicsContext gc, Stage stage, Board board) {
        drawBackground(gc);
        Font f = gc.getFont();
        gc.setFill(Color.CHOCOLATE);
        gc.setFont(Font.loadFont("file:Font/KaushanScript-Regular.otf", 50));
        gc.fillText("Game Created by:", width/2, height/8);
        gc.fillText("Juliet Bialy", width/2, 3*height/8);
        gc.fillText("Jakub Dziarkowski", width/2, 4*height/8);
        gc.fillText("Dominik Wojcikiewicz", width/2, 5*height/8);

        gc.setFont(f);
        gc.drawImage(Board.endGameButton, width/2 - Board.endGameButton.getWidth()/2, 7*height/8 - Board.endGameButton.getHeight()/2 - 10);
        gc.setFill(Color.BURLYWOOD);
        gc.fillText("Back to menu", width/2, 7*height/8);
        gc.getCanvas().setOnMouseClicked(e ->  {
            if(e.getX() >= width/2 - Board.endGameButton.getWidth()/2 && e.getX() <= width/2 - Board.endGameButton.getWidth()/2 + Board.endGameButton.getWidth() && e.getY() >=7*height/8 - Board.endGameButton.getHeight()/2 && (e.getY() <= 7*height/8 - Board.endGameButton.getHeight()/2 + Board.endGameButton.getHeight())   ) { //Back to Menu
                board.showMenu = true;
                board.showCredits = false;
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------------
    public void start(Stage stage) throws Exception {
        stage.setTitle("SkyBreaker");
        Canvas canvas = new Canvas(width, height);
        canvas.setCursor(Cursor.CLOSED_HAND);
        makeSoundsGreatAgain();
        Board board = new Board(1,7);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            try {
                run(gc, stage, board);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }
    //------------------------------------------------------------------------------------------------------------------

    void run(GraphicsContext gc, Stage stage, Board board) throws InterruptedException {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.loadFont("file:Font/KaushanScript-Regular.otf", 30));
        if(board.showMenu) {
            GameMenu(gc, stage, board);
        }
        else if(board.showCredits) {
            Credits(gc, stage, board);
        }
        else if(board.showScores) {
            ScoreBoard(gc, stage, board);
        }
        else if(board.gameInAction) { //jezeli gra trwa
            GameInAction(gc, stage, board);
        }
        else if (!Board.endGame){ //gdy gra jest zatrzymana, pilki nie sa w ruchu
            GameStopped(gc, stage, board);
        }
        else {
            //napis konca gry
            GameFinished(gc, stage, board);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void stop() { //Wywoływana zawsze przed zamknięciem aplikacji
        try {
            for(Integer score : HighScores) {
                ScoresBuffWrite.write(String.valueOf(score));
                ScoresBuffWrite.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ScoresBuffWrite);
            close(ScoresBuffReader);
        }

    }

    public static void close(Closeable c) {
         if (c == null) return;
         try {
             c.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }
}
