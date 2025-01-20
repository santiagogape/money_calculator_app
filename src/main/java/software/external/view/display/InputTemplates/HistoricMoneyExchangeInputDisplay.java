package software.external.view.display.InputTemplates;

import software.external.view.dialog.*;
import software.external.view.display.SwingDrawDisplay;
import software.external.view.display.SwingManagedPanelDisplay;
import software.external.view.display.SwingScrollPanelDisplay;
import software.ulpgc.model.Currency;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.view.dialog.Button;
import software.ulpgc.view.dialog.DateChooser;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.dialog.Text;
import software.ulpgc.view.display.Draw;
import software.ulpgc.view.display.PanelDisplay;

import java.awt.*;

public class HistoricMoneyExchangeInputDisplay extends SwingManagedPanelDisplay {
    private final Selector<Currency> from;
    private final Selector.MultipleSelector<Currency> to;
    private final Text value;
    private final DateChooser since;



    public HistoricMoneyExchangeInputDisplay(CurrencyReference reference, PanelDisplay.ManagedPanelDisplay base) {
        super("HISTORY", base.color(), base.dimensions(), new BorderLayout());

        from = new SwingUniqueSelector<>("FROM", reference.currencies());
        to = new SwingMultipleSelector<>(reference.exclude(from.selected()));

        from.perform(() -> {
            Currency currency = from.selected();
            to.update(reference.exclude(currency));
        });

        SwingScrollPanelDisplay scroll = new SwingScrollPanelDisplay("SCROLL",base.color(), null,to);
        Draw arrow = new SwingDrawDisplay("ARROW", base.color(),null);
        arrow.draw("→","BLACK");

        Button all = new SwingButton("ALL", "Select All");
        all.perform(() -> {
            if (all.Text().equals("select all")) {
                to.selectAll();
                all.Text("unselect all");
            } else {
                to.clearSelection();
                all.Text("select all");
            }
        });

        SwingManagedPanelDisplay up = new SwingManagedPanelDisplay("UP",base.color(),null,new BorderLayout());
        SwingManagedPanelDisplay divided = new SwingManagedPanelDisplay("DIVIDED",base.color(),null,new BorderLayout());

        ManagedPanelDisplay down = new SwingManagedPanelDisplay("DOWN",base.color(),null,new FlowLayout());

        divided.add(from,"north");
        divided.add(arrow, "center");
        divided.add(scroll, "south");

        up.add(divided, "center");
        up.add(all, "south");

        value = new SwingText("MONEY",10);
        since = new SwingDateChooser("SINCE");
        down.add(value);
        down.add(since);


        add(up,"center");
        add(down,"south");
    }

    public Selector<Currency> from() {
        return from;
    }

    public Selector.MultipleSelector<Currency> to() {
        return to;
    }

    public Text value() {
        return value;
    }

    public DateChooser since() {
        return since;
    }
}
