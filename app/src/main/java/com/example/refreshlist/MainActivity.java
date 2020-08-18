package com.example.refreshlist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final String heading = "heading";
    static final String subtitle = "subtitle";
    SharedPreferences saveSting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveSting = getSharedPreferences("LargeText", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveSting.edit();
        if(saveSting.contains(getString(R.string.large_text))) {}
        else {
            editor.putString("LargeText", getString(R.string.large_text));
            editor.apply();
        }
        String largeText = saveSting.getString("LargeText", "");
        final ListView list = findViewById(R.id.list);
        final String[] values = largeText.split("\n\n");
        final List<Map<String, String>> data = new ArrayList<>();

        DataAdd(values, data);

        final SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.twin_item,
                new String [] {heading, subtitle}, new int[] {R.id.heading, R.id.subtitle});
        list.setAdapter(sAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.remove(position);
                sAdapter.notifyDataSetChanged();
            }
        });
        final androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String largeText2 = saveSting.getString("LargeText", "");
                final String[] values2 = largeText2.split("\n\n");
                DataAdd(values2, data);
                SimpleAdapter Adapter = new SimpleAdapter(MainActivity.this, data, R.layout.twin_item,
                        new String [] {heading, subtitle}, new int[] {R.id.heading, R.id.subtitle});
                list.setAdapter(sAdapter);
                Adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

            }
        });

    }


    public static void DataAdd(String[] values, List<Map<String, String>> data) {
        for (String s: values) {
            Map<String, String> str = new HashMap<>();
            str.put(heading, s);
            str.put(subtitle, Integer.toString(s.length()));
            data.add(str);
        }
    }
}
