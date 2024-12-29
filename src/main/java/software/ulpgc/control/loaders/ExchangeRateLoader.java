package software.ulpgc.control.loaders;

import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;

import java.io.IOException;
import java.util.List;

public interface ExchangeRateLoader {
    Histogram load(String token, Currency from, List<Currency> to) throws IOException;

}
