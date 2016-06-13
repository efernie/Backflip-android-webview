package com.backflip.backflip_android_webview.backflip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

  private WebView web;

  // Logging
  private Boolean VERBOSE = true;
  private String TAG = "Backflip";

  private final String localhostProtocol = "http://";
  private final String localhostUrl = "192.168.1.137";
  private final String localhostPort = ":3000";
  private final String fullLocalhostUrl = localhostProtocol + localhostUrl + localhostPort;

  public static Context context;

  private final WebViewClient webClient = new WebViewClient() {

    @Override
    public boolean shouldOverrideUrlLoading( WebView view, String url ) {

      if ( Uri.parse(url).getHost().equals(localhostUrl) ) {
        return false;
      }

      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      startActivity(intent);
      return true;
    }
  };


  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MainActivity.context = getApplicationContext();

    setContentView(R.layout.activity_main);

    web = (WebView)findViewById(R.id.web);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    web.getSettings().setDomStorageEnabled(true);
    web.getSettings().setAllowFileAccess(true);

    web.addJavascriptInterface(new BackflipJavascriptInterface(this), "Android");
    web.setWebViewClient(webClient);

    web.loadUrl(fullLocalhostUrl);

    // To catch the console logs and display them in the logcat
    web.setWebChromeClient(new WebChromeClient() {
      public boolean onConsoleMessage(ConsoleMessage cm) {
        if (VERBOSE) Log.d(TAG, cm.message() + " -- From line "
          + cm.lineNumber() + " of "
          + cm.sourceId() );
        return true;
      }
    });
  }

  public class BackflipJavascriptInterface {
    final Context mContext;

    BackflipJavascriptInterface(Context c) {
      mContext = c;
    }

    @JavascriptInterface
    // TODO: add code to take picture and return it to the webapp
    public void takePicture () {

    }
  }
}
