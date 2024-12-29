package software.external.model.Jsons;

import java.util.Map;

public record ExchangeRateResponse(
        boolean success,
        ErrorResponse error,
        String base,
        String date,
        Map<String, Double> rates
){
}
