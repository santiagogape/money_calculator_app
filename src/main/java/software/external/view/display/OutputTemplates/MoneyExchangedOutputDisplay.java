package software.external.view.display.OutputTemplates;

import software.external.view.display.SwingManagedPanelDisplay;
import software.external.view.display.SwingMoneyDisplay;

import java.awt.*;

public class MoneyExchangedOutputDisplay extends SwingManagedPanelDisplay {
    private final SwingMoneyDisplay base;
    private final SwingMoneyDisplay converted;

    public MoneyExchangedOutputDisplay(ManagedPanelDisplay back) {
        super("MONEY", back.color(), back.dimensions(), new GridLayout(1,2));
        base = new SwingMoneyDisplay("BASE",back.color(),back.dimensions().verticalSplit());
        converted = new SwingMoneyDisplay("CONVERTED",back.color(),back.dimensions().verticalSplit());
        add(base);
        add(converted);

    }

    public SwingMoneyDisplay base() {
        return base;
    }

    public SwingMoneyDisplay converted() {
        return converted;
    }
}
