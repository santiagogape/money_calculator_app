package software.ulpgc.control.deserializers;

import software.ulpgc.model.Histogram;

public interface ExchangeRateDeserializer {
    Histogram deserialize(String json);
}
