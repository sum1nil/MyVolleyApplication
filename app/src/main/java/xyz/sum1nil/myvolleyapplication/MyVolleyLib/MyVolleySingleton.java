package xyz.sum1nil.myvolleyapplication.MyVolleyLib;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MyVolleySingleton {
    public final static String TAG = MyVolleySingleton.class.getSimpleName();
    private static MyVolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;
    private static Context mCtx;

    private MyVolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized MyVolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyVolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if(mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }
}
