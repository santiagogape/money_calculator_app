package software.ulpgc.control.commands.builders;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.commands.instances.ExchangeHistoricMoneyCommand;
import software.ulpgc.control.loaders.HistoricExchangeRateLoader;
import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;
import software.ulpgc.view.dialog.DateChooser;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.dialog.Text;
import software.ulpgc.view.display.output.LineChartDisplay;

public class ExchangeHistoricMoneyCommandBuilder implements CommandBuilder {
    private ExchangeHistoricMoneyCommand.Input input;

    public ExchangeHistoricMoneyCommandBuilder with(Selector<Currency> from,
                                                    Selector.MultipleSelector<Currency> to,
                                                    Text amount, DateChooser since, LineChartDisplay<Histogram> chart,
                                                    HistoricExchangeRateLoader loader, String token){
        this.input = new ExchangeHistoricMoneyCommand.Input() {
            @Override
            public LineChartDisplay<Histogram> chart() {
                return chart;
            }

            @Override
            public HistoricExchangeRateLoader loader() {
                return loader;
            }

            @Override
            public DateChooser since() {
                return since;
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
            public String token() {
                return token;
            }

        };
        return this;
    }


    @Override
    public Command build() {
        return new ExchangeHistoricMoneyCommand(input);
    }

}
