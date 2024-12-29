package software.external.control.deserializers;

import com.google.gson.GsonBuilder;
import software.external.model.Jsons.ErrorResponse;
import software.external.model.Jsons.ExchangeRateResponse;
import software.ulpgc.control.deserializers.ExchangeRateDeserializer;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.model.ExchangeRate;
import software.ulpgc.model.Histogram;

import java.time.*;
import java.util.ArrayList;
import java.util.List;


public class GsonJsonExchangeRateDeserializer implements ExchangeRateDeserializer {

    private final CurrencyReference reference;

    public GsonJsonExchangeRateDeserializer(CurrencyReference reference) {
        this.reference = reference;
    }

    @Override
    public Histogram deserialize(String json) {
        ExchangeRateResponse response = new GsonBuilder()
                .create()
                .fromJson(json, ExchangeRateResponse.class);
        check(response);
        List<ExchangeRate> rates = new ArrayList<>();
        response.rates().forEach((code, rate) -> adapt(code, rate, rates, response));
        return new Histogram(rates);
    }

    private void adapt(String code, Double rate, List<ExchangeRate> rates, ExchangeRateResponse response) {
        rates.add(new ExchangeRate(
                reference.get(response.base()),
                reference.get(code),
                toZonedDateTime(response.date()),
                rate
        ));
    }

    private ZonedDateTime toZonedDateTime(String date) {
        return LocalDateTime.of(LocalDate.parse(date), LocalTime.now())
                .atZone(ZoneId.systemDefault());
    }

    private static void check(ExchangeRateResponse response) {
        if (!response.success()){
            ErrorResponse error = response.error();
            throw new RuntimeException(error.toString());
        }
    }
}
