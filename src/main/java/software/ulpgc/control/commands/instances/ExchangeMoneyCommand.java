package software.ulpgc.control.commands.instances;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.loaders.ExchangeRateLoader;

import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;
import software.ulpgc.model.Money;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.dialog.Text;
import software.ulpgc.view.display.output.MoneyDisplay;

import java.io.IOException;
import java.util.List;

public class ExchangeMoneyCommand implements Command {
    private final Text amount;
    private final Selector<Currency> from;
    private final Selector.MultipleSelector<Currency> to;
    private final ExchangeRateLoader loader;
    private final MoneyDisplay base;
    private final MoneyDisplay.ListedMoneyDisplay currencies;
    private final String token;

    public ExchangeMoneyCommand(Input.Exchange input) {
        this.token = input.token();
        this.amount = input.amount();
        this.from = input.from();
        this.to = input.to();
        this.loader = input.loader();
        this.base = input.base();
        this.currencies = input.currencies();
    }

    public interface Input extends Command.Input.API {
        Selector<Currency> from();
        Selector.MultipleSelector<Currency> to();
        Text amount();
        interface Exchange extends Input{
            ExchangeRateLoader loader();
            MoneyDisplay base();
            MoneyDisplay.ListedMoneyDisplay currencies();
        }
    }

    @Override
    public void execute() {
        try {
            double amount = Double.parseDouble(this.amount.get());
            Histogram histogram = loader.load(token,from.selected(), to.selections());
            List<Money> result = histogram.stream()
                    .map(r -> new Money(amount * r.rate(), r.to())).toList();

            base.show(new Money(amount,from.selected()));
            currencies.show(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
