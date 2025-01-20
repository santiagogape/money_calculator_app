package software.ulpgc.control.commands.instances;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.loaders.HistoricExchangeRateLoader;
import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;
import software.ulpgc.view.dialog.DateChooser;
import software.ulpgc.view.dialog.Selector;
import software.ulpgc.view.dialog.Text;
import software.ulpgc.view.display.output.LineChartDisplay;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ExchangeHistoricMoneyCommand implements Command {

    private final Selector<Currency> from;
    private final Selector.MultipleSelector<Currency> to;
    private final HistoricExchangeRateLoader loader;
    private final LineChartDisplay<Histogram> chart;
    private final String token;
    private final DateChooser since;
    private final Text amount;


    public ExchangeHistoricMoneyCommand(Input input) {
        this.token = input.token();
        this.from = input.from();
        this.to = input.to();
        since = input.since();
        this.loader = input.loader();
        this.chart = input.chart();
        this.amount = input.amount();
    }

    public interface Input extends ExchangeMoneyCommand.Input {
        LineChartDisplay<Histogram> chart();
        HistoricExchangeRateLoader loader();
        DateChooser since();
    }

    @Override
    public void execute() {
        try {
            Histogram histogram = loader.load(token,from.selected(), to.selections(),since.date(), ZonedDateTime.now());
            chart.show(histogram, Double.parseDouble(this.amount.get()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
