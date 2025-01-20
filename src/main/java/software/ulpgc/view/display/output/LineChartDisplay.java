package software.ulpgc.view.display.output;
import software.ulpgc.view.display.PanelDisplay;

public interface LineChartDisplay<T> extends PanelDisplay {
    void show(T data);

    void show(T histogram, double amount);
}
