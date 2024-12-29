package software.ulpgc.control.loaders;

import software.ulpgc.model.Currency;

import java.io.IOException;
import java.util.List;

public interface CurrencyLoader {
    List<Currency> load(String token) throws IOException;
}
