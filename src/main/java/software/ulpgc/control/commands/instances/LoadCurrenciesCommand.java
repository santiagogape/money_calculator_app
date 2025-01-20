package software.ulpgc.control.commands.instances;

import software.ulpgc.control.commands.Command;
import software.ulpgc.control.loaders.CurrenciesLoader;

import software.ulpgc.model.CurrencyReference;

import java.io.IOException;



public class LoadCurrenciesCommand implements Command {
    private final CurrenciesLoader loader;
    private final String token;
    private final CurrencyReference reference;



    public LoadCurrenciesCommand(Input input){
        token = input.token();
        loader = input.loader();
        this.reference = input.reference();
    }

    public interface Input extends Command.Input.API{
        CurrenciesLoader loader();
        CurrencyReference reference();
    }


    @Override
    public void execute() {
        try {
            reference.add(loader.load(token));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
