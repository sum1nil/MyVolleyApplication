package xyz.sum1nil.myvolleyapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import xyz.sum1nil.myvolleyapplication.MyVolleyLib.JsonHelper;
import xyz.sum1nil.myvolleyapplication.MyVolleyLib.MyVolleySingleton;

public class MainVolleyActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private static String TAG = "MainVolleyActivity";
    private ArrayAdapter<String> spinnerAdapter;
    private TextView mTextView;
    private Button mButton;
    private Spinner mLangSpinner;
    private RequestQueue mQueue;
    private ArrayList<String> mLangList;
    private HashMap<String, Object> mLangListsMap = new HashMap<String, Object>();
    public  HashMap<String, Object> getmLangListsMap() {
        return mLangListsMap;
    }
    public void setmLangListsMap(HashMap<String, Object> mLangListsMap) {
        this.mLangListsMap = mLangListsMap;
    }


    public MainVolleyActivity() {
        super();
        Log.i(TAG, "In Constructor");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_volley);
        mTextView = findViewById(R.id.text_lang);
        mButton = findViewById(R.id.button);
        mLangSpinner = findViewById(R.id.spinner_langs);
        mLangList = new ArrayList<String>();
        mLangListsMap = new HashMap<>(100);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mQueue = MyVolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        StringBuilder requestStringBuilder = new StringBuilder();
        /*
        * Yandex format to get language list
        * https://translate.yandex.net/api/v1.5/tr.json/getLangs ?
        * key=<API key>
        * & [ui=<language code>]
        * & [callback=<name of the callback function>]
        */
        requestStringBuilder.append(getString(R.string.api_url) +  "getLangs?key=");
        requestStringBuilder.append(getString(R.string.api_key));
        requestStringBuilder.append("&ui=en");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestStringBuilder.toString(), new JSONObject(), this, this);
        jsonObjectRequest.setTag("reqlangs");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.add(jsonObjectRequest);
            }
        });
    }
    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        mTextView.setText(error.getMessage());
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(Object response) {
          mTextView.setText("Response is: \n" + response);
          try {
              mTextView.setText(((JSONObject)response).getString("langs"));
              mLangListsMap = (HashMap)JsonHelper.getMap((JSONObject)response, "langs");
             // TODO: Loop the Map and add to List<String>
              for (Map.Entry<String, Object> entry : mLangListsMap.entrySet()) {
                  mLangList.add(entry.getValue().toString());
              }
           }
          catch (JSONException e) {
              Log.d(TAG, e.getMessage());
              e.printStackTrace();
              System.exit(2);
          }
          catch (UnknownError e) {
              Log.d(TAG, e.getMessage());
              e.printStackTrace();
              System.exit(3);
          }

          spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mLangList);
          spinnerAdapter.setDropDownViewResource
                  (android.R.layout.simple_spinner_dropdown_item);
          mLangSpinner.setAdapter(spinnerAdapter);
    }

    private void fillSpinner(JSONObject jsonObject)  {

    }
}
