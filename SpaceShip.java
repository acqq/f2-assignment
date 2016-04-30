
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class SpaceShip extends Sprite {

    public static int step = 5;
    private int posX = 0;
    private int posY = 0;
    private int life = 3;
    public static int maxHP = 100;
    public static int HP = 100;
    private BufferedImage image;
    private Timer timer;
    private int count;
    public boolean alive = true;

    public SpaceShip(int x, int y, int width, int height) {
        super(x, y, width, height);

        timer = new Timer(26, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                move();
            }
        });
        timer.setRepeats(true);
        timer.start();

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(x, y, width, height);

    }

    public void setPosX(int state) {
        posX = state;

    }

    public void setPosY(int state) {
        posY = state;
    }

    private void move() {
        if ((x + step * posX) > -10 && (x + step * posX) < 375) {
            x += step * posX;
        }

        if ((y + step * posY) < 570 && (y + step * posY) > 0) {
            y += step * posY;
        }
    }

    public void stop() {
        timer.stop();
    }

    public int getLife() {
        return life;
    }

    public void desHP() {
        HP -= 10;

    }

    public int getHP() {
        return HP;

    }

    public void decreaseLife() {
        life -= 1;
        Bullet.step = 15;
        SpaceShip.step = 5;
    }

    public void plusLife() {
        if (life < 3) {
            life += 1;
        }
    }
}
