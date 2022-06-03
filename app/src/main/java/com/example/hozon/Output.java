package com.example.hozon;
import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.*;

public class Output {

    private String path;                                //ファイルを読み書きするためのディレクトリのパスが入る。
    final private static String name = "Genre_names";   //ジャンルファイルのファイル名。
    Genre genre;                                        //ジャンルを管理するインスタンス

    /*  */
    public Output(String path) {

        this.path = path;
        this.genre = Read(this.name); //ジャンルオブジェクトを

        if(genre == null){
            this.genre = new Genre();
            Log.w("OUTPUT", "Genreがないので作成しました");
            if(!write(name, genre))
                Log.w("OUTPUT", "Output: ");
        }
    }



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

    public void hoge(){
        write("削除してください",new Book("削除してください","その他のジャンル",genre));
    }

    public List<String> getGenres(){
        return new ArrayList<String>(genre.genre_names);
    }

    private <T> boolean write(String name,T book){//throws FileNotFoundException,IOException{
        try{
            FileOutputStream FileStream = new FileOutputStream(path + "/" + name + ".dat");
            BufferedOutputStream buf = new BufferedOutputStream(FileStream);
            ObjectOutputStream output = new ObjectOutputStream(buf);
            output.writeObject(book);
            output.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /* 型名を指定し、ファイル名を受け取って ストレージ内にあるオブジェクトデータを読み込んでインスタンスを返す。 */
    private <T> T Read(String name){//throws FileNotFoundException,IOException,ClassNotFoundException{
        try {
            FileInputStream f;
            if(name.endsWith(".dat"))
                f = new FileInputStream(name);
            else
                f = new FileInputStream(path + "/" + name + ".dat");

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

    public void finish(){
        write(this.name,genre);
    }

    public int delete(String book_name){
        File def_file = new File(this.path + book_name);
        if(def_file == null)
            return -1;
        if(def_file.delete())
            return 1;
        return 0;
    }

    public List<Book> all_books(){
        List BookList = new ArrayList<Book>();
        File file = new File(this.path);
        for(File e : file.listFiles()){
            if(e.getName().contains(this.name))
                continue;
            Book book = Read(path + "/" + e.getName());
            if(book == null) {
                new Regestar_Activity().view_error("bookファイルを読み込みきれませんでした");
            }
            BookList.add(book);
        }
        return BookList;
    }

    public String[] allBookName(){
        List<String> BookList = new ArrayList<String>();
        File file = new File(this.path);
        for(File e : file.listFiles()){
            BookList.add(e.getName());
        }
        String[] st = new String[BookList.size()];
        BookList.toArray(st);
        return st;
    }

    public boolean Regestar_book(Book b){
        return(write(b.getName(),b));
    }

}