package software.ulpgc.control.commands;

import software.ulpgc.control.loaders.ExchangeRateLoader;
import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;

import java.io.IOException;
import java.util.List;

public class LoadExchangeRatesCommand implements Command{

    private final ExchangeRateLoader loader;
    private final Input input;
    private final Output output;

    public LoadExchangeRatesCommand(ExchangeRateLoader loader, Input input, Output output) {
        this.loader = loader;
        this.input = input;
        this.output = output;
    }

    public interface Input extends LoadCurrenciesCommand.Input {
        Currency from();
        List<Currency> to();
    }

    public interface Output{
        void set(Histogram data);
        Histogram result();
    }

    @Override
    public void execute() {
        try {
            output.set(loader.load(input.token(),input.from(),input.to()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
