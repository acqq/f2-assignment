
import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Sprite implements AbilityBullet {

    public static int step = 15;
    public boolean alive = true;

    public Bullet(int x, int y) {
        super(x, y, 7, 20);

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public void proceed() {
        y -= step;

        if (y < 0) {
            alive = false;
        }
    }

    public void die() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void plusStep() {
        step += 1;
    }
}
