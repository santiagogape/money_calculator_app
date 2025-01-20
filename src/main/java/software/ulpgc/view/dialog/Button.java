package software.ulpgc.view.dialog;

import software.ulpgc.view.display.Component;
import software.ulpgc.control.Performer;

public interface Button extends Component, Performer {
    String Text();
    void Text(String label);
}
