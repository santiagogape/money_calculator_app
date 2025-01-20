package software.external.view.dialog;

import software.ulpgc.view.dialog.Selector;

import javax.swing.*;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class SwingUniqueSelector<T> implements Selector<T> {

    private final JComboBox<T> selector;

    @SuppressWarnings("unchecked")
    public SwingUniqueSelector(String name, List<T> options) {
        this.selector = new JComboBox<>((T[]) options.toArray());
        selector.setName(name);
        this.selector.setSelectedItem(options.getFirst());
    }

    @Override
    public List<T> options() {
        return IntStream.range(0,selector.getItemCount()).mapToObj(selector::getItemAt).toList();
    }

    @Override
    public T selected() {
        return (T) selector.getSelectedItem();
    }

    @Override
    public void update(List<T> options) {
        T selected = (T) selector.getSelectedItem();
        selector.setModel(new DefaultComboBoxModel<>((T[]) options.toArray()));
        if (options.contains(selected)) selector.setSelectedItem(selected);
    }

    @Override
    public void perform(Action action) {
        selector.addActionListener(_ -> action.act());
    }

    @Override
    public String name() {
        return selector.getName();
    }

    @Override
    public Object self() {
        return selector;
    }
}
