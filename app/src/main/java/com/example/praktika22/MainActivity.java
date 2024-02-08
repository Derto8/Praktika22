package com.example.praktika22;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    EditText etName, etEmail, etId;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etId = (EditText) findViewById(R.id.etId);

        dbHelper = new DBHelper(this);
    }

    public void onClick(View v){

        ContentValues cv = new ContentValues();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String id = etId.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int getId = v.getId();
        if(getId == R.id.btnAdd){
            Log.d(LOG_TAG, "--- Insert ---");

            cv.put("name", name);
            cv.put("email", email);

            long rowId = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, Id = " + rowId);
        }
        if(getId == R.id.btnRead){
            Log.d(LOG_TAG, "--- Rows in mytable ---");
            Cursor c = db.query("mytable", null, null ,null ,null, null, null);
            if(c.moveToFirst()){
                int idColInder = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int emailColIndex = c.getColumnIndex("email");

                do{
                    Log.d(LOG_TAG,
                            "ID = " + c.getInt(idColInder) +
                            ", name = " + c.getString(nameColIndex) +
                            ", email = " + c.getString(emailColIndex));
                } while (c.moveToNext());
            } else Log.d(LOG_TAG, "0 rows");
            c.close();
        }
        if(getId == R.id.btnClear){
            Log.d(LOG_TAG, "--- Clear mytable ---");
            int clearCOunt = db.delete("mytable", null, null);
            Log.d(LOG_TAG, "deleted rows count = " + clearCOunt);
        }
        if(getId == R.id.btnUpdate){
            if (id.equalsIgnoreCase("")) {
                return;
            }
            Log.d(LOG_TAG, "--- Update mytable: ---");
            // подготовим значения для обновления
            cv.put("name", name);
            cv.put("email", email);
            // обновляем по id
            int updCount = db.update("mytable", cv, "id = ?",
                    new String[] { id });
            Log.d(LOG_TAG, "updated rows count = " + updCount);
        }

        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper{
        public  DBHelper(Context context){
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "---onCreate database ---");
            db.execSQL("create table myTable (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "email text" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}