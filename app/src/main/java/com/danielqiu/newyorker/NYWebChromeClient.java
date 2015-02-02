package com.danielqiu.newyorker;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

/**
 * Created by danielqiu on 2/2/15.
 */
public class NYWebChromeClient extends WebChromeClient {

    String message = "";

    public boolean onConsoleMessage(ConsoleMessage cm) {
        message = cm.message();
        return true;
    }
}
