package software.ulpgc.model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Histogram implements Iterable<ExchangeRate>{


    private final List<ExchangeRate> histogram;

    public Histogram(List<ExchangeRate> compared){
        this.histogram = new ArrayList<>();
        histogram.addAll(compared);
        histogram.sort(sorter());
    }

    private Comparator<? super ExchangeRate> sorter() {
        return Comparator.comparing(ExchangeRate::date).thenComparing(ExchangeRate::to);
    }

    public Stream<ExchangeRate> stream(){
        return histogram.stream();
    }


    @Override
    public Iterator<ExchangeRate> iterator() {
        return histogram.iterator();
    }

    public ExchangeRate get(int index){
        return histogram.get(index);
    }

    @Override
    public String toString() {
        return "Histogram{" +
                "histogram=" + histogram +
                '}';
    }
}
