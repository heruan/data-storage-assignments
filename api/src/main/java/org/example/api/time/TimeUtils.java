package org.example.api.time;

import java.time.*;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static Date wrapLocalDate(LocalDate localDate) {
        return Date.newBuilder()
                .setDay(localDate.getDayOfMonth())
                .setMonth(localDate.getMonthValue())
                .setYear(localDate.getYear())
                .build();
    }

    public static LocalDate unwrapDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static Time wrapLocalTime(LocalTime localTime) {
        return Time.newBuilder()
                .setHour(localTime.getHour())
                .setMinute(localTime.getMinute())
                .setSecond(localTime.getSecond())
                .build();
    }

    public static TimeZone wrapZoneId(ZoneId zoneId) {
        return TimeZone.newBuilder().setZoneId(zoneId.getId()).build();
    }

    public static Timestamp wrapZonedDateTime(ZonedDateTime zonedDateTime) {
        return Timestamp.newBuilder()
                .setDate(wrapLocalDate(zonedDateTime.toLocalDate()))
                .setTime(wrapLocalTime(zonedDateTime.toLocalTime()))
                .setTimeZone(wrapZoneId(zonedDateTime.getZone()))
                .build();
    }

    public static Timestamp wrapInstant(Instant instant) {
        return wrapZonedDateTime(ZonedDateTime.ofInstant(instant, ZoneOffset.UTC));
    }
}
