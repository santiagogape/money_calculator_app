package software.ulpgc.view.display.output;

import software.ulpgc.model.Money;
import software.ulpgc.view.display.Component;

import java.util.List;

public interface MoneyDisplay extends Component {
    void show(Money money);
    interface ListedMoneyDisplay extends MoneyDisplay{
        void show(List<Money> list);
    }
}
