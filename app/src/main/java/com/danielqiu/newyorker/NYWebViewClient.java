package com.danielqiu.newyorker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * Created by danielqiu on 2/1/15.
 */
public class NYWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Uri.parse(url).getHost().endsWith("newyorker.com")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest (WebView view, WebResourceRequest request)
    {
        if (request.getUrl().getHost().endsWith("newyorker.com"))
        {
            return null;
        }

        return new WebResourceResponse("","",null);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest (WebView view, String url)
    {
        if (Uri.parse(url).getHost().endsWith("newyorker.com"))
        {
            return null;
        }

        return new WebResourceResponse("","",null);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        String javascript="javascript: $( \".nav-links\" ).append('<li class=\"menu-item\"><a>Favourite</a></li>')";
        view.loadUrl(javascript);
    }

}
