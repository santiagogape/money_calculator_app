package software.ulpgc.control.commands.builders;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.commands.instances.ExchangeMoneyCommand;
import software.ulpgc.control.loaders.ExchangeRateLoader;

import software.ulpgc.model.Currency;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.dialog.Text;
import software.ulpgc.view.display.output.MoneyDisplay;

public class ExchangeMoneyCommandBuilder implements CommandBuilder{

    private ExchangeMoneyCommand.Input.Exchange input;

    public ExchangeMoneyCommandBuilder with(Text amount,
              Selector<Currency> from,
              Selector.MultipleSelector<Currency> to,
              MoneyDisplay base,
              MoneyDisplay.ListedMoneyDisplay currencies,
              ExchangeRateLoader loader,
              String token){
        this.input = new ExchangeMoneyCommand.Input.Exchange() {
            @Override
            public String token() {
                return token;
            }


            @Override
            public Selector<Currency> from() {
                return from;
            }

            @Override
            public Selector.MultipleSelector<Currency> to() {
                return to;
            }

            @Override
            public Text amount() {
                return amount;
            }

            @Override
            public ExchangeRateLoader loader() {
                return loader;
            }

            @Override
            public MoneyDisplay base() {
                return base;
            }

            @Override
            public MoneyDisplay.ListedMoneyDisplay currencies() {
                return currencies;
            }
        };
        return this;
    }

    @Override
    public Command build() {
        return new ExchangeMoneyCommand(input);
    }
}
