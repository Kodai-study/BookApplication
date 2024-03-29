package com.example.hozon;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * ジャンルについて、一覧票を管理したりするクラス
 */
public class Genre implements Serializable{

    /**
     * ジャンル名を全てまとめておくリスト
     */
    List<String> genre_names = new ArrayList<String>();

    /**
     * ジャンル名が存在しているかの検索
     *   新たに入力されたときに、存在しているかどうか
     *
     * @param genre ジャンル名
     * @return 既に存在していればそのIDを、存在していなければ新たに作成して渡す
     */
    public int serch(String genre){

        if(!genre_names.contains(genre)){
            System.out.println("新しく ジャンル" + genre + " を追加します。");
            if(genre_names.size() == 0)
                genre_names.add("その他ジャンル");
            genre_names.add(genre_names.size() - 1,genre);
        }

        return -(genre_names.indexOf(genre));
    }

    /**
     * ジャンルの数を返す
     */
    /* 今あるジャンルの数=genre_namesの大きさを返す */
    public int getGenreSize(){
        return genre_names.size();
    }

    /**
     * ジャンル名を、見やすく全てを表示する文字列を作成して返す
     * @return 1行ごとにジャンルを表示した文字列
     */

    public String check_genres(){
        int cnt = 0;
        StringBuilder st = new StringBuilder("ジャンルの種類----------------\n");
       // System.out.println("ジャンルの種類----------------");
        for(String e : genre_names)
            st.append((cnt++) + " :  " + e + " \n");

        st.append("-----------------------------\n");
        return st.toString();
    }

    /**
     * ジャンル一覧の、文字列配列で取得する
     * @return string [ ]
     */
    /* ジャンルの要素全てを、String型の配列にして返す(参照によって悪いことが起こらないため) */
    public String[] getGenreList(){
        return genre_names.toArray(new String[genre_names.size()]);
    }

    /**
     * Bookインスタンスを受け取って、該当するジャンル名を返す
     * @param book ジャンルを調べたいBook
     * @return ジャンルの文字列
     */
    /* ブックを引数に受け取って、該当するジャンルを返す */
    public String get_genre(Book book){
        return genre_names.get(book.getGenreId());
    }

}