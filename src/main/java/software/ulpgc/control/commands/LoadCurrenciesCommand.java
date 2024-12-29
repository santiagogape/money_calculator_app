package software.ulpgc.control.commands;

import software.ulpgc.control.loaders.CurrencyLoader;
import software.ulpgc.model.Currency;

import java.io.IOException;
import java.util.List;

public class LoadCurrenciesCommand implements Command{
    private final CurrencyLoader loader;
    private final Output output;
    private final Input input;

    public LoadCurrenciesCommand(CurrencyLoader loader, Input input, Output output){
        this.loader = loader;
        this.output = output;
        this.input = input;
    }

    @Override
    public void execute() {
        try {
            loader.load(input.token()).forEach(output::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Output {
        void add(Currency currency);
        List<Currency> currencies();
    }

    public interface Input {
        String token();
    }
}
