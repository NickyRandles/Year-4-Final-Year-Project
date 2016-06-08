package com.project.nicky.medicalmanager.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NameSearchActivity extends AppCompatActivity implements BackgroundResponse {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Integer> idList = new ArrayList<Integer>();
    private BackgroundTask backgroundTask;
    private String type = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_search);

        searchView = (SearchView)findViewById(R.id.searchView);

        listView = (ListView)findViewById(R.id.resultsList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(arrayAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("testing", query);
                backgroundTask = new BackgroundTask(getApplicationContext());
                backgroundTask.backgroundResponse = NameSearchActivity.this;
                backgroundTask.execute(type, query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                backgroundTask = new BackgroundTask(getApplicationContext());
                backgroundTask.backgroundResponse = NameSearchActivity.this;
                backgroundTask.execute(type, newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                String profileId = Integer.toString(idList.get(position));
                intent.putExtra("profileId", profileId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void processResponse(String result) {
        arrayAdapter.clear();
        idList.clear();
        if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String firstName = listItem.getString("first_name");
                    String lastName = listItem.getString("last_name");
                    int id = listItem.getInt("id");
                    arrayAdapter.add(firstName + " " + lastName);
                    idList.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
