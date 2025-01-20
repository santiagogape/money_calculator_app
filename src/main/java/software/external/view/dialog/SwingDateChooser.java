package software.external.view.dialog;

import software.ulpgc.view.dialog.DateChooser;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SwingDateChooser implements DateChooser {

    private final JSpinner date;
    private final String pattern;

    public SwingDateChooser(String name) {
        date = new JSpinner(new SpinnerDateModel());
        date.setEditor(new JSpinner.DateEditor(date,"yyyy/MM/dd"));
        date.setName(name);
        this.pattern = "yyyy-MM-dd";
    }

    @Override
    public String name() {
        return date.getName();
    }

    @Override
    public Object self() {
        return date;
    }

    @Override
    public ZonedDateTime date() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) date.getValue());
        LocalDate localDate = LocalDate.parse(new SimpleDateFormat(pattern).format(date.getValue()), DateTimeFormatter.ISO_LOCAL_DATE);
        return ZonedDateTime.of(LocalDateTime.of(localDate, LocalTime.now()), ZoneId.systemDefault());
    }

    @Override
    public String get() {
        return date.getValue().toString();
    }
}
