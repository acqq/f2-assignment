
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

public class GameEngine implements KeyListener, GameReporter {

    GamePanel gp;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Item> item = new ArrayList<Item>();
    private SpaceShip v;
    private int time = 0;
    private int life = 3;
    private Timer timer;
    private long score = 0;
    private int level = 0;
    private int PosItem = 20;
    private double difficulty = 0.05;
    private double difficultyItem = 0.98;

    public GameEngine(GamePanel gp, SpaceShip v) {
        this.gp = gp;
        this.v = v;

        gp.sprites.add(v);

        timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                process();
                time++;
            }
        });
        timer.setRepeats(true);

    }

    public void start() {
        timer.start();
    }

    private void generateEnemy() {
        Enemy e = new Enemy((int) (Math.random() * 390), 30);
        gp.sprites.add(e);
        enemies.add(e);
    }

    private void generateBullet() {
        Bullet b = new Bullet(v.x + v.width / 2 - 2, v.y - 10);
        gp.sprites.add(b);
        bullets.add(b);
    }

    private void generateItem() {
        int a = new Random().nextInt(390);
        System.out.println(a);
        Item m = new Item(a, 0, 30, 40);
        gp.sprites.add(m);
        item.add(m);
    }

    private void process() {

        if (Math.random() < difficulty) {
            generateEnemy();
            if (Math.random() >= difficultyItem) {
                generateItem();
            }
        }

        Iterator<Enemy> e_iter = enemies.iterator();
        Iterator<Bullet> b_iter = bullets.iterator();
        Iterator<Item> m_iter = item.iterator();

        while (e_iter.hasNext()) {
            Enemy e = e_iter.next();
            e.proceed();

            if (!e.isAlive()) {
                e_iter.remove();
                gp.sprites.remove(e);
            }
        }

        while (m_iter.hasNext()) {
            Item m = m_iter.next();
            m.proceed();
            if (!m.isAlive()) {
                m_iter.remove();
                gp.sprites.remove(m);
            }

        }

        while (b_iter.hasNext()) {
            Bullet b = b_iter.next();
            Rectangle2D.Double b_hit = b.getRectangle();
            Rectangle2D.Double m_hit;
            Rectangle2D.Double e_hit;
            b.proceed();
            Iterator<Enemy> e_iterNew = enemies.iterator();
            Iterator<Item> m_iterNew = item.iterator();

            while (e_iterNew.hasNext()) {
                Enemy e = e_iterNew.next();
                e_hit = e.getRectangle();
                if (e_hit.intersects(b_hit)) {
                    b.die();
                    e_iterNew.remove();
                    gp.sprites.remove(e);
                    score += 100;
                    break;
                }
            }
            if (!b.isAlive()) {
                b_iter.remove();
                gp.sprites.remove(b);
            }
        }


        life = v.getLife();
        gp.updateGameUI(this);
        Rectangle2D.Double vr = v.getRectangle();
        Iterator<Enemy> e_iterNew = enemies.iterator();
        Iterator<Item> m_iterNew = item.iterator();

        Rectangle2D.Double e_hit;
        Rectangle2D.Double m_hit;


        while (e_iterNew.hasNext()) {
            Enemy e = e_iterNew.next();
            e_hit = e.getRectangle();
            if (e_hit.intersects(vr)) {
                e_iterNew.remove();
                gp.sprites.remove(e);
                if (v.getLife() - 1 < 0) {
                    die();
                } else {
                    v.desHP();
                    if (SpaceShip.HP <= 0) {
                        v.decreaseLife();
                        life = v.getLife();
                        SpaceShip.HP = SpaceShip.maxHP;
                    }
                }
                return;
            }
        }

        while (m_iterNew.hasNext()) {
            Item m = m_iterNew.next();
            m_hit = m.getRectangle();
            if (m_hit.intersects(vr)) {
                m_iterNew.remove();
                gp.sprites.remove(m);
                if (v.getLife() - 1 <= 0) {
                    die();
                } else {

                    switch (m.getTypeOfItem()) {
                        case Item.RED_COLOR: {
                            Bullet.step += 20;
                            break;
                        }
                        case Item.GREEN_COLOR: {
                            v.plusLife();
                            break;
                        }
                        case Item.BLUE_COLOR: {
                            gp.sprites.removeAll(enemies);
                            enemies.clear();
                            break;
                        }
                    }
                    life = v.getLife();
                }
                return;
            }
        }
    }

    public void die() {
        v.stop();
        timer.stop();
    }

    void pressVehicle(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                v.setPosX(-1);
                break;
            case KeyEvent.VK_RIGHT:
                v.setPosX(1);
                break;
            case KeyEvent.VK_UP:
                v.setPosY(-1);
                break;
            case KeyEvent.VK_DOWN:
                v.setPosY(1);
                break;
            case KeyEvent.VK_SPACE:
                generateBullet();
                break;
            case KeyEvent.VK_D:
                difficulty += 0.01;
                break;
        }
    }

    void releaseVehicle(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                v.setPosX(0);
                break;
            case KeyEvent.VK_RIGHT:
                v.setPosX(0);
                break;
            case KeyEvent.VK_UP:
                v.setPosY(0);
                break;
            case KeyEvent.VK_DOWN:
                v.setPosY(0);
                break;
            case KeyEvent.VK_0:
                gp.sprites.removeAll(enemies);
                enemies.clear();
                break;
        }
    }

    public long getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    public int getLevel() {
        return level;
    }

    public int getHP() {
        return v.getHP();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressVehicle(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        releaseVehicle(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }
}
