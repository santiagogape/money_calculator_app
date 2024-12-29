package software.ulpgc.control.deserializers;

import software.ulpgc.model.Currency;

import java.util.List;

public interface CurrenciesDeserializer {
    List<Currency> deserializer(String json);
}
