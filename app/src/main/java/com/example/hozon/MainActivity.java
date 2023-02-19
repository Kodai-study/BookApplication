package com.example.hozon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Text.
     */
    TextView text;
    /**
     * The Spinner.
     */
    Spinner spinner;
    /**
     * The Output.
     */
    Output output;
    /**
     * The File.
     */
    File file;
    /**
     * The Button.
     */
    Button button;
    /**
     * The Scroll view.
     */
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context =  getApplicationContext();
        file = context.getFilesDir();
        output = new Output(file.getAbsolutePath());
        InputProcess inputProcess = new InputProcess();

        spinner.setOnItemSelectedListener( inputProcess.new Doropdown_proces());

        try {
            String st = "";
            for(File f : file.listFiles())
                st += f.getName() + "\n";
            text.setText(st);

            String[] genres = output.genre.getGenreList();
            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genres);

            //output.main(true,"Genre_names");
           // output.hoge();

        }
        catch (Exception e){
            Log.w("MAIN","output.mainのエラー" + e.getMessage());
        }
        finally {
            output.finish();
        }
   }

    /**
     * The type Input process.
     */
    class InputProcess {

        private int dropdown_position;

        /**
         * The type Doropdown proces.
         */
        class Doropdown_proces implements AdapterView.OnItemSelectedListener{
            @Override
            public void onItemSelected(AdapterView<?>adapterView, View view, int position, long id){
                //text.setText((String)spinner.getSelectedItem());
                int size = output.genre.getGenreSize();
                if(position == size - 1){

                }
                text.setText(String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                text.setText((String)spinner.getSelectedItem());
            }

            /**
             * Get doropdown int.
             *
             * @return the int
             */
            public int getDoropdown(){ return dropdown_position; }
        }

    }

}