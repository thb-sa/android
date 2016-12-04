package de.th_brandenburg.eispartikel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import datenKlassen.Tageswerte;

public class Util {
    /**
     * return a string with a plus or minus
     * 5 -> +5
     * -5 -> -5
     *
     * @param value
     * @return
     */
    public static String plusMinus(int value) {
        return (value >= 0 ? "+" : "") + String.valueOf(value);
    }

    /**
     * Wandelt ein Datum in Textform in einen Unix Timestamp um
     *
     * @param dateSting Datum in Textform
     * @return          Datum in Unix Timestamp
     */
    public static long dateToTimestamp(String dateSting) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = format.parse(dateSting);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Sortiert die Tage in einer ConcurrentHashMap
     *
     * @param werte
     * @return
     */
    public static List<String> oderedTaqe(ConcurrentHashMap<String, Tageswerte> werte) {
        List<String> tage = new ArrayList<>();
        tage.addAll(werte.keySet());
        Collections.sort(tage, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return (int) (dateToTimestamp(lhs) - dateToTimestamp(rhs));
            }
        });
        return tage;
    }

}
