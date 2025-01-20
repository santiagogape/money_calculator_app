package software.ulpgc.view.dialog;

import software.ulpgc.view.display.Component;
import software.ulpgc.control.Performer;

import java.util.List;

public interface Selector<T> extends Component, Performer {
    List<T> options();
    void update(List<T> options);
    T selected();
    interface MultipleSelector<T> extends Selector<T>{
        List<T> selections();
        void selectAll();
        void clearSelection();
    }
}
