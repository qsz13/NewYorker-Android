package com.danielqiu.newyorker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.loopj.android.http.*;

public class MainActivity extends FragmentActivity {

    private WebView mWebView;
    private ActionMode mActionMode = null;
    private NYWebChromeClient mWebChromeClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new NYWebViewClient());
        mWebChromeClient = new NYWebChromeClient();
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.loadUrl("http://www.newyorker.com/");
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            mActionMode = mode;
            Menu menu = mode.getMenu();
            // Remove the default menu items (select all, copy, paste, search)
//            menu.clear();

            // If you want to keep any of the defaults,
            // remove the items you don't want individually:

            // Inflate your own menu items
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
        }

        super.onActionModeStarted(mode);

    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        mActionMode = null;
        super.onActionModeFinished(mode);
    }


    public void onContextualMenuItemClicked(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                sharePage();
                break;
            case R.id.definition:
                getDefinition();
                break;
            default:
                // ...
                break;
        }

        // This will likely always be true, but check it anyway, just in case
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    public void sharePage()
    {
        String url = mWebView.getUrl();
        String title = mWebView.getTitle();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title+": "+url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void getDefinition()
    {
//        mWebView.loadUrl("javascript:if0.textOut(document.getSelection().toString());"‌​);
        mWebView.evaluateJavascript("console.log(window.getSelection());", null);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                translate(mWebChromeClient.message);

            }
        }, 50);
    }

    public void translate(String text) {





        AsyncHttpClient client = new AsyncHttpClient();
        String query = "";
        try {
             query = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.get("http://fanyi.youdao.com/openapi.do?keyfrom=mbnxczmvnbz&key=1679713817&type=data&doctype=json&version=1.1&q="+query, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                String result = "No result";
                try {
                    result = parseResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayTranslateResult(result);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void displayTranslateResult(String text) {
        TranslateDialogFragment dialog = new TranslateDialogFragment();
        dialog.setTranslateText(text);
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }

    public String parseResult(JSONObject response) throws JSONException {
        String query = response.getString("query");
        String result = query+":\n\n";
        JSONObject basic = null;
        try {
            basic = response.getJSONObject("basic");
        } catch (JSONException e)
        {

            JSONArray translation = response.getJSONArray("translation");
            for (int i=0; i < translation.length(); i++)
            {
                String t = translation.getString(i);
                result+= (t+"\n");
            }
            return result;

        }

        JSONArray explains = basic.getJSONArray("explains");
        String phonetic = "";
        try {
            phonetic = basic.getString("phonetic");
        } catch (JSONException e)
        {

        }


        for (int i=0; i < explains.length(); i++)
        {
            String e = explains.getString(i);
            result+= (e+"\n");
        }

        result += "\n"+phonetic;
        return result;

    }





}