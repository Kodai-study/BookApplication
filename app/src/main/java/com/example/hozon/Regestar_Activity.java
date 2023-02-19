package com.example.hozon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Calendar;
import java.util.List;


/**
 * The type Regestar activity.
 */
public class Regestar_Activity extends AppCompatActivity {

    /**
     *  入力ハンドラで呼び出される処理をまとめたクラス
     */
    InputProcess inputProcess = new InputProcess();
    /**
     * The Photo.
     */
    ImageView photo;
    /**
     * 本の名前を入れるテキストエディタ
     */
    EditText input_name;
    /**
     * 新ジャンルを選択したときに入力するテキストボックス
     */
    EditText input_genre;
    /**
     * ジャンル一覧を表示するドロップダウン
     */
    Spinner dropdown;
    /**
     * 入力内容で登録するボタン
     */
    Button regesterButton;
    /**
     * The Output.
     */
    Output output;
    /**
     * The Text view.
     */
    TextView textView;
    /**
     * エラー項目を表示するテキスト
     */
    TextView error;
    /**
     * 読み始めた日を格納するカレンダークラス
     */
    Calendar calendar;
    /**
     * 読み初めの日を
     */
    EditText date;
    /**
     * 何日前かを表示、もしくは入力するテキストボックス
     */
    EditText befor_days;
    /**
     * The Calendar button.
     */
    ImageButton calendar_button;

    LinearLayout thisLayout;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        init();

        dropdown.setOnItemSelectedListener(inputProcess.new Doropdown_proces());
        input_genre.setOnFocusChangeListener( inputProcess.new EditText_cursol(inputProcess.DEFOLT_GENRE));
        input_name.setOnFocusChangeListener(inputProcess.new EditText_cursol(inputProcess.DEFOLT_BOOKNAME));
        regesterButton.setOnClickListener(inputProcess.new RegestarButton_click());
        calendar_button.setOnClickListener(inputProcess.new calendarButton_push());
        photo.setOnClickListener(inputProcess.new DebugButtonPush());

        try {
            String[] genres = output.genre.getGenreList();
            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genres);

            output.main(true,"Genre_names");
            List<Book> BookList = output.all_books();
            dropdown.setAdapter(spinner_adapter);
        }
        catch (Exception e){
            Log.w("MAIN","output.mainのエラー" + e.getMessage());
            view_error(e);
        }
        finally {
            output.finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mInputMethodManager.hideSoftInputFromWindow(thisLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        thisLayout.requestFocus();
        return true;
    }

    private void init(){

        setContentView(R.layout.register_scene);
        photo = findViewById(R.id.imageView);
        input_name = findViewById(R.id.BookName);
        input_genre = findViewById(R.id.BookGenre);
        dropdown = findViewById(R.id.Genres);
        regesterButton = findViewById(R.id.Resgester_button);
        File file = getApplicationContext().getFilesDir();
        output = new Output(file.getAbsolutePath());
        textView = findViewById(R.id.textView2);
        error = findViewById(R.id.error_message);
        error.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        date = findViewById(R.id.input_date);
        befor_days = findViewById(R.id.days_ago);
        calendar_button = findViewById(R.id.calendar_button);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        thisLayout = findViewById(R.id.register_layout);

        input_name.setText(inputProcess.DEFOLT_BOOKNAME, TextView.BufferType.NORMAL);
        input_genre.setText(inputProcess.DEFOLT_GENRE, TextView.BufferType.NORMAL);


    }

    /**
     * View error.
     *
     * @param e the e
     */
    public void view_error(Exception e){
        error.setVisibility(View.VISIBLE);
        error.setText(e.getMessage(), TextView.BufferType.NORMAL);
    }

    /**
     * 登録時に発生したエラーを表示する。
     *
     * @param errorMessage 表示するメッセージ。 エラーを解除する場合はnullを入力
     */
    public void view_error(String errorMessage){

        if(errorMessage == null){
            error.setText("");
            error.setVisibility(View.GONE);
            return;
        }
        String txt = error.getText().toString();
        if(!txt.contains(errorMessage)){
            error.setText(txt + "\n" + errorMessage);
        }
        error.setVisibility(View.VISIBLE);

    }


    /**
     *
     */
    protected void refresh(){
        String[] genres = output.genre.getGenreList();
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genres);
        dropdown.setAdapter(spinner_adapter);
    }

    private class InputProcess {

        private int dropdown_position = 0;
        /**
         * The Defolt genre.
         */
        public final String DEFOLT_GENRE = "新しく追加するジャンルを入力";
        /**
         * The Defolt bookname.
         */
        public final String DEFOLT_BOOKNAME = "本の名前を入力";


        /**
         * 新しいジャンルを選択したら、新しいジャンル名を入力するテキストボックスを表示する
         */
        class Doropdown_proces implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //text.setText((String)spinner.getSelectedItem());
                if (position == getSelectSize() - 1) {
                    input_genre.setVisibility(View.VISIBLE);
                } else {
                    input_genre.setVisibility(View.GONE);
                }
                textView.setText((String) dropdown.getSelectedItem());
                dropdown_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dropdown.setSelection(dropdown.getAdapter().getCount() - 1);
            }

            public int getSelectSize() {
                return dropdown.getAdapter().getCount();
            }
        }


        /**
         * 本の名前のテキストボックスから離れたときに起きる処理。
         *   プロンプトと同じか、空白ならプロンプトを再表示する
         */
        class EditText_cursol implements View.OnFocusChangeListener {
            private String default_text;

            /**
             * Instantiates a new Edit text cursol.
             *
             * @param default_text プロンプトとして表示される、デフォルトの文字を指定する
             */
            public EditText_cursol(String default_text) {
                this.default_text = default_text;
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText text = (EditText) v;
                /*
                if(!hasFocus) {
                    mInputMethodManager.hideSoftInputFromWindow(thisLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    thisLayout.requestFocus();
                }*/

                if (hasFocus && text.getText().toString().contains(default_text))
                    text.setText("", TextView.BufferType.NORMAL);
                else if (!hasFocus && text.getText().toString().length() == 0)
                    text.setText(default_text, TextView.BufferType.NORMAL);
            }// クラス ForcusChange
        }


        /**
         * 登録ボタンを押された時の処理
         *  入力が正しければ情報を登録し、正しくなければエラーを表示する。
         */
        class RegestarButton_click implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                view_error((String)null);
                Book regest_book = null;
                String bookName = input_name.getText().toString();
                boolean ok = true;
                /* 本の名前が入力されていないか、プロンプトのままだったらエラー */
                if(bookName.contains(inputProcess.DEFOLT_BOOKNAME) || bookName.length() == 0){
                    view_error("本の名前を入力してください");
                    ok = false;
                }
                /* ジャンルを新たに登録するとき、名前が入力されていなかったらエラー */
                if(input_genre.getVisibility() != View.GONE) {
                    String genre = input_genre.getText().toString();
                    if (genre.contains(inputProcess.DEFOLT_GENRE) || genre.length() == 0) {
                        view_error("ジャンル名を入力してください");
                        ok = false;
                    } else {
                        regest_book = new Book(bookName, genre, output.genre);
                    }
                } else if(ok) {
                    int genre_id = dropdown.getSelectedItemPosition();
                    regest_book = new Book(bookName, genre_id);
                }

                if(!ok) return;

                if(!output.Regestar_book(regest_book))
                    view_error("Bookを登録できませんでした");
                else {
                    output.finish();
                    refresh();
                }
            }
        }// クラス 登録ボタンクリック


        /**
         * カレンダーボタンをクリックしたときの処理
         *    現在の日付を基準にしたカレンダーが表示される。
         */
        class calendarButton_push implements View.OnClickListener{
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(Regestar_Activity.this,new calendar_Select(),calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        }

        /**
         * カレンダーの日付を選んだ時の処理。年月日を取得して、適切な値なら読み始めた日として表示する。
         */
        class calendar_Select implements DatePickerDialog.OnDateSetListener{
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //選択した日付の、00:00 の時刻
                Calendar selectDay = Calendar.getInstance();
                selectDay.clear();
                selectDay.set(year,month,dayOfMonth);

                //今日より後の日付が選択されていればエラー
                if(selectDay.after(calendar)) {
                    view_error("未来を選んでいます");
                    befor_days.setText("0");
                    return;
                }
                int def_cnt = -1;
                while(calendar.after(selectDay)){
                    selectDay.add(Calendar.DATE,1);
                    def_cnt++;
                }
                befor_days.setText(String.valueOf(def_cnt));
                date.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth));
                view_error((String)null);
            }

            private void day_set(Calendar c){
                //date.setText();
            }
        }

        /**
         * 右側の写真、デバッグモードに入るボタンを7回連打するとデバッグモードに移行する。
         */
        class DebugButtonPush implements View.OnClickListener{
            private int debugButtonCount = 0;
            @Override
            public void onClick(View v) {
                debugButtonCount++;
                if(debugButtonCount > 7){
                    output.finish();
                    Intent intent = new Intent(getApplication(),Debug.class);
                    startActivity(intent);
                }
            }
        }
    }
}


