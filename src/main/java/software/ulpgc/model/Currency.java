package software.ulpgc.model;

import java.util.*;

import static java.util.Currency.getInstance;

public record Currency(String code, String name) implements Comparable<Currency>{

    @Override
    public String toString() {
        return code + " ("+ symbol() +") - " + name;
    }
    public String symbol(){
        Map<String, String> others = Map.of("BTC", "₿",
                "CNH", "¥",
                "GGP", "£",
                "IMP", "£",
                "JEP", "£");
        if (others.containsKey(code)) return others.get(code);
        return getInstance(code).getSymbol(Locale.UK);
    }


    @Override
    public int compareTo(Currency o) {
        return this.code.compareTo(o.code);
    }
}
