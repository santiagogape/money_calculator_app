package software.ulpgc.control.loaders;

import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public interface HistoricExchangeRateLoader {

    Histogram load(String token, Currency from, List<Currency> to, ZonedDateTime since, ZonedDateTime until) throws IOException;
}
