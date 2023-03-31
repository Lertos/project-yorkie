package com.lertos.projectyorkie;

import java.util.LinkedHashMap;
import java.util.Map;

public class IdleNumber {

    public static final Map<Integer, String> digitIdentifiers = new LinkedHashMap<Integer, String>() {{
        put(18, "QI");
        put(15, "QA");
        put(12, "T");
        put(9, "B");
        put(6, "M");
        put(3, "K");
        put(0, "");
    }};

    public static String getStrNumber(double number) {
        String strValue = String.valueOf(Math.round(number));
        int numOfDigits = strValue.length();

        return getHighestOrderStr(strValue, numOfDigits);
    }

    private static String getHighestOrderStr(String strNumber, int numOfDigits) {
        Map.Entry<Integer, String> entry = getEntry(numOfDigits);
        String identifier = entry.getValue();
        int highestDigit = entry.getKey();
        int importantDigits = numOfDigits - highestDigit;

        StringBuilder sb = new StringBuilder();
        sb.append(strNumber.substring(0, importantDigits));
        if (importantDigits != 0 && highestDigit != 0) {
            sb.append(".");
            sb.append(strNumber.substring(importantDigits, importantDigits + 2));
        }
        sb.append(identifier);

        return sb.toString();
    }

    private static Map.Entry<Integer, String> getEntry(int numOfDigits) {
        for (Map.Entry<Integer, String> entry : digitIdentifiers.entrySet()) {
            if (numOfDigits > entry.getKey())
                return entry;
        }
        return null;
    }
}
