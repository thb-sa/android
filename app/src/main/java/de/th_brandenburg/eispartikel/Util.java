package de.th_brandenburg.eispartikel;

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

}
