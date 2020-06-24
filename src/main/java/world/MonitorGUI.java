package world;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        game.addUpdateListener(panel::repaint);
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                game.getWorld().translatePlayerLocation(0, -2);
                break;
            case KeyEvent.VK_DOWN:
                game.getWorld().translatePlayerLocation(0, 2);
                break;
            case KeyEvent.VK_LEFT:
                game.getWorld().translatePlayerLocation(-2, 0);
                break;
            case KeyEvent.VK_RIGHT:
                game.getWorld().translatePlayerLocation(2, 0);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class Panel extends JPanel {
        public Panel() {
            setSize(MonitorGUI.this.getWidth(), MonitorGUI.this.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, MonitorGUI.this.getWidth(), MonitorGUI.this.getHeight());

            g.setColor(Color.WHITE);
            for (Sprite sprite : game.getSprites()) {
                g.drawString(String.valueOf(sprite.getTypeId()),
                        (int) sprite.getPoint().x, (int) sprite.getPoint().y);
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize(20);
        game.start();
        MonitorGUI monitorGUI = new MonitorGUI(game);
        monitorGUI.setVisible(true);
    }
}
