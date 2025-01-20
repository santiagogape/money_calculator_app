package software.external.view.display.InputTemplates;

import software.external.view.SwingLabel;
import software.external.view.dialog.SwingMultipleSelector;
import software.external.view.display.SwingManagedPanelDisplay;
import software.external.view.display.SwingScrollPanelDisplay;
import software.ulpgc.model.Currency;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.view.Label;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.display.PanelDisplay;

import java.awt.*;

public class CurrenciesInputDisplay extends SwingManagedPanelDisplay {

    private final Selector.MultipleSelector<Currency> currencies;

    public CurrenciesInputDisplay(CurrencyReference reference, PanelDisplay.ManagedPanelDisplay base) {
        super("CURRENCIES", base.color(), base.dimensions(), new BorderLayout());
        Label format = new SwingLabel("FORMAT","select Currencies:");
        currencies = new SwingMultipleSelector<>(reference.allCurrencies().stream().filter(c -> !reference.currencies().contains(c)).toList());
        ManagedPanelDisplay up = new SwingManagedPanelDisplay("UP",base.color(),null,new FlowLayout());
        SwingScrollPanelDisplay down = new SwingScrollPanelDisplay("SCROLL",base.color(), null,currencies);
        up.add(format);
        add(up, "north");
        add(down, "center");
    }

    public Selector.MultipleSelector<Currency> currencies() {
        return currencies;
    }
}
