package com.project.pastebinsimple.feature.util;

import lombok.experimental.UtilityClass;
import java.time.*;

@UtilityClass
public class DateTimeUtil {

    public static Instant getCurrentInstant() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        ZoneId zoneId = ZoneOffset.UTC;
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, time, zoneId);
        return zonedDateTime.toInstant();
    }

    public static Instant getInstant(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        ZoneId zoneId = ZoneOffset.UTC;
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        System.out.println(zonedDateTime.toInstant());
        return zonedDateTime.toInstant();
    }

}
