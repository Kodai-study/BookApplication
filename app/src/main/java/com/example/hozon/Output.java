package com.example.hozon;
import android.content.Context;
import android.util.Log;

import java.io.*;
import java.sql.Ref;
import java.util.*;


/**
 * 本の登録やデータの引き出しを行うクラス
 */
public class Output {
    /** ファイルを読み書きするためのディレクトリのパスが入る */
    private String path;
    /** ジャンルファイルのファイル名 */
    final private static String GENRE_NAME = "Genre_names.dat";
    /**
     * ジャンル一覧を管理するインスタンス
     */
    Genre genre;
    /**
     * 本の名前を格納する配列
     */
    String[] allBookName;
    /**
     * ファイルの操作時の結果
     */
    public final int FILE_NOT_FOUND = -1;
    /**
     * The File cant delete.
     */
    public final int FILE_CANT_DELETE = 0;
    /**
     * The File delete success.
     */
    public final int FILE_DELETE_SUCCESS = 1;

    /**
     * Instantiates a new Output.
     *
     * @param path : データを格納するファイルパス
     */
    public Output(String path) {

        this.path = path;
        this.genre = Read(this.GENRE_NAME); //ジャンルオブジェクトを

        if(genre == null){
            this.genre = new Genre();
            Log.w("OUTPUT", "Genreがないので作成しました");
            if(!write(GENRE_NAME, genre))
                Log.w("OUTPUT", "Output: ");
        }
        Reflesh();
    }


    /**
     * Main string.
     *
     * @param mode the mode
     * @param name the name
     * @return the string
     * @throws FileNotFoundException  the file not found exception
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public String main(boolean mode,String name) throws FileNotFoundException,IOException,ClassNotFoundException{

        if(mode) {
            write(name, new Book(name, "ネクストジャンル", genre));
            write(name, genre);
            return name + "はかきこみできました";
        }
        else {
             Genre b = Read(name);
             return b.check_genres();
        }

    }


    /**
     * Get genres list.
     *
     * @return the list
     */
    public List<String> getGenres(){
        return new ArrayList<String>(genre.genre_names);
    }

    /**
     * @param name 保存するファイル名
     * @param klass 保存するインスタンス
     * @param <T>   インスタンスのクラス
     * @return      書き込みが成功したらTRUE 失敗したらFALSE
     */
    private <T> boolean write(String name,T klass){//throws FileNotFoundException,IOException{
        try{
            FileOutputStream FileStream = new FileOutputStream(path + "/" + name + ".dat");
            BufferedOutputStream buf = new BufferedOutputStream(FileStream);
            ObjectOutputStream output = new ObjectOutputStream(buf);
            output.writeObject(klass);
            output.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /* 型名を指定し、ファイル名を受け取って ストレージ内にあるオブジェクトデータを読み込んでインスタンスを返す。 */

    /**
     * ファイル名から、ストレージ内のオブジェクトデータを読み込んで返す
     *
     * @param <T>  受け取るオブジェクトの型名
     * @param name the name
     * @return <T> 型の、読み込んだインスタンス、なければNULL
     */
    public <T> T Read(String name){//throws FileNotFoundException,IOException,ClassNotFoundException{
        try {
            FileInputStream f;
            if(name.startsWith(path)){
                 if(name.endsWith(".dat"))
                     f = new FileInputStream(name);
                 else
                     f = new FileInputStream(name + ".dat");
            }
            else {
                if(name.endsWith(".dat"))
                    f = new FileInputStream(path + "/" +name);
                else
                    f = new FileInputStream(path + "/" + name + ".dat");
            }
            BufferedInputStream b = new BufferedInputStream(f);
            ObjectInputStream in = new ObjectInputStream(b);
            T ret = (T) in.readObject();
            in.close();
            return ret;
        }
        catch(Exception e) {
            Log.w("READ", e.getMessage());
            return null;
        }
    }

    /**
     * Finish.
     */
    public void finish(){
        write(this.GENRE_NAME,genre);
    }

    /**
     * 指定されたファイル名を削除する。
     *
     * @param book_name 削除するファイル名(.datは省略可)
     * @return  ファイル削除の結果
     *            NOT_FOUND,SUCCESS,ERROR
     */
    public int delete(String book_name){
        File deleteFile;
        int resultState = -1;
        if(!book_name.endsWith(".dat"))
            deleteFile = new File(this.path + "/" + book_name + ".dat");

        else
            deleteFile = new File(this.path + "/"  + book_name);
        if(deleteFile == null) {
            return this.FILE_NOT_FOUND;
        }
        else if(deleteFile.delete()) {
            Reflesh();
            return FILE_DELETE_SUCCESS;
        }
            Log.d("fuck",deleteFile.getAbsolutePath());
            return FILE_CANT_DELETE;
    }

    /**
     * 保存されている本クラスのインスタンスを取得する。
     *
     * @return Bookクラスのインスタンスリスト
     */
    public List<Book> all_books(){
        List BookList = new ArrayList<Book>();
        File file = new File(this.path);
        for(File e : file.listFiles()){
            if(e.getName().contains(this.GENRE_NAME))
                continue;
            Book book = Read(path + "/" + e.getName());
            if(book == null) {
                new Regestar_Activity().view_error("bookファイルを読み込みきれませんでした");
            }
            BookList.add(book);
        }
        return BookList;
    }

    /**
     * All book name string [ ].
     *
     * @return the string [ ]
     */
    public String[] allBookName(){

        return this.allBookName;
    }

    /**
     * 本の中身を登録する。
     *
     * @param b Bookのインスタンス
     * @return 書き込みができたかどうか。writeの返り値
     * @see #write(String, Object) 
     */
    public boolean Regestar_book(Book b){
        return(write(b.getName(),b));
    }

    /**
     * allBookNameの値を読み込みなおす。
     * @see #allBookName
     */
    public void Reflesh(){
        List<String> BookList = new ArrayList<String>();
        File file = new File(this.path);
        for(File e : file.listFiles()){
            BookList.add(e.getName());
        }
        String[] st = new String[BookList.size()];
        BookList.toArray(st);
        this.allBookName = st;
    }
}