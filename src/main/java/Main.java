import software.external.control.deserializers.GsonJsonCurrenciesDeserializer;
import software.external.control.deserializers.GsonJsonExchangeRatesDeserializer;
import software.external.control.deserializers.GsonJsonHistoricExchangeRatesDeserializer;
import software.external.control.loaders.*;

import software.ulpgc.control.commands.*;
import software.ulpgc.model.API;
import software.ulpgc.model.CurrencyReference;

import java.util.*;


public class Main {
    // fb9kZFwNkyv1KrmgjEKXs0QZMZB6N4aM
    // https://apilayer.com/marketplace/fixer-api?live_demo=show
    // https://apilayer.com/marketplace/fixer-api#documentation-tab
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TOKEN = "fb9kZFwNkyv1KrmgjEKXs0QZMZB6N4aM";
    private static final String PATH = "C:\\Users\\santi\\Desktop\\EII3º\\T1\\IS2\\PROYECTOS\\money_calculator_app\\src\\main\\resources\\JSON\\";


    public static void main(String[] args) {
        APITest();
    }

    private static void APITest() {
        CurrencyReference  reference = new CurrencyReference();
        API api = api();
        MainAppTest app = new MainAppTest(
                reference,
                new CommandFactory(),
                new HttpClientCurrenciesLoader(api,new GsonJsonCurrenciesDeserializer()),
                new HttpClientExchangeRatesLoader(api, new GsonJsonExchangeRatesDeserializer(reference)),
                new HttpClientHistoricExchangeRatesLoader(api, new GsonJsonHistoricExchangeRatesDeserializer(reference)),
                TOKEN
        );
        app.start();
    }


    private static API api() {
        //noinspection SpellCheckingInspection
        return new API("http","api.apilayer.com","apikey",endpoints());
    }

    private static Map<API.EndPointName, API.EndPoint> endpoints() {
        Map<API.EndPointName, API.EndPoint> endpoints = new HashMap<>();

        endpoints.put(API.EndPointName.currencies, new API.EndPoint("fixer/symbols",new HashMap<>()));

        HashMap<API.Parameter,String> latest = new HashMap<>();
        latest.put(API.Parameter.from, "base");
        latest.put(API.Parameter.to, "symbols");
        endpoints.put(API.EndPointName.rates, new API.EndPoint("fixer/latest", latest));

        HashMap<API.Parameter, String> historic = new HashMap<>();
        historic.put(API.Parameter.from, "base");
        historic.put(API.Parameter.to, "symbols");
        historic.put(API.Parameter.since, "start_date");
        historic.put(API.Parameter.until, "end_date");

        //noinspection SpellCheckingInspection
        endpoints.put(API.EndPointName.historic, new API.EndPoint("fixer/timeseries",historic));

        return endpoints;
    }


}
