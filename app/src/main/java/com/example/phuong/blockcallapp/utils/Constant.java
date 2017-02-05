package com.example.phuong.blockcallapp.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by phuong on 05/02/2017.
 */

public class Constant {
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
