

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class Item extends Sprite {

    private int step = 3;
    private BufferedImage image;
    private Timer timer;
    public static final int Y_TO_DIE = 600;
    private boolean alive = true;
    private int numCheckItem = (int) (Math.random() * 100) % 3;
    public static final int RED_COLOR = 0;
    public static final int GREEN_COLOR = 1;
    public static final int BLUE_COLOR = 2;

    public Item(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g) {

        switch (getTypeOfItem()) {
            case RED_COLOR: {
                g.setColor(Color.RED);
                g.fillRect(x, y, width, height);
                break;
            }
            case BLUE_COLOR: {
                g.setColor(Color.BLUE);
                g.fillRect(x, y, width, height);
                break;
            }
            case GREEN_COLOR:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, width, height);
                break;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getTypeOfItem() {
        return numCheckItem;
    }

    public void proceed() {
        y += step;
        if (y > Y_TO_DIE) {
            alive = false;
        }
    }
}
