package software.external.view.display;

import software.ulpgc.view.display.Component;
import software.ulpgc.view.display.MainFrame;
import software.ulpgc.view.display.PanelDisplay;

import java.awt.*;

import static software.external.view.SwingMainFrame.positionOf;

public class SwingManagedPanelDisplay extends SwingPanelDisplay implements PanelDisplay.ManagedPanelDisplay {

    public SwingManagedPanelDisplay(String name, Color color, MainFrame.WindowSize size, LayoutManager manager){
        super(name, color, size);
        super.panel.setLayout(manager);
    }

    @Override
    public void add(Component component, String position) {
        super.panel.add((java.awt.Component) component.self(),positionOf(panel.getLayout().getClass(), position));
        super.components.put(component.name(),component);
    }
}
