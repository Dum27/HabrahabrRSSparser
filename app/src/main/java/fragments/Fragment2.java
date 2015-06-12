package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.work.rss.R;



public class Fragment2 extends Fragment
{
    private WebView mWebView;
    private String link;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            link = bundle.getString("link");
        }

        mWebView = (WebView) getActivity().findViewById(R.id.webview);

        mWebView.setWebViewClient(new HelloWebViewClient());


        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(link);
    }

    private class HelloWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
