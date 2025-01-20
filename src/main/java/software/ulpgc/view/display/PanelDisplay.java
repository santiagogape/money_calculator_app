package software.ulpgc.view.display;

import java.awt.*;
public interface PanelDisplay extends Component{
    void revalidate();
    Color color();
    MainFrame.WindowSize dimensions();
    void add(Component component);
    Component get(String name);
    void removeAll();
    interface ManagedPanelDisplay extends PanelDisplay{
        void add(Component component, String position);

    }
}
