package com.example.clayou.memo;

import java.io.Serializable;

/**
 * Created by 10295 on 2018/5/7.
 */

public class Memo extends Item implements Serializable, Comparable {

    private String username;

    private String Memo;

    private Date deleteDate;

    public Memo(String name, Date date, String memo) {
        super(name, date, memo);
        this.username = "Admin";
        Memo = "";
    }

    public Memo(String name, Date date, String memo, String username, String memo1) {
        super(name, date, memo);
        this.username = username;
        Memo = memo1;
    }

    public Memo getClone(){

        return  new Memo(getName(),getDate(),getMemo());

    }

    @Override
    public int compareTo(Object o) {

        java.util.Date d1 = new java.util.Date(getDate().getYear(),
                getDate().getMonth(),getDate().getDay(),getDate().getHour(),getDate().getMinute());

        Memo o2 = (Memo) o;
        java.util.Date d2 = new java.util.Date(o2.getDate().getYear(),
                o2.getDate().getMonth(),o2.getDate().getDay(),o2.getDate().getHour(),o2.getDate().getMinute());


        if(d1.before(d2))
            return  -1;
        else return 1;
    }


    public Date getDate() {
        return super.getDate();
    }

    public void setDate(Date date) {
        super.setDate(date);
    }

    public String getName() {
        return StringUtil.clearEnter(super.getName());
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getUsername() {
        return username;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setUsername(String text) {
        this.username = text;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
