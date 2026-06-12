package org.adguardhome;

import android.app.Activity;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private static final String TAG = "AdguardHome";
    private WebView webView;
    private static final String TARGET_URL = "http://127.0.0.1:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate start");

        webView = new WebView(this);
        setContentView(webView);
        Log.d(TAG, "setContentView done");

        setupWindow();

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

    private void setupWindow() {
        Log.d(TAG, "setupWindow, SDK=" + Build.VERSION.SDK_INT);

        getWindow().setStatusBarColor(0xFFF5F5F5);
        getWindow().setNavigationBarColor(0x00000000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Window window = getWindow();
            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                Log.d(TAG, "controller found");
                controller.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                );
                controller.show(WindowInsets.Type.statusBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
            webView.setOnApplyWindowInsetsListener((v, insets) -> {
                Insets statusInsets = insets.getInsets(WindowInsets.Type.statusBars());
                Insets navInsets = insets.getInsets(WindowInsets.Type.navigationBars());
                Log.d(TAG, "insets: statusBars.top=" + statusInsets.top + " navBars.bottom=" + navInsets.bottom);
                v.setPadding(0, statusInsets.top, 0, navInsets.bottom);
                Log.d(TAG, "padding set: top=" + statusInsets.top + " bottom=" + navInsets.bottom);
                return insets;
            });
            webView.requestApplyInsets();
            Log.d(TAG, "listener set");
        } else {
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            Log.d(TAG, "legacy: visibleDisplayFrame top=" + rect.top);
            webView.setPadding(0, rect.top, 0, 0);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged focus=" + hasFocus);
        if (hasFocus) {
            setupWindow();
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