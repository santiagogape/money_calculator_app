package software.external.control.deserializers;

import com.google.gson.GsonBuilder;
import software.external.model.Jsons.ErrorResponse;

import software.external.model.Jsons.HistoryResponse;
import software.ulpgc.control.deserializers.ExchangeRateDeserializer;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.model.ExchangeRate;
import software.ulpgc.model.Histogram;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonJsonHistoricExchangeRateDeserializer implements ExchangeRateDeserializer {

    private final CurrencyReference reference;

    public GsonJsonHistoricExchangeRateDeserializer(CurrencyReference reference) {
        this.reference = reference;
    }

    @Override
    public Histogram deserialize(String json) {
        HistoryResponse response = new GsonBuilder()
                .create()
                .fromJson(json, HistoryResponse.class);
        check(response);
        List<ExchangeRate> rates = new ArrayList<>();
        response.rates().forEach((date, exchangeRates) -> adapt(date, exchangeRates, rates, response));
        return new Histogram(rates);
    }

    private void adapt(String date, Map<String,Double> exchangeRates, List<ExchangeRate> rates, HistoryResponse response) {
         exchangeRates.forEach(
                 (code, rate) ->
                         rates.add(new ExchangeRate(
                                 reference.get(response.base()),
                                 reference.get(code),
                                 toZonedDateTime(date),
                                 rate
                         ))

         );
    }

    private ZonedDateTime toZonedDateTime(String date) {
        return LocalDateTime.of(LocalDate.parse(date), LocalTime.now())
                .atZone(ZoneId.systemDefault());
    }

    private static void check(HistoryResponse response) {
        if (!response.success()){
            ErrorResponse error = response.error();
            throw new RuntimeException(error.toString());
        }
    }
}
