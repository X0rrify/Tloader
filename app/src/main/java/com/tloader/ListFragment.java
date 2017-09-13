package com.tloader;

import android.os.Bundle;
import android.os.Trace;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListFragment extends Fragment implements JsonDownloaderThread.OnResponseListener {

    public static final String TAG = ListFragment.class.getName();
    private static final int MIN_LIST_SIZE = 2;
    private String[] titles;
    private String[] avatarUrls;
    private ArrayList<String> chosenAvatarUrls = new ArrayList<>();

    private ListView infoList;
    private ArrayAdapter<String> infoArrayAdapter;

    public ListFragment() {
        // Required empty public constructor.
    }

    public static ListFragment newInstance() {
        ListFragment listFragment = new ListFragment();
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JsonDownloaderThread jsonDownloaderThread = new JsonDownloaderThread();
        jsonDownloaderThread.onResponseListener = this;
        jsonDownloaderThread.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        infoList = (ListView) view.findViewById(R.id.listview_info);
        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (String chosenAvatarUrl : chosenAvatarUrls) {
                    // If the url is already in the list, remove it.
                    if (chosenAvatarUrl.equals(avatarUrls[position])) {
                        chosenAvatarUrls.remove(chosenAvatarUrl);
                        return;
                    }
                }

                // If the url is not in the list, add it.
                chosenAvatarUrls.add(avatarUrls[position]);
            }
        });

        Button submitButton = (Button) view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenAvatarUrls.size() >= MIN_LIST_SIZE) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, ImageFragment.newInstance(chosenAvatarUrls), ImageFragment.TAG)
                            .commit();
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_select_items), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onJsonDownloadComplete(JSONArray jsonArray) {
        JSONObject userJsonObject;

        titles = new String[jsonArray.length()];
        avatarUrls = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                userJsonObject = new JSONObject(jsonArray.getJSONObject(i).getString("user"));
                titles[i] = jsonArray.getJSONObject(i).getString("title") + " | " +
                        userJsonObject.getString("login");
                avatarUrls[i] = userJsonObject.getString("avatar_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        infoArrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, titles);
        infoList.setAdapter(infoArrayAdapter);
    }
}
