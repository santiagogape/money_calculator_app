package software.ulpgc.model;

public class MoneyExchanger {
    public Money exchange(Money money, ExchangeRate rate){
        return new Money(money.amount() * rate.rate(), rate.to());
    }
}
