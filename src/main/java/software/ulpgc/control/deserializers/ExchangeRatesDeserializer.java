package software.ulpgc.control.deserializers;

import software.ulpgc.model.Histogram;

@FunctionalInterface
public interface ExchangeRatesDeserializer {
    Histogram deserialize(String json);
}
