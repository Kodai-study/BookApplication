package com.example.hozon;
import androidx.annotation.NonNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The type Book.
 */
public class Book implements Serializable{

    private String name;
    /**
     * 当てはまるジャンルのID
     */
    int genre_num;
    /**
     * 新しいジャンル名を登録したときに、その名前
     */
    String new_genre;
    /**
     * 読み始めた日を格納
     */
    Calendar first_day;
    /**
     * 名前が入力されなかったときの、デフォルトの名前
     */
    final String NO_NAME = "名前の入力なし";
    /**
     * 日付を表示するときに使うフォーマッター  yyyy/mm/dd
     */
    final String DATAFORMAT = "yyyy/MM/dd";


    /**
     * Instantiates a new Book.
     * @param first_day 読み初めの日を登録
     * @param genre_id  既存のジャンルに登録するときは、idを指定
     */
    public Book(String name,int genre_id,Calendar first_day){

        this.name = name != "" ? name : this.NO_NAME;
        this.genre_num = genre_id;
        this.new_genre = null;
        this.first_day = first_day;

    }

    /**
     * Instantiates a new Book.
     *   読み初めの日が確定しない場合の登録。
     */
    public Book(String name,int genre_id){

        this(name,genre_id,Calendar.getInstance());

    }

    /**
     * ジャンルを新たに作成したときの処理
     */
    public Book(String name,String new_genre,Genre genre,Calendar first_day){

        this.name = name;
        this.new_genre = new_genre;
        int index = genre.serch(this.new_genre);
        this.genre_num = index > 0 ? index : -index;
        this.first_day = first_day;
    }

    /**
     * Instantiates a new Book.
     *
     * @param name      the name
     * @param new_genre the new genre
     * @param genre     the genre
     */
    public Book(String name,String new_genre,Genre genre){
        this(name,new_genre,genre,Calendar.getInstance());
    }


    /**
     * @return 本の名前のゲッタ
     */
    public String getName(){ return name; }

    /**
     * (デバッグ用)本の情報一覧を出すときの文字列。
     * @return ・名前 ・ジャンル名 ・ジャンルのID ・読み初めの日
     */
    public String get_data(@NonNull Genre genre){
        SimpleDateFormat format = new SimpleDateFormat(DATAFORMAT);

        String st = "名前 : " + this.name;
        st += "\nジャンル : " + genre.get_genre(this);
        st += "\nジャンルのid :" + this.genre_num;
        st += "\n読み始め : " + format.format(this.first_day.getTime());
        return st;
    }

    /**
     * Get genre id int.
     */
    public int getGenreId(){
        return genre_num;
    }
}

