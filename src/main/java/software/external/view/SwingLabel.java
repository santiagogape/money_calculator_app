package software.external.view;

import software.ulpgc.view.Label;

import javax.swing.*;

public class SwingLabel implements Label {
    private  final JLabel label;
    public SwingLabel(String name, String text){
        label = new JLabel(text);
        label.setName(name);
    }
    @Override
    public String name() {
        return label.getName();
    }

    @Override
    public Object self() {
        return label;
    }

    @Override
    public void text(String text) {
        label.setText(text);
    }
}
