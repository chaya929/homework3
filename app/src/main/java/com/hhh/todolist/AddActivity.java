package com.hhh.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private Button bt_submit;
    private String name,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_name = (EditText) findViewById(R.id.et_name);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit:
                name = et_name.getText().toString().trim();
                if ((name.equals(""))){
                    Toast.makeText(this, "信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                time = DataString.StringData();
                MySqlite mySQLite = new MySqlite(this, 1);
                SQLiteDatabase db = mySQLite.getWritableDatabase();
                //使用ContentValues添加数据
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("time", time);
                values.put("statue", "待完成");
                db.insert("dolist", null, values);
                db.close();
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
