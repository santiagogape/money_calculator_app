package software.external.view.dialog;

import software.ulpgc.view.dialog.Selector;

import javax.swing.*;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.IntStream;

public class SwingMultipleSelector<T> implements Selector.MultipleSelector<T> {
    private final JList<T> list;
    private final Set<Integer> indexes = new HashSet<>();

    public SwingMultipleSelector(List<T> options) {
        list = new JList<>(new Vector<>(options));
        list.setVisibleRowCount(1);
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // Evitar la selección automática; se controlará manualmente
                if (isSelectedIndex(index0)) {
                    removeSelectionInterval(index0, index1); // Deseleccionar si ya está seleccionado
                } else {
                    super.setSelectionInterval(index0, index1); // Seleccionar si no está seleccionado
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                int index = list.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (list.isSelectedIndex(index)) {
                        indexes.add(index);
                    } else {
                        indexes.remove(index);
                    }
                    indexes.forEach(i ->  list.addSelectionInterval(i,i));
                }

            }
        });
    }

    @Override
    public List<T> options() {
        return IntStream.range(0,list.getModel().getSize())
                .mapToObj(i -> list.getModel().getElementAt(i))
                .toList();
    }

    @Override
    public T selected() {
        return list.getSelectedValue();
    }

    @Override
    public List<T> selections() {
        return list.getSelectedValuesList();
    }

    @Override
    public void selectAll() {
        list.clearSelection();
        list.setSelectionInterval(0, list.getModel().getSize() - 1);
        indexes.clear();
        IntStream.range(0,list.getModel().getSize() - 1).forEach(indexes::add);
    }

    @Override
    public void clearSelection() {
        list.clearSelection();
        indexes.clear();
    }

    @Override
    public void update(List<T> options) {
        List<T> previous = list.getSelectedValuesList().stream().filter(options::contains).toList();
        list.setListData(new Vector<>(options));
        previous.forEach(a->list.setSelectedValue(a,true));
        indexes.clear();
        previous.stream().filter(options::contains).map(options::indexOf).forEach(indexes::add);
    }

    @Override
    public void perform(Action action) {
        action.act();
    }

    @Override
    public String name() {
        return list.getName();
    }

    @Override
    public Object self() {
        return list;
    }
}
