
import software.external.view.SwingMainFrame;
import software.external.view.dialog.*;
import software.external.view.display.*;
import software.external.view.display.InputTemplates.MoneyExchangeInputDisplay;
import software.external.view.display.InputTemplates.HistoricMoneyExchangeInputDisplay;
import software.external.view.display.InputTemplates.CurrenciesInputDisplay;

import software.external.view.display.OutputTemplates.MoneyExchangedOutputDisplay;
import software.ulpgc.control.Performer;
import software.ulpgc.control.commands.CommandFactory;
import software.ulpgc.control.loaders.CurrenciesLoader;
import software.ulpgc.control.loaders.ExchangeRateLoader;
import software.ulpgc.control.loaders.HistoricExchangeRateLoader;
import software.ulpgc.model.Currency;
import software.ulpgc.model.CurrencyReference;
import software.ulpgc.model.Histogram;
import software.ulpgc.model.Money;
import software.ulpgc.view.dialog.*;
import software.ulpgc.view.dialog.Button;
import software.ulpgc.view.display.MainFrame;

import software.ulpgc.view.display.MainFrame.WindowSize;
import software.ulpgc.view.display.PanelDisplay;
import software.ulpgc.view.display.PanelDisplay.ManagedPanelDisplay;
import software.ulpgc.view.display.output.LineChartDisplay;
import software.ulpgc.view.display.output.MoneyDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class MainAppTest {

    private final CurrencyReference reference;
    private final CommandFactory factory;
    private final ExchangeRateLoader exchangeRateLoader;
    private final HistoricExchangeRateLoader historicExchangeRateLoader;
    private final String token;

    private final WindowSize corner = new WindowSize(120, 100);
    private final WindowSize result = new WindowSize(400, 400);
    private final WindowSize window = new WindowSize(700,500);
    private final WindowSize EMPTY_SPACE = new WindowSize(100, 400);
    private final Map<String, String> selections = Map.of("Exchange", "EXCHANGE", "Historic Rates", "HISTORY", "Import Currencies", "CURRENCIES");

    public MainAppTest(CurrencyReference reference,
                       CommandFactory factory,
                       CurrenciesLoader currenciesLoader,
                       ExchangeRateLoader exchangeRateLoader,
                       HistoricExchangeRateLoader historicExchangeRateLoader,
                       String token) {
        this.reference = reference;
        this.factory = factory;
        this.exchangeRateLoader = exchangeRateLoader;
        this.historicExchangeRateLoader = historicExchangeRateLoader;
        this.token = token;
        factory.loadCurrenciesCommandBuilder().with(currenciesLoader,reference,token).build().execute();
        reference.setVisible(List.of("USD", "EUR", "GBP", "CHF", "JPY", "CAD", "CNY", "RUB", "MXN", "KRW"));
    }

    public void start() {
        //window
        MainFrame mainFrame = new SwingMainFrame(
                "Money Calculator",
                window,
                new BorderLayout(),
                Color.black
        );

        //base
        ManagedPanelDisplay mainPanel = new SwingManagedPanelDisplay("mainPanel", Color.white, window, new BorderLayout());

        //UI
        ManagedPanelDisplay ui = new SwingManagedPanelDisplay("UI", Color.gray, new WindowSize(700,100), new FlowLayout());
        mainPanel.add(ui, "north");

        //OUTPUT
        ManagedPanelDisplay out = new SwingManagedPanelDisplay("OUTPUT", Color.lightGray, new WindowSize(600,400), new FlowLayout());

        mainPanel.add(out, "center");

        //UI components
        ManagedPanelDisplay inputDisplay = new SwingManagedPanelDisplay("INPUT", Color.ORANGE, new WindowSize(400,100), new CardLayout());
        PanelDisplay selectionsDisplay = new SwingPanelDisplay("SELECTOR", ui.color(), corner);

        Selector<String> selector = new SwingUniqueSelector<>("SELECTOR",selections.keySet().stream().sorted().toList());
        selector.perform(() -> {
            String selected = selections.get(selector.selected());
            CardLayout cl = (CardLayout) ( (JPanel) inputDisplay.self()).getLayout();
            cl.show((Container) inputDisplay.self(),selected);
        });
        selectionsDisplay.add(selector);

        PanelDisplay confirmationDisplay = new SwingPanelDisplay("CONFIRMATION", ui.color(), corner);
        Button confirmButton = new SwingButton("CONFIRM","Confirm Choices");
        confirmationDisplay.add(confirmButton);

        ui.add(selectionsDisplay);
        ui.add(inputDisplay);
        ui.add(confirmationDisplay);

        //OUT Components
        PanelDisplay left = new SwingPanelDisplay("LEFT", out.color(), EMPTY_SPACE);
        PanelDisplay right = new SwingPanelDisplay("RIGHT", out.color(), EMPTY_SPACE);

        ManagedPanelDisplay response = new SwingManagedPanelDisplay("RESPONSE",Color.white,result, new BorderLayout());

        out.add(left);
        out.add(response);
        out.add(right);

        // input components
        MoneyExchangeInputDisplay exchange = new MoneyExchangeInputDisplay(reference,inputDisplay);
        HistoricMoneyExchangeInputDisplay history = new HistoricMoneyExchangeInputDisplay(reference,inputDisplay);
        CurrenciesInputDisplay currencies = new CurrenciesInputDisplay(reference,inputDisplay);

        inputDisplay.add(exchange);
        inputDisplay.add(history);
        inputDisplay.add(currencies);

        confirmButton.perform(actionForChoices(selector, exchange,history,currencies, response));


        mainFrame.add(mainPanel,"CENTER");

        mainFrame.show();

    }

    private Performer.Action actionForChoices(Selector<String> selector,
                                              MoneyExchangeInputDisplay exchange,
                                              HistoricMoneyExchangeInputDisplay history,
                                              CurrenciesInputDisplay currencies,
                                              ManagedPanelDisplay response) {
         return () -> {
             String selection = selections.get(selector.selected());
             switch (selection){
                 case "EXCHANGE" -> {
                     MoneyExchangedOutputDisplay outputDisplay = new MoneyExchangedOutputDisplay(response);

                     factory.exchangeMoneyCommandBuilder().with(exchange.value(),
                             exchange.from(),
                             exchange.to(),
                             outputDisplay.base(),
                             outputDisplay.converted(),
                             exchangeRateLoader,
                             token)
                             .build()
                             .execute();
                     response.removeAll();
                     response.add(outputDisplay, "center");
                     response.revalidate();
                 }

                 case "HISTORY" -> {
                     LineChartDisplay<Histogram> chart = new SwingHistogramLineChartDisplay("CHART",response.color(),response.dimensions());
                     factory.exchangeHistoricMoneyCommandBuilder().with(
                             history.from(),history.to(), history.value(), history.since(), chart, historicExchangeRateLoader, token
                     ).build().execute();
                     response.removeAll();
                     response.add(chart,"center");
                     response.revalidate();
                 }

                 case "CURRENCIES" -> {

                     List<Currency> currenciesList = currencies.currencies().selections();
                     currencies.currencies().update(currencies.currencies().options().stream().filter(o -> !currenciesList.contains(o)).toList());
                     reference.setVisible(currenciesList.stream().map(Currency::code).toList());
                     exchange.from().update(reference.currencies());
                     exchange.to().update(reference.exclude(exchange.from().selected()));
                     history.from().update(reference.currencies());
                     history.to().update(reference.exclude(history.from().selected()));

                     MoneyDisplay.ListedMoneyDisplay visual = new SwingMoneyDisplay("CURRENCIES",response.color(),response.dimensions());
                     visual.show(reference.currencies().stream().map(c -> new Money(1,c)).toList());
                     response.removeAll();
                     response.add(visual, "center");
                     response.revalidate();
                 }
             }
        };
    }


}
