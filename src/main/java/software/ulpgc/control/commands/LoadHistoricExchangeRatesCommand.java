package software.ulpgc.control.commands;

import software.ulpgc.control.loaders.HistoricExchangeRateLoader;



import java.io.IOException;

import java.time.ZonedDateTime;


public class LoadHistoricExchangeRatesCommand implements Command{

    private final HistoricExchangeRateLoader loader;
    private final Input input;
    private final Output output;

    public LoadHistoricExchangeRatesCommand(HistoricExchangeRateLoader loader, Input input, Output output) {
        this.loader = loader;
        this.input = input;
        this.output = output;
    }

    public interface Input extends LoadExchangeRatesCommand.Input {
        ZonedDateTime since();
        ZonedDateTime until();
    }

    public interface Output extends LoadExchangeRatesCommand.Output { }

    @Override
    public void execute() {
        try {
            output.set(loader.load(input.token(),input.from(),input.to(),input.since(),input.until()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
