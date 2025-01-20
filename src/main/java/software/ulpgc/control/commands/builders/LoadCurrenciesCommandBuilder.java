package software.ulpgc.control.commands.builders;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.commands.instances.LoadCurrenciesCommand;
import software.ulpgc.control.loaders.CurrenciesLoader;
import software.ulpgc.model.CurrencyReference;

public class LoadCurrenciesCommandBuilder implements CommandBuilder{

    private LoadCurrenciesCommand.Input input;

    public LoadCurrenciesCommandBuilder with(CurrenciesLoader loader,
              CurrencyReference reference,
              String token){
        this.input = new LoadCurrenciesCommand.Input() {
            @Override
            public CurrenciesLoader loader() {
                return loader;
            }

            @Override
            public String token() {
                return token;
            }

            @Override
            public CurrencyReference reference() {
                return reference;
            }
        };
        return this;
    }

    @Override
    public Command build() {
        return new LoadCurrenciesCommand(input);
    }
}
