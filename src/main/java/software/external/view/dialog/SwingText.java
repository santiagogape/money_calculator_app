package software.external.view.dialog;

import software.ulpgc.view.dialog.Text;

import javax.swing.*;

public class SwingText implements Text {

    private final JTextField text;

    public SwingText(String name, int length){
        text = new JTextField(length);
        text.setName(name);
    }

    @Override
    public String name() {
        return text.getName();
    }

    @Override
    public Object self() {
        return text;
    }

    @Override
    public String get() {
        return text.getText();
    }
}
