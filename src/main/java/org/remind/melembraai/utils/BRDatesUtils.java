package org.remind.melembraai.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class BRDatesUtils {
    private static final String BRAZIL_DATE_FORMAT = "dd/MM/yyyy";
    private static final ZoneId BRAZIL_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    /**
     * Construtor privado para impedir a instanciação.
     *
     * <p>Como esta é uma classe utilitária, não deve ser instanciada. Este construtor
     * privado garante que a classe não possa ser instanciada externamente.</p>
     */
    private BRDatesUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    public static ZoneId getBrazilZoneId() {
        return BRAZIL_ZONE_ID;
    }

    public static String transformLocalDateToBrazilDateFormatString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(BRAZIL_DATE_FORMAT));
    }

    public static String transformLocalDateTimeToBrazilDateTimeFormatString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public static LocalDate transformBrazilDateFormatStringToLocalDate(String brazilDateFormatString) {
        return LocalDate.parse(brazilDateFormatString, DateTimeFormatter.ofPattern(BRAZIL_DATE_FORMAT));
    }

    public static LocalDateTime transformBrazilDateTimeFormatStringToLocalDateTime(String brazilDateTimeFormatString) {
        return LocalDateTime.parse(brazilDateTimeFormatString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
