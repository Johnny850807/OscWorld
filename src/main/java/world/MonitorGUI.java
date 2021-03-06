package world;

import utils.MinMax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class MonitorGUI extends JFrame implements KeyListener {
    private Game game;
    private Panel panel = new Panel();

    public MonitorGUI(Game game) throws HeadlessException {
        super("Monitor");
        this.game = game;
        setSize(800, 800);
        setContentPane(panel);
        setFocusable(true);
        requestFocusInWindow();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.addGameListener(new Game.GameListener() {
            @Override
            public void opUpdate(List<Sprite> viewSprites) {
                panel.repaint(viewSprites);
            }

            @Override
            public void onGameOver() {
            }
        });
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                game.getWorld().translatePlayerLocation(0, 0,  2);
                break;
            case KeyEvent.VK_DOWN:
                game.getWorld().translatePlayerLocation(0, 0,-2);
                break;
            case KeyEvent.VK_LEFT:
                game.getPlayer().setAngle(game.getPlayer().getAngle()+0.01);
                break;
            case KeyEvent.VK_RIGHT:
                game.getPlayer().setAngle(game.getPlayer().getAngle()-0.01);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class Panel extends JPanel {
        private List<Sprite> viewSprites;

        public Panel() {
            setSize(MonitorGUI.this.getWidth(), MonitorGUI.this.getHeight());
        }

        public void repaint(List<Sprite> viewSprites) {
            this.viewSprites = viewSprites;
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, MonitorGUI.this.getWidth(), MonitorGUI.this.getHeight());

            g.setColor(Color.WHITE);
            for (Sprite sprite : viewSprites) {
                int size = sprite instanceof SoundSprite ?
                        (int) (((SoundSprite) sprite).getVolume() * 100) : 10;
                g.setFont(new Font("Microsoft JhengHei", Font.BOLD, size));

                int x = MinMax.minmax((int) sprite.getPoint().x, -45, 70, 0, 115);
                int z = MinMax.minmax((int) sprite.getPoint().z, -40, 72, 0, 112);
                g.drawString(SoundSprites.getName(sprite.getTypeId()), x*10, -z*10);
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.start();
        MonitorGUI monitorGUI = new MonitorGUI(game);
        monitorGUI.setVisible(true);
    }
}
