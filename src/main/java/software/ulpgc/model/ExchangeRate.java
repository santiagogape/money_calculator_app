package software.ulpgc.model;

public record ExchangeRate(Currency from, Currency to, java.time.ZonedDateTime date, double rate){
}
