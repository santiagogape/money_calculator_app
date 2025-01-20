package software.external.control.deserializers;


import com.google.gson.GsonBuilder;
import software.external.model.Jsons.ErrorResponse;
import software.external.model.Jsons.SymbolsResponse;
import software.ulpgc.control.deserializers.CurrenciesDeserializer;
import software.ulpgc.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class GsonJsonCurrenciesDeserializer implements CurrenciesDeserializer {


    @Override
    public List<Currency> deserialize(String json) {
        SymbolsResponse response = new GsonBuilder()
                .create()
                .fromJson(json, SymbolsResponse.class);
        check(response);
        List<Currency> currencies = new ArrayList<>();
        fill(response, currencies);
        return currencies;
    }

    private static void fill(SymbolsResponse response, List<Currency> currencies) {
        response.symbols().forEach((code, name) -> currencies.add(new Currency(code,name)));
    }

    private static void check(SymbolsResponse response) {
        if (!response.success()){
            ErrorResponse error = response.error();
            throw new RuntimeException(error.toString());
        }
    }
}
