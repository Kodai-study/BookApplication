package com.example.hozon;
import androidx.annotation.NonNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class Book implements Serializable{

    static int id = 0;

    private String name;
    int genre_num;
    String new_genre;
    Calendar first_day;
    final String no_name = "名前の入力なし";
    final String DATAFORMAT = "yyyy/MM/dd";



    public Book(String name,int genre_id,Calendar first_day){

        this.name = name != "" ? name : this.no_name;
        this.genre_num = genre_id;
        this.new_genre = null;
        this.first_day = first_day;

    }
    public Book(String name,int genre_id){

        this(name,genre_id,Calendar.getInstance());

    }

    public Book(String name,String new_genre,Genre genre,Calendar first_day){

        this.name = name;
        this.new_genre = new_genre;
        int index = genre.serch(this.new_genre);
        this.genre_num = index > 0 ? index : -index;
        this.first_day = first_day;
    }
    public Book(String name,String new_genre,Genre genre){
        this(name,new_genre,genre,Calendar.getInstance());
    }


    public String getName(){ return name; }

    public String get_data(@NonNull Genre genre){
        SimpleDateFormat format = new SimpleDateFormat(DATAFORMAT);

        String st = "名前 : " + this.name;
        st += "\nジャンル : " + genre.get_genre(this);
        st += "\nジャンルのid :" + this.genre_num;
        st += "\n読み始め : " + format.format(this.first_day.getTime());
        return st;
    }

    public int getGenreId(){
        return genre_num;
    }

/*	public String getFirstDay(){
		String st = String.valueOf(first_day.getTime)
	}*/


}

