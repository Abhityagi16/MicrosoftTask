package com.abhishek.microsofttask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php";
    private static final String WIKIPEDIA_API_REQUEST_TAG = "wikipedia.request";

    private RequestQueue mRequestQueue;
    private RecyclerView mWikipediaList;
    private EditText mSearchBox;
    private ArrayList<Page> mPages;
    private PagesAdapter mPagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(this);
        mWikipediaList = (RecyclerView) findViewById(R.id.wikipedia_page_list);
        mSearchBox = (EditText) findViewById(R.id.search);
        mPagesAdapter = new PagesAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mWikipediaList.setAdapter(mPagesAdapter);
        mWikipediaList.setLayoutManager(layoutManager);
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString();
                requestWikipediaAPI(searchQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void requestWikipediaAPI(String searchQuery) {
        StringBuilder requestUrl = new StringBuilder(WIKIPEDIA_API_URL);
        requestUrl.append("?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=400&" +
                "pilimit=50&generator=prefixsearch&gpssearch=");
        requestUrl.append(searchQuery);
        requestUrl.append("&gpslimit=50");

        String url = requestUrl.toString();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, "Request size " + res)
                        Log.d(TAG, response.toString());
                        JSONObject queryObject;
                        try {
                            queryObject = response.getJSONObject("query");
                            JSONObject pagesObject = queryObject.getJSONObject("pages");
                            if(pagesObject != null) {
                                parsePages(pagesObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = super.getHeaders();
//                header.put("")
//            }
        };
        jsObjRequest.setTag(WIKIPEDIA_API_REQUEST_TAG);
        mRequestQueue.cancelAll(WIKIPEDIA_API_REQUEST_TAG);
        mRequestQueue.add(jsObjRequest);
    }

    public void parsePages(JSONObject pages) {
        mPages = new ArrayList<>();
        Iterator<String> iterator = pages.keys();
        while(iterator.hasNext()) {
            String id = iterator.next();
            try {
                JSONObject pageObject = pages.getJSONObject(id);
                String title = pageObject.getString("title");
                String imageUrl = null;
                if(pageObject.has("thumbnail")) {
                    JSONObject thumbnailObject = pageObject.getJSONObject("thumbnail");
                    imageUrl = thumbnailObject.getString("source");
                }
                Page page = new Page(id, title, imageUrl);
                mPages.add(page);
            } catch (JSONException e) {
                Log.d(TAG, "Tag not found for " + id);
                e.printStackTrace();
            }
        }
        mPagesAdapter.setPageList(mPages);
        mPagesAdapter.notifyDataSetChanged();
    }
}
