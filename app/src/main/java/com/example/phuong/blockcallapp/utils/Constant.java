package com.example.phuong.blockcallapp.utils;

import com.example.phuong.blockcallapp.models.Contact;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    public static List<Contact> sortList(List<Contact> contacts) {
        //sort
        Collections.sort(contacts, new Comparator<Contact>() {
            public int compare(Contact v1, Contact v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });

        return contacts;
    }
}
