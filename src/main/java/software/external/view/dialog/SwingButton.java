package software.external.view.dialog;

import software.ulpgc.view.dialog.Button;

import javax.swing.*;

public class SwingButton implements Button {
    private final JButton button;

    public SwingButton(String name, String label) {
        button = new JButton(label);
        button.setName(name);
    }

    @Override
    public String Text() {
        return button.getText();
    }

    @Override
    public void Text(String label) {
        button.setText(label);
    }

    @Override
    public void perform(Action action) {
        button.addActionListener(_ -> action.act());
    }

    @Override
    public String name() {
        return button.getName();
    }

    @Override
    public Object self() {
        return button;
    }
}
