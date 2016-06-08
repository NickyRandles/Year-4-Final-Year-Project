package com.project.nicky.medicalmanager.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsFragment extends Fragment implements BackgroundResponse {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private BackgroundTask backgroundTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(arrayAdapter);

        backgroundTask = new BackgroundTask(getActivity());
        backgroundTask.backgroundResponse = this;
        String type = "getContacts";
        UserInformation userInformation = new UserInformation(getActivity());
        String username = userInformation.getUsername();
        String userType = userInformation.getUserType();
        backgroundTask.execute(type, username, userType);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:018027463"));
                startActivity(callIntent);
            }
        });
        return view;
    }

    @Override
    public void processResponse(String result) {
        if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String firstName = listItem.getString("first_name");
                    String lastName = listItem.getString("last_name");
                    arrayAdapter.add(firstName + " " + lastName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}