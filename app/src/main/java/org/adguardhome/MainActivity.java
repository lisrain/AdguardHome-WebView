package org.adguardhome;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AdguardHome";
    private WebView webView;
    private View statusBarPlaceholder;

    private static final String TARGET_URL = "http://127.0.0.1:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate start");

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "setContentView done");

        statusBarPlaceholder = findViewById(R.id.statusBarPlaceholder);
        webView = findViewById(R.id.webview);

        setupSystemBars();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setAllowFileAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(TARGET_URL);
        Log.d(TAG, "loadUrl done");
    }

    private void setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Log.d(TAG, "insets: top=" + systemBars.top + " bottom=" + systemBars.bottom);

            if (statusBarPlaceholder != null) {
                statusBarPlaceholder.getLayoutParams().height = systemBars.top;
                statusBarPlaceholder.requestLayout();
                Log.d(TAG, "statusBarPlaceholder height=" + systemBars.top);
            }

            return insets;
        });

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.setAppearanceLightStatusBars(true);
            Log.d(TAG, "controller setAppearanceLightStatusBars true");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed canGoBack=" + webView.canGoBack());
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}