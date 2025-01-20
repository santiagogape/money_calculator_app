package software.external.view.display;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.data.category.DefaultCategoryDataset;
import software.ulpgc.model.Histogram;
import software.ulpgc.view.display.MainFrame;
import software.ulpgc.view.display.output.LineChartDisplay;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SwingHistogramLineChartDisplay extends SwingPanelDisplay implements LineChartDisplay<Histogram> {


    public SwingHistogramLineChartDisplay(String name, Color color, MainFrame.WindowSize size) {
        super(name, color, size);
    }

    @Override
    public void show(Histogram data) {
        String title = "Historic Conversion Rates";
        DefaultCategoryDataset dataset = createDataset(data);
        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "Dates",
                "Rates",
                dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(dimensions().x(), dimensions().y()));
        super.panel.add(chartPanel);
    }

    @Override
    public void show(Histogram data, double amount) {
        String title = "Historic Converted Money";
        DefaultCategoryDataset dataset = createDataset(data, amount);
        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "Dates",
                "Money",
                dataset
        );

        chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(dimensions().x(), dimensions().y()));
        super.panel.add(chartPanel);
    }

    private DefaultCategoryDataset createDataset(Histogram data, double amount) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        data.forEach(rate -> dataset.addValue(
                rate.rate() * amount,
                rate.to(),
                formatter.format(rate.date())));
        return dataset;
    }

    private DefaultCategoryDataset createDataset(Histogram data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        data.forEach(rate ->
            dataset.addValue(rate.rate(),
                    rate.to(),
                    formatter.format(rate.date()))
        );
        return dataset;
    }

}
