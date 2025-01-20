package software.external.view.display;

import software.ulpgc.view.display.Component;
import software.ulpgc.view.display.MainFrame;
import software.ulpgc.view.display.PanelDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingPanelDisplay implements PanelDisplay {
    protected final JPanel panel;
    protected final Map<String, Component> components;

    @Override
    public void add(Component component) {
        panel.add(component.name(), (java.awt.Component) component.self());
        components.put(component.name(),component);
    }

    @Override
    public Component get(String name) {
        try {
          assert components.containsKey(name);
          return components.get(name);
        } catch (AssertionError a){
            throw new RuntimeException(a);
        }
    }

    public SwingPanelDisplay(String name, Color color, MainFrame.WindowSize size){
        panel = new JPanel();
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
        if (size != null) panel.setPreferredSize(new Dimension(size.x(), size.y()));
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

}
