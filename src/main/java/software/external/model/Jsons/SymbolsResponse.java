package software.external.model.Jsons;

import java.util.Map;

public record SymbolsResponse(
        boolean success,
        ErrorResponse error,
        Map<String,String> symbols
) {
}
