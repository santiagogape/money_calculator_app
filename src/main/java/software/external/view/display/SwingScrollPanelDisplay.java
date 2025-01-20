package software.external.view.display;

import software.ulpgc.view.display.Component;
import software.ulpgc.view.display.MainFrame;
import software.ulpgc.view.display.PanelDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingScrollPanelDisplay implements PanelDisplay {

    private final JScrollPane scroll;
    protected final Map<String,Component> components;

    public SwingScrollPanelDisplay(String name, Color color, MainFrame.WindowSize o, Component component) {
        scroll = new JScrollPane((java.awt.Component) component.self());
        scroll.setName(name);
        scroll.setBackground(color);
        if (o!=null) scroll.setPreferredSize(new Dimension(o.x(),o.y()));
        components =new HashMap<>();
        components.put(component.name(),component);
    }

    @Override
    public void revalidate() {
        scroll.revalidate();
    }

    @Override
    public Color color() {
        return scroll.getBackground();
    }

    @Override
    public MainFrame.WindowSize dimensions() {
        return new MainFrame.WindowSize(scroll.getSize().width, scroll.getSize().height);
    }

    @Override
    public void add(Component component) {
        assert !components.containsKey(component.name());
        components.put(component.name(), component);
        scroll.add((java.awt.Component) component.self());
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



    @Override
    public void removeAll() {
        components.clear();
        scroll.removeAll();
    }


    @Override
    public String name() {
        return scroll.getName();
    }

    @Override
    public Object self() {
        return scroll;
    }
}
