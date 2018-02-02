package gov.cipam.gi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.YoutubeAdapter;
import gov.cipam.gi.model.youtubeItem;

/**
 * Created by hp on 1/23/2018.
 */

public class YoutubeFragment extends Fragment implements View.OnClickListener,YoutubeAdapter.setOnYoutubeItemClickedListener {

    String url="https://www.googleapis.com/youtube/v3/search?key=AIzaSyCDkCxwYCvLELBDFfufi9xMZok1RM-hkLI&channelId=UCJ6td3C9QlPO9O_J5dF4ZzA&part=snippet,id&order=date&maxResults=50";
    private RecyclerView recyclerView;
    private ArrayList<youtubeItem> items = new ArrayList<>();
    Button dataButton;

    public static YoutubeFragment newInstance() {

        Bundle args = new Bundle();

        YoutubeFragment fragment = new YoutubeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_youtube,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.youtubeRecyclerView);
        dataButton= view.findViewById(R.id.dataButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        dataButton.setOnClickListener(this);
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData(){
//https://www.youtube.com/watch?v=SJeng55MOl4
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObj = new JSONObject(response);
                            longInfo(response);
                            if (mainObj!=null){
                                JSONArray item_json = mainObj.getJSONArray("items");
                                if (item_json!=null){
                                    for (int j =0 ; j<= item_json.length() ; j++){
                                        JSONObject item = item_json.getJSONObject(j);
                                        if(item!=null){
                                            JSONObject id = item.getJSONObject("id");
                                            String videoLink = "https://www.youtube.com/watch?v="+id.getString("videoId");
                                            JSONObject snippet = item.getJSONObject("snippet");
                                            if(snippet!=null){
                                                JSONObject thumb = snippet.getJSONObject("thumbnails");
                                                JSONObject medium = thumb.getJSONObject("medium");
                                                String url = medium.getString("url");//"https://i.ytimg.com/vi/a6MVH9SX5do/hqdefault.jpg";
                                                String title = snippet.getString("title");
                                                String description = "MonsterCat";
                                                String publisedAt = snippet.getString("publishedAt");
                                                Log.e("title",title);
                                                Log.e("description",description);
                                                Log.e("publishedAt",publisedAt);
                                                Log.e("urrrrl",url);
                                                Log.e("videolink",videoLink);
                                                youtubeItem item1 = new youtubeItem(title,description,publisedAt,url,videoLink);
                                                items.add(item1);
                                            }
                                        }

                                    }
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }
    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.e("response;", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.e("response", str);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.dataButton:
                recyclerView.setAdapter(new YoutubeAdapter(this,items,getActivity()));
                break;
        }

    }

    @Override
    public void onItemClickedListener(View view, int position) {
        Toast.makeText(getContext(),items.get(position).getVideoLink(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Social Feed");
    }
}
