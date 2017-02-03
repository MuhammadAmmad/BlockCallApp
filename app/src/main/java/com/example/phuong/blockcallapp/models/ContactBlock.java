package com.example.phuong.blockcallapp.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by phuong on 03/02/2017.
 */
@Table
public class ContactBlock extends SugarRecord {
    private String name;
    private String numberPhone;
    private boolean is_hidden;

    public ContactBlock() {
    }

    public ContactBlock(String name, String numberPhone, boolean is_hidden) {
        this.name = name;
        this.numberPhone = numberPhone;
        this.is_hidden = is_hidden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public boolean is_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(boolean is_hidden) {
        this.is_hidden = is_hidden;
    }
}
