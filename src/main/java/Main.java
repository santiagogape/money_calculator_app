import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.external.control.deserializers.GsonJsonCurrenciesDeserializer;
import software.external.control.deserializers.GsonJsonExchangeRateDeserializer;
import software.external.control.deserializers.GsonJsonHistoricExchangeRateDeserializer;
import software.external.control.loaders.HttpClientCurrencyLoader;

import software.external.control.loaders.HttpClientExchangeRatesLoader;
import software.external.control.loaders.HttpClientHistoricExchangeRatesLoader;
import software.external.model.Jsons.ZonedDateTimeAdapter;
import software.ulpgc.control.commands.Command;
import software.ulpgc.control.commands.LoadCurrenciesCommand;
import software.ulpgc.control.commands.LoadExchangeRatesCommand;
import software.ulpgc.control.commands.LoadHistoricExchangeRatesCommand;
import software.ulpgc.control.loaders.CurrencyLoader;
import software.ulpgc.model.API;
import software.ulpgc.model.Currency;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.model.Histogram;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.*;


public class Main {
    // fb9kZFwNkyv1KrmgjEKXs0QZMZB6N4aM
    // https://apilayer.com/marketplace/fixer-api?live_demo=show
    // https://apilayer.com/marketplace/fixer-api#documentation-tab
    @SuppressWarnings("SpellCheckingInspection")
    private static final String token = "fb9kZFwNkyv1KrmgjEKXs0QZMZB6N4aM";
    private static final String PATH = "C:\\Users\\santi\\Desktop\\EII3º\\T1\\IS2\\PROYECTOS\\money_calculator_app\\src\\main\\resources\\JSON\\";

    public static List<Currency> readCurrencies() {
        String json;
        try {
            json = Files.readString(Path.of(PATH+"all.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new GsonJsonCurrenciesDeserializer().deserializer(json);
    }

    public static void main(String[] args) {
        //load from api, read from past response
        List<Currency> currencies = readCurrencies(); //loadCurrencies();
        CurrencyReference reference = new CurrencyReference();
        reference.add(currencies);
        reference.setVisible(List.of("USD", "EUR", "GBP", "CHF", "JPY", "CAD", "CNY", "RUB", "MXN", "KRW"));
        //write(reference,PATH+"reference.json");
        loading(reference);
    }

    private static void write(Object reference, String path) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(reference, writer);
            System.out.println("stored "+ path);
        } catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
        }
    }

    private static void loading(CurrencyReference reference) {
        Histogram dailyRates = loadRates(reference, base(reference), currencies(reference));
        System.out.println(dailyRates);

        Histogram historic = loadHistoric(reference,
                base(reference),
                currencies(reference),
                ZonedDateTime.now().minusMonths(1),
                ZonedDateTime.now());
        System.out.println(historic);

        write(dailyRates,PATH+"today.json");
        write(historic,PATH+"month.json");
    }

    private static Currency base(CurrencyReference reference) {
        return reference.get("EUR");
    }

    private static List<Currency> currencies(CurrencyReference reference) {
        Set<String> codes = new HashSet<>(Set.of("USD", "EUR", "GBP", "CHF", "JPY", "CAD", "CNY", "RUB", "MXN", "KRW"));
        codes.remove("EUR"); // exclude base
        List<Currency> currencies = new ArrayList<>();
        codes.forEach(c -> currencies.add(reference.get(c)));
        return currencies;
    }

    private static Histogram loadHistoric(CurrencyReference reference, Currency base, List<Currency> currencies, ZonedDateTime since, ZonedDateTime until) {
        LoadHistoricExchangeRatesCommand.Output output = getOutput();
        Command loadHistoricRates = new LoadHistoricExchangeRatesCommand(
                new HttpClientHistoricExchangeRatesLoader(API(), new GsonJsonHistoricExchangeRateDeserializer(reference)),
                input(base, currencies, since, until),
                output
        );
        loadHistoricRates.execute();
        return output.result();
    }

    private static LoadHistoricExchangeRatesCommand.Input input(Currency base, List<Currency> currencies, ZonedDateTime since, ZonedDateTime until) {
        return new LoadHistoricExchangeRatesCommand.Input() {
            @Override
            public ZonedDateTime since() {
                return since;
            }

            @Override
            public ZonedDateTime until() {
                return until;
            }

            @Override
            public Currency from() {
                return base;
            }

            @Override
            public List<Currency> to() {
                return currencies;
            }

            @Override
            public String token() {
                return token;
            }
        };
    }

    private static LoadHistoricExchangeRatesCommand.Output getOutput() {
        return new LoadHistoricExchangeRatesCommand.Output() {
            Histogram histogram;
            @Override
            public void set(Histogram data) {
                histogram = data;
            }

            @Override
            public Histogram result() {
                return histogram;
            }
        };
    }

    private static Histogram loadRates(CurrencyReference reference, Currency base, List<Currency> currencies) {
        LoadExchangeRatesCommand.Output output = getOutput();
        Command loadRates =
                new LoadExchangeRatesCommand(
                        ratesLoader(reference),
                        input(base,currencies,null,null),
                        output
                );

        loadRates.execute();
        return output.result();
    }

    private static HttpClientExchangeRatesLoader ratesLoader(CurrencyReference reference) {
        return new HttpClientExchangeRatesLoader(API(), new GsonJsonExchangeRateDeserializer(reference));
    }

    private static List<Currency> loadCurrencies() {
        LoadCurrenciesCommand.Output output = output();
        Command command = new LoadCurrenciesCommand(currencyLoader(), input(null,null,null,null),output);
        command.execute();
        return output.currencies();
    }

    private static CurrencyLoader currencyLoader() {
        return new HttpClientCurrencyLoader(API(),new GsonJsonCurrenciesDeserializer());
    }

    private static LoadCurrenciesCommand.Output output() {
        return new LoadCurrenciesCommand.Output() {
            private final List<Currency> currencies = new ArrayList<>();
            @Override
            public void add(Currency currency) {
                currencies.add(currency);
            }

            @Override
            public List<Currency> currencies() {
                return currencies;
            }
        };
    }

    private static API API() {
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
