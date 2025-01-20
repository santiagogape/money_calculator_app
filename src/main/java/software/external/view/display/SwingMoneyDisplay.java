package software.external.view.display;

import software.ulpgc.model.Money;
import software.ulpgc.view.display.MainFrame.WindowSize;
import software.ulpgc.view.display.output.MoneyDisplay;


import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SwingMoneyDisplay implements MoneyDisplay.ListedMoneyDisplay {

    private final JPanel panel;

    public SwingMoneyDisplay(String name, Color color, WindowSize size) {
        panel = new JPanel(new BorderLayout());
        panel.setName(name);
        panel.setBackground(color);
        panel.setSize(new Dimension(size.x(),size.y()));
    }

    private void show(JPanel panel, Money money){
        SwingDrawDisplay symbolDisplay = new SwingDrawDisplay(
                money.currency().code(),
                Color.white,
                null
        );
        SwingDrawDisplay amountDisplay = new SwingDrawDisplay(
                "AMOUNT",
                Color.white,
                null
        );
        symbolDisplay.draw(money.currency().symbol(),"BLACK");
        amountDisplay.draw(String.valueOf(money.amount()),"BLACK");
        panel.add((Component) symbolDisplay.self());
        panel.add((Component) amountDisplay.self());
        panel.revalidate();
    }

    @Override
    public void show(Money money) {
        panel.removeAll();
        JPanel grid = new JPanel(new GridLayout(2,1));
        grid.setSize(panel.getSize());
        show(grid,money);
        panel.add(grid,BorderLayout.CENTER);
        panel.revalidate();
    }

    @Override
    public void show(List<Money> list) {
        panel.removeAll();
        JPanel boxes = new JPanel();
        boxes.setLayout(new CardLayout());

        list.forEach(m -> {
            JPanel box = new JPanel();
            box.setLayout(new GridLayout(2,1));
            box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            box.setSize(panel.getSize());
            boxes.add(box);
            show(box,m);
        });
        boxes.addMouseWheelListener(e -> {
            CardLayout layout = (CardLayout) boxes.getLayout();
            if (e.getWheelRotation() > 0) layout.next(boxes);
            else layout.previous(boxes);

        });
        panel.add(boxes,BorderLayout.CENTER);
        panel.revalidate();
    }

    @Override
    public String name() {
        return panel.getName();
    }

    @Override
    public Object self() {
        return panel;
    }
}
