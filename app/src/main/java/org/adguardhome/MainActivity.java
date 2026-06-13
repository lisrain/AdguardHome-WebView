package org.adguardhome;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;
    private View statusBarPlaceholder;
    private final List<String> historyStack = new ArrayList<>();
    private boolean isLoginTransition = false;

    private static final String TARGET_URL = "http://127.0.0.1:3000";
    private static final String DASHBOARD_URL = TARGET_URL + "/";
    private static final String LOGIN_PAGE = "/login.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        statusBarPlaceholder = findViewById(R.id.statusBarPlaceholder);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        webView = findViewById(R.id.webview);

        setupSystemBars();
        setupWebView();
        setupSwipeRefresh();
        setupBackCallback();

        webView.loadUrl(TARGET_URL);
    }

    private void setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            if (statusBarPlaceholder != null) {
                statusBarPlaceholder.getLayoutParams().height = systemBars.top;
                statusBarPlaceholder.requestLayout();
            }

            return insets;
        });

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
            controller.setAppearanceLightStatusBars(!isDarkMode);
        }
    }

    private void setupWebView() {
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

        boolean isDarkMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            settings.setAlgorithmicDarkeningAllowed(isDarkMode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            settings.setForceDark(isDarkMode ? WebSettings.FORCE_DARK_ON : WebSettings.FORCE_DARK_AUTO);
        }

        webView.setWebChromeClient(new WebChromeClient());
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(
            R.color.swipe_refresh_color_1,
            R.color.swipe_refresh_color_2,
            R.color.swipe_refresh_color_3,
            R.color.swipe_refresh_color_4
        );

        swipeRefresh.setOnRefreshListener(() -> {
            webView.reload();
        });
    }

    private void setupBackCallback() {
        OnBackPressedCallback backCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (historyStack.isEmpty()) {
                    finish();
                    return;
                }

                historyStack.remove(historyStack.size() - 1);
                webView.evaluateJavascript("history.back()", null);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backCallback);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefresh.setRefreshing(false);

                if (url.contains(LOGIN_PAGE)) {
                    historyStack.clear();
                    isLoginTransition = true;
                    return;
                }

                if (isLoginTransition) {
                    historyStack.clear();
                    isLoginTransition = false;
                    return;
                }

                if (isDashboardUrl(url)) {
                    historyStack.clear();
                    return;
                }

                if (!historyStack.isEmpty() && historyStack.get(historyStack.size() - 1).equals(url)) {
                    return;
                }

                historyStack.add(url);
            }
        });
    }

    private boolean isDashboardUrl(String url) {
        if (url == null) return false;
        String baseUrl = url.contains("#") ? url.substring(0, url.indexOf("#")) : url;
        return baseUrl.equals(TARGET_URL) || baseUrl.equals(DASHBOARD_URL);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}