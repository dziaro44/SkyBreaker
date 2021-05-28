package Skybreaker;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ball {
    //ball center x=xPos+BALL_R;
    //ball center y=yPos+BALL_R;
     static final double BALL_R = 15;
     static final Image ball_image = new Image("file:PNG/58-Breakout-Tiles.png", 30, 30, false, false);
     static final double width = 1000;
     static final double height = 800;
    //powinno zawsze byc XSpeed^2+YSpeed^2=1
     double xSpeed = 0;
     double ySpeed = -1;
     double speedMultiplier = 20;
     double xPos = width/2-BALL_R;
     double yPos = height-2*BALL_R;
     boolean isInGame = false;
     double distance(double x1, double y1, double x2, double y2) {
         return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
     }

    public Ball() {}
    public Ball(double xSpeed, double ySpeed, double speedMultiplier, boolean isInGame) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.speedMultiplier = speedMultiplier;
        this.isInGame = isInGame;
    }

    public double getR() {return BALL_R;}
    public double getX() {return xPos;}
    public double getY() {return yPos;}
    public double getXSpeed() {return xSpeed;}
    public double getYSpeed() {return ySpeed;}
    public double getSpeedMultiplier() {return speedMultiplier;}
    public boolean isInGame() {return isInGame;}

    public void setX(double xPos) {this.xPos = xPos;}
    public void setY(double yPos) {this.yPos = yPos;}
    public void setXSpeed(double xSpeed) {this.xSpeed = xSpeed;}
    public void setYSpeed(double ySpeed) {this.ySpeed = ySpeed;}
    public void setSpeedMultiplier(double speedMultiplier) {this.speedMultiplier = speedMultiplier;}
    public void setIsInGame(boolean isInGame) {this.isInGame = isInGame;}

    public void moveOneFifth() {
        xPos += 0.2*xSpeed;
        yPos += 0.2*ySpeed;
    }
    public void reset() {
        xPos = width/2-BALL_R;
        yPos = height-2*BALL_R;
        xSpeed = 0;
        ySpeed = -1;
        isInGame = false;
    }
    public void draw(GraphicsContext gc) {
        gc.drawImage(ball_image,xPos,yPos);
        //gc.fillOval(xPos, yPos, BALL_R, BALL_R);
    }
    public void throwBall(double bigX, double bigY) {
        double bigRoot = Math.sqrt(bigX * bigX + bigY * bigY);
        double smallX = bigX / bigRoot;
        double smallY = bigY / bigRoot;
        xSpeed = speedMultiplier * smallX;
        ySpeed = speedMultiplier * smallY;
        isInGame = true;
    }
    public boolean checkWallsBounce() {
        if (yPos < 0 && ySpeed < 0) {
            ySpeed *= -1;
            return true;
        }
        if (xPos + 2*BALL_R> width && xSpeed > 0) {
            xSpeed *= -1;
            return true;
        }
        if(xPos < 0 && xSpeed < 0) {
            xSpeed *= -1;
            return true;
        }
        if (yPos> height) {
            isInGame = false;
            return true;
        }
        return false;
    }
    public boolean checkCollisionWithBlockEdge(Block block) {
        double center_x=xPos+BALL_R;
        double center_y=yPos+BALL_R;
        //sprawdzamy czy kolizja z dolnym bokiem
        if(block.getXValues()[0] <= center_x && center_x <= block.getXValues()[1] && block.getYValues()[0] >= center_y-1.1*BALL_R
                && center_y+1.1*BALL_R >= block.getYValues()[0] && ySpeed<0) {
            ySpeed *= -1;
            return true;
        }
        //sprawdzamy czy kolizja z gornym bokiem
        if(block.getXValues()[0] <= center_x && center_x <= block.getXValues()[1] && center_y+1.1*BALL_R >= block.getYValues()[2]
                && center_y-1.1*BALL_R <= block.getYValues()[2] && ySpeed>0) {
            ySpeed *= -1;
            return true;
        }
        //sprawdzamy czy kolizja z lewym bokiem
        if(block.getYValues()[2] <= center_y && center_y <= block.getYValues()[0] && center_x-1.1*BALL_R <= block.getXValues()[0]
                && center_x + 1.1*BALL_R >= block.getXValues()[0] && xSpeed>0) {
            xSpeed *= -1;
            return true;
        }
        //sprawdzamy czy kolizja z prawym bokiem
        if(block.getYValues()[2] <= center_y && center_y <= block.getYValues()[0] && center_x+1.1*BALL_R >= block.getXValues()[1]
                && center_x-1.1*BALL_R <= block.getXValues()[1] && xSpeed<0) {
            xSpeed *= -1;
            return true;
        }
        return false;
    }
    public boolean checkCollisionWithBlockVertex(Block block) {
        double center_x=xPos+BALL_R;
        double center_y=yPos+BALL_R;
        //sprawdzamy czy kolizja z lewym dolnym naroznikiem
        if(center_x <= block.getXValues()[0] && center_y >= block.getYValues()[0] &&
                distance(center_x, center_y, block.getXValues()[0], block.getYValues()[0]) <= 1.05*BALL_R ) {
            boolean zmiana = false;
            if(ySpeed < 0) {
                ySpeed *= -1;
                zmiana = true;
            }
            if(xSpeed > 0) {
                xSpeed *= -1;
                zmiana = true;
            }
            if(zmiana) return true;
        }
        //sprawdzamy czy kolizja z prawym dolnym naroznikiem
        if(center_x >= block.getXValues()[1] && center_y >= block.getYValues()[1] &&
                distance(center_x, center_y, block.getXValues()[1], block.getYValues()[1]) <= 1.05*BALL_R) {
            boolean zmiana = false;
            if(ySpeed < 0) {
                ySpeed *= -1;
                zmiana = true;
            }
            if(xSpeed < 0) {
                xSpeed *= -1;
                zmiana = true;
            }
            if(zmiana) return true;
        }
        //sprawdzamy czy kolizja z prawym gornym naroznikiem
        if(center_x >= block.getXValues()[2] && center_y <= block.getYValues()[2] &&
                distance(center_x, center_y, block.getXValues()[2], block.getYValues()[2]) <= 1.05*BALL_R) {
            boolean zmiana = false;
            if(ySpeed > 0) {
                ySpeed *= -1;
                zmiana = true;
            }
            if(xSpeed < 0) {
                xSpeed *= -1;
                zmiana = true;
            }
            if(zmiana) return true;
        }
        //sprawdzamy czy kolizja z lewym gornym naroznikiem
        if(center_x <= block.getXValues()[3] && center_y <= block.getYValues()[3] &&
                distance(center_x, center_y, block.getXValues()[3], block.getYValues()[3]) <= 1.05*BALL_R) {
            boolean zmiana = false;
            if(ySpeed > 0) {
                ySpeed *= -1;
                zmiana = true;
            }
            if(xSpeed > 0) {
                xSpeed *= -1;
                zmiana = true;
            }
            if(zmiana) return true;
        }
        return false;
    }
    public boolean checkCollisionWithStar(Star star) {
         double BallCenter_x = xPos+BALL_R;
         double BallCenter_y = yPos+BALL_R;
         double StarCenter_x = (star.getXValues()[0]+star.getXValues()[2])/2;
         double StarCenter_y = (star.getYValues()[0]+star.getYValues()[2])/2;
         double Star_R = 25;
         if(distance(BallCenter_x,BallCenter_y,StarCenter_x,StarCenter_y)<=Star_R+BALL_R)
             return true;
         return false;
    }
    public boolean checkCollisionWithTh(ThunderHoriz Th) {
        double BallCenter_x = xPos+BALL_R;
        double BallCenter_y = yPos+BALL_R;
        double StarCenter_x = (Th.getXValues()[0]+Th.getXValues()[2])/2;
        double StarCenter_y = (Th.getYValues()[0]+Th.getYValues()[2])/2;
        double Star_R = 25;
        if(distance(BallCenter_x,BallCenter_y,StarCenter_x,StarCenter_y)<=Star_R+BALL_R && Th.my_timer <= 0)
            return true;
        return false;
    }
    public boolean checkCollisionWithTv(ThunderVert Tv) {
        double BallCenter_x = xPos+BALL_R;
        double BallCenter_y = yPos+BALL_R;
        double StarCenter_x = (Tv.getXValues()[0]+Tv.getXValues()[2])/2;
        double StarCenter_y = (Tv.getYValues()[0]+Tv.getYValues()[2])/2;
        double Star_R = 25;
        if(distance(BallCenter_x,BallCenter_y,StarCenter_x,StarCenter_y)<=Star_R+BALL_R && Tv.my_timer <= 0)
            return true;
        return false;
    }
}
