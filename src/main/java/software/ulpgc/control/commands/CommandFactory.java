package software.ulpgc.control.commands;

import software.ulpgc.control.commands.builders.ExchangeHistoricMoneyCommandBuilder;
import software.ulpgc.control.commands.builders.ExchangeMoneyCommandBuilder;
import software.ulpgc.control.commands.builders.LoadCurrenciesCommandBuilder;


public class CommandFactory {
    public LoadCurrenciesCommandBuilder loadCurrenciesCommandBuilder(){
        return new LoadCurrenciesCommandBuilder();
    }

    public ExchangeMoneyCommandBuilder exchangeMoneyCommandBuilder(){
        return new ExchangeMoneyCommandBuilder();
    }

    public ExchangeHistoricMoneyCommandBuilder exchangeHistoricMoneyCommandBuilder(){
        return new ExchangeHistoricMoneyCommandBuilder();
    }
}
