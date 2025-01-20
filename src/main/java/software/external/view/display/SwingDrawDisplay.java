package software.external.view.display;

import software.ulpgc.view.display.Draw;

import software.ulpgc.view.display.MainFrame;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class SwingDrawDisplay implements Draw {

    protected final DrawingPanel panel;
    protected final Map<String, software.ulpgc.view.display.Component> components;


    public SwingDrawDisplay(String name, Color color, MainFrame.WindowSize size){
        panel = new DrawingPanel();
        components = new HashMap<>();
        this.set(color, name, size);
    }

    @Override
    public void removeAll() {
        components.clear();
        panel.removeAll();
    }

    private void set(Color color, String name, MainFrame.WindowSize size) {
        panel.setBackground(color);
        panel.setName(name);
        if (size != null) panel.setSize(new Dimension(size.x(), size.y()));
    }

    @Override
    public String name() {
        return panel.getName();
    }

    @Override
    public void revalidate() {
        panel.revalidate();
    }

    @Override
    public Color color() {
        return panel.getBackground();
    }

    @Override
    public MainFrame.WindowSize dimensions() {
        Dimension dimension = panel.getPreferredSize();
        return new MainFrame.WindowSize(dimension.width, dimension.height);
    }

    @Override
    public Object self() {
        return panel;
    }

    @Override
    public void add(software.ulpgc.view.display.Component component) {
        panel.add((java.awt.Component) component.self());
        components.put(component.name(),component);
    }

    @Override
    public software.ulpgc.view.display.Component get(String name) {
        try {
            assert components.containsKey(name);
            return components.get(name);
        } catch (AssertionError a){
            throw new RuntimeException(a);
        }
    }

    @Override
    public void draw(String paint, String color) {
        panel.draw(paint,color);
    }

    protected static class DrawingPanel extends JPanel{
        private String paint;
        private Color color;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            int panelWidth = getWidth();
            int panelHeight = getHeight();


            int symbolWidth = (int) (panelWidth * 0.9);
            int symbolHeight = (int) (panelHeight * 0.9);


            int x = (panelWidth - symbolWidth) / 2;
            int y = (panelHeight - symbolHeight) / 2;

            Font font = new Font("Arial", Font.BOLD, Math.min(symbolWidth/paint.length(), symbolHeight));
            g2.setFont(font);
            g2.setColor(color);

            FontMetrics metrics = g2.getFontMetrics(font);
            int symbolX = x + (symbolWidth - metrics.stringWidth(paint)) / 2;
            int symbolY = y + ((symbolHeight - metrics.getHeight()) / 2) + metrics.getAscent();

            g2.drawString(paint, symbolX, symbolY);
        }

        protected void draw(String paint, String color){
            this.color = Color.getColor(color);
            this.paint = paint;
            repaint();
            revalidate();
        }
    }


}
