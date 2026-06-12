package bank_sampah.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtil {

    public static String rupiah(double value) {

        NumberFormat nf =
                NumberFormat.getCurrencyInstance(
                        Locale.of("id", "ID")
                );

        return nf.format(value)
                .replace(",00", "");
    }

    public static String kg(double value) {

        return String.format(
                Locale.US,
                "%.1f Kg",
                value
        );
    }
}