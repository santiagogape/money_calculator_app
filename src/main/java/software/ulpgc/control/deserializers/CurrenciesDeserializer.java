package software.ulpgc.control.deserializers;

import software.ulpgc.model.Currency;

import java.util.List;

@FunctionalInterface
public interface CurrenciesDeserializer {
    List<Currency> deserialize(String json);
}
