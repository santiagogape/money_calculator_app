
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainFrame {

    public static final String TITLE = "Money Calculator";
    private static final Dimension inputDimensions = new Dimension(400,100);
    private static final Dimension corner = new Dimension(120,100);
    private static final Dimension outputDimensions = new Dimension(400,400);
    private static final Dimension emptySpace = new Dimension(100,400);

    public static void main(String[] args) {
        // window
        JFrame mainFrame = new JFrame(TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 500);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(Color.black);

        // base
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);


        //UI
        JPanel ui = new JPanel(new FlowLayout());
        ui.setBackground(Color.gray);
        ui.setPreferredSize(new Dimension(700,100));
        mainPanel.add(ui,BorderLayout.NORTH);

        //out
        JPanel out = new JPanel(new FlowLayout());
        out.setBackground(Color.lightGray);
        out.setPreferredSize(new Dimension(600,400));
        mainPanel.add(out,BorderLayout.CENTER);


        //UI components
        JPanel input = new JPanel(new CardLayout());
        input.setBackground(Color.ORANGE);
        input.setPreferredSize(inputDimensions);

        JPanel selection = new JPanel();
        selection.setBackground(ui.getBackground());
        selection.setPreferredSize(corner);
        JComboBox<String> coins = getStringJComboBox(input);
        selection.add(coins);



        JPanel button = new JPanel();
        button.setBackground(ui.getBackground());
        button.setPreferredSize(corner);
        JButton confirm = new JButton("confirm");
        confirm.addActionListener(confirmationCommand(out));
        button.add(confirm);

        ui.add(selection);
        ui.add(input);
        ui.add(button);

        // out components
        JPanel left = new JPanel();
        left.setPreferredSize(emptySpace);
        left.setBackground(out.getBackground());
        JPanel right = new JPanel();
        right.setPreferredSize(emptySpace);
        right.setBackground(out.getBackground());

        JPanel response = new JPanel();
        response.setPreferredSize(outputDimensions);

        JPanel one = new JPanel();
        one.setPreferredSize(outputDimensions);
        one.setBackground(Color.GRAY);

        JPanel other = new JPanel();
        other.setPreferredSize(outputDimensions);
        other.setBackground(Color.darkGray);

        out.add(left);
        out.add(response);
        out.add(right);


        // input components
        JPanel exchange = exchange();
        JPanel history = history();
        JPanel currencies = addCurrencies();
        Map<String, JPanel> inputs = Map.of("exchange", exchange, "history", history, "currencies", currencies);
        inputs.forEach(input::add);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private static JPanel addCurrencies() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("ISO CODE for Currencies (coma separated):");
        label.setName("format");

        JTextField textField = new JTextField(10);
        textField.setName("currencies");

        JButton button = new JButton("send");
        button.setName("button");


        JLabel resultLabel = new JLabel();
        resultLabel.setName("result");


        button.addActionListener(_ -> {
            String userInput = textField.getText();
            if (userInput.matches("^[A-Z]{3}(?:,[A-Z]{3})*$")) {
                resultLabel.setText("valid: " + userInput);
            } else {
                resultLabel.setText("invalid.");
            }
        });


        JPanel up = new JPanel(new FlowLayout());
        up.add(label);
        up.add(button);
        up.setName("up");
        JPanel down = new JPanel(new FlowLayout());
        down.add(textField);
        down.add(resultLabel);
        down.setName("down");
        panel.add(up, BorderLayout.NORTH);
        panel.add(down,BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel history() {
        JPanel frame2 = new JPanel();
        frame2.setLayout(new BorderLayout());

        JComboBox<String> fromComboBox2 = new JComboBox<>(new String[]{"option 1", "option 2", "option 3"});
        fromComboBox2.setName("from");
        JScrollPane toList2 = new JScrollPane(getStringJList());
        toList2.setName("to");
        JLabel arrowLabel2 = new JLabel("→");


        JPanel up = new JPanel(new FlowLayout());
        JPanel down = new JPanel(new FlowLayout());

        up.add(fromComboBox2);
        up.add(arrowLabel2);
        up.add(toList2);

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        down.add(dateSpinner);

        frame2.add(up,BorderLayout.NORTH);
        frame2.add(down,BorderLayout.SOUTH);

        return frame2;
    }

    private static JPanel exchange() {
        JPanel frame1 = new JPanel();
        frame1.setLayout(new BorderLayout());
        JComboBox<String> fromComboBox1 = new JComboBox<>(new String[]{"option 1", "option 2", "option 3"});
        fromComboBox1.setName("from");
        JComboBox<String> toComboBox1 = new JComboBox<>(new String[]{"option 1", "option 2", "option 3"});
        toComboBox1.setName("to");
        JTextField inputField1 = new JTextField(10);
        inputField1.setName("money");
        JCheckBox checkBox1 = new JCheckBox("calculate changes from a specific date");
        checkBox1.setName("historic");
        JLabel arrowLabel1 = new JLabel("→");


        JPanel up = new JPanel(new FlowLayout());
        up.add(fromComboBox1);
        up.add(arrowLabel1);
        up.add(toComboBox1);
        up.setName("up");


        JPanel down = new JPanel(new FlowLayout());
        down.setName("down");
        down.add(inputField1);
        JPanel date = new JPanel();
        down.add(date);


        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);


        checkBox1.addActionListener(_ -> {
            if (checkBox1.isSelected()){
                date.add(dateSpinner);
                date.revalidate();
            } else {
                date.remove(dateSpinner);
                date.revalidate();
            }

        });


        JPanel center = new JPanel(new FlowLayout());
        center.add(new JPanel());
        center.add(checkBox1,FlowLayout.CENTER);

        frame1.add(up,BorderLayout.NORTH);
        frame1.add(center,BorderLayout.CENTER);
        frame1.add(down,BorderLayout.SOUTH);
        return frame1;
    }

    private static JComboBox<String> getStringJComboBox(JPanel out) {
        JComboBox<String> coins = new JComboBox<>(new Vector<>(List.of("Exchange","History","Add Currencies")));
        Map<String ,String > entries = Map.of("Exchange",
                "exchange",
                "History",
                "history",
                "Add Currencies",
                "currencies");
        coins.addActionListener(_ -> {
            String selected = entries.get((String) coins.getSelectedItem());
            CardLayout cl = (CardLayout) out.getLayout();
            cl.show(out, selected);
        });
        return coins;
    }

    private static JList<String> getStringJList() {
        JList<String> toList2 = new JList<>(new String[]{"option A", "option B", "option C"});
        toList2.setVisibleRowCount(3);
        toList2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        toList2.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0)) {
                    removeSelectionInterval(index0, index1);
                } else {
                    addSelectionInterval(index0, index1);
                }
            }
        });
        return toList2;
    }

    private static ActionListener confirmationCommand(JPanel panel) {
        return _ -> JOptionPane.showMessageDialog(panel,
                "¡pressed!",
                "response",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
