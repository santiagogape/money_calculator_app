package software.external.model.Jsons;

import java.util.Map;

public record HistoryResponse(
        boolean success,
        ErrorResponse error,
        String start_date,
        String end_date,
        String base,
        Map<String, Map<String, Double>> rates
) {
}
