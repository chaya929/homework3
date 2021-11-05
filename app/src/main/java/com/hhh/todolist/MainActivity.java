package com.hhh.todolist;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_time,tv_name;
    private CheckBox cb;
    private ImageView iv_delete;
    private List<Map<String,String>> list;
    private MyAdapter adapter;
    private ListView lv_user;
    private MySqlite mySqlite;
    private SQLiteDatabase db;
    private FloatingActionButton bt_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_add = (FloatingActionButton) findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);
        lv_user = (ListView) findViewById(R.id.lv_user);
        list = new ArrayList<>();
        getData();
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item,
                new String[]{"name","time"},
                new int[]{R.id.tv_name,R.id.tv_time}
        );

        lv_user.setAdapter(adapter);
        adapter.setDeleteOnClickListener(new DeleteOnClickListener() {
            @Override
            public void deleteItem(View view, final int pos) {
                System.out.println("pos:"+pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteList(list.get(pos).get("id"));
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        adapter.setAltOnClickListener(new AltOnClickListener() {
            @Override
            public void altItem(View view, int pos) {
                if(list.get(pos).get("statue").equals("已完成")){
                    cb.setChecked(false);
                    altStatue("待完成",list.get(pos).get("id"));
                }else {
                    cb.setChecked(true);
                    altStatue("已完成",list.get(pos).get("id"));
                }
            }

        });
    }

    private void altStatue(String s,String id) {
        mySqlite=new MySqlite(this,1);
        db=mySqlite.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("statue",s);
        db.update("dolist",values,"id=?",new String[]{id});
    }

    public void deleteList(String i){
        mySqlite=new MySqlite(this,1);
        db=mySqlite.getWritableDatabase();
        db.delete("dolist","id=?",new String[]{i});
        db.close();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item,
                new String[]{"name","time"},
                new int[]{R.id.tv_name,R.id.tv_time}
        );
        lv_user.setAdapter(adapter);
        adapter.setDeleteOnClickListener(new DeleteOnClickListener() {
            @Override
            public void deleteItem(View view, final int pos) {
                System.out.println("pos:"+pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteList(list.get(pos).get("id"));
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        adapter.setAltOnClickListener(new AltOnClickListener() {
            @Override
            public void altItem(View view, int pos) {
                if(list.get(pos).get("statue").equals("已完成")){
                    list.get(pos).put("statue","待完成");
                    adapter.notifyDataSetChanged();
                    altStatue("待完成",list.get(pos).get("id"));
                }else {
                    list.get(pos).put("statue","已完成");
                    adapter.notifyDataSetChanged();
                    altStatue("已完成",list.get(pos).get("id"));
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_add:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;
        }
    }

    public interface DeleteOnClickListener{
        void deleteItem(View view,int pos);
    }
    public interface AltOnClickListener{
        void altItem(View view,int pos);
    }

    public class MyAdapter extends SimpleAdapter {
         DeleteOnClickListener deleteOnClickListener;
         AltOnClickListener altOnClickListener;
        public void setAltOnClickListener(AltOnClickListener altOnClickListener) {
            this.altOnClickListener = altOnClickListener;
        }

        public void setDeleteOnClickListener(DeleteOnClickListener deleteOnClickListener) {
            this.deleteOnClickListener = deleteOnClickListener;
        }

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                         String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cb.setChecked(false);
            holder.tv_name.setText(list.get(position).get("name"));
            holder.tv_time.setText(list.get(position).get("time"));
            if(list.get(position).get("statue").equals("待完成")){
                holder.cb.setChecked(false);
                holder.tv_name.getPaint().setFlags(0);
                holder.tv_name.setTextColor(Color.BLACK);
            }else {
                holder.cb.setChecked(true);
                holder.tv_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                holder.tv_name.setTextColor(Color.GRAY);
            }
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    altOnClickListener.altItem(view,position);
                }
            });

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteOnClickListener.deleteItem(view,position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_name,tv_time;
            ImageView iv_delete;
            CheckBox cb;

            public ViewHolder(View v) {
                tv_time = v.findViewById(R.id.tv_time);
                tv_name = v.findViewById(R.id.tv_name);
                cb = v.findViewById(R.id.cb);
                iv_delete = v.findViewById(R.id.iv_delete);
            }
        }
    }

    public List<Map<String,String>> getData(){
        list.clear();
        MySqlite mySQLite = new MySqlite(this, 1);
        SQLiteDatabase database = mySQLite.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from dolist", null);
        System.out.println(cursor.getCount());
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String statue = cursor.getString(cursor.getColumnIndex("statue"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Map<String,String> map = new HashMap<>();
            map.put("name",name);
            map.put("statue",statue);
            map.put("id",id);
            map.put("time",time);
            list.add(map);
        }
        return list;
    }
}
