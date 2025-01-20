package software.external.view;

import software.ulpgc.view.display.MainFrame;
import software.ulpgc.view.display.PanelDisplay;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.reflect.Modifier.isStatic;

public class SwingMainFrame implements MainFrame {

    private final JFrame main;

    public SwingMainFrame(String title, WindowSize size, LayoutManager manager, Color color){
        main = new JFrame(title);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setPreferredSize(new Dimension(size.x(), size.y()));
        main.setSize(new Dimension(size.x(), size.y()));
        main.setLayout(manager);
        main.setBackground(color);
    }

    @Override
    public void add(PanelDisplay panel, String position) {
        Object place = positionOf(main.getLayout().getClass(),position);
        Class<?> t = place.getClass();
        main.add((JPanel)panel.self(), t.cast(place));
    }

    public static Object positionOf(Class<?> manger, String position) {

        try {
            Field[] fields = manger.getDeclaredFields();
            Optional<Field> field = Arrays.stream(fields)
                    .filter(f -> isStatic(f.getModifiers()))
                    .filter(f->f.getName().equals(position.toUpperCase()))
                    .findFirst();
            if (field.isEmpty()) throw new IllegalArgumentException("Not found: " + position);
            return field.get().get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void show() {
        main.setVisible(true);
    }

}
