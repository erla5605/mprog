package com.mprog.oppnawebbsidor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

// App with a web view and buttons to links to three web pages.
public class WebActivity extends AppCompatActivity {

    private String googleLink = "https://www.google.com/";
    private String wikiLink = "https://www.wikipedia.org/";
    private String svtLink = "https://www.svt.se/";

    private Button googleButton, wikiButton, svtButton;
    private ImageButton backButton, forwardButton;
    private WebView webView;

    // Sets up the web view and the buttons.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        googleButton = findViewById(R.id.google_button);
        googleButton.setOnClickListener(new View.OnClickListener() {
            // Loads the google.com to the web view.
            @Override
            public void onClick(View v) {
                webView.loadUrl(googleLink);
            }
        });
        wikiButton = findViewById(R.id.wiki_button);
        wikiButton.setOnClickListener(new View.OnClickListener() {
            // Loads wikipedia.org to the web view.
            @Override
            public void onClick(View v) {
                webView.loadUrl(wikiLink);
            }
        });
        svtButton = findViewById(R.id.svtButton);
        svtButton.setOnClickListener(new View.OnClickListener() {
            // Loads svt.se to the webv iew.
            @Override
            public void onClick(View v) {
                webView.loadUrl(svtLink);
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            // Navigate the web view backwards if possible.
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        forwardButton = findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            // Navigate the web view forwards if possible.
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    // If back button on device is pressed the web view will go back instead of out of app.
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }


    }
}
