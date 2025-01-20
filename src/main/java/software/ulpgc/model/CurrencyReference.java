package software.ulpgc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyReference {
    private final Map<String, Currency> currencies;
    private final Map<String, Currency> filtered;

    public CurrencyReference() {
        this.currencies = new HashMap<>();
        this.filtered = new HashMap<>();
    }

    public void add(Currency currency){
        currencies.putIfAbsent(currency.code(), currency);
    }

    public void add(List<Currency> currencies){
        currencies.forEach(this::add);
    }

    public List<Currency> allCurrencies() {
        return currencies.values().stream().sorted().toList();
    }

    public List<Currency> currencies() {
        return filtered.values().stream().sorted().toList();
    }

    public List<Currency> exclude(Currency option){
        return currencies().stream().filter(o -> !o.equals(option)).toList();
    }

    public Currency get(String key) {
        return filtered.get(key);
    }

    public Currency search(String key) {
        return currencies.get(key);
    }

    public void setVisible(List<String> codes){
        codes.forEach(this::setVisible);
    }

    public void setVisible(String code) {
        filtered.putIfAbsent(code, currencies.get(code));
    }

    public static String codesOf(List<Currency> to) {
        return to.stream().map(Currency::code).collect(Collectors.joining(","));
    }
}
