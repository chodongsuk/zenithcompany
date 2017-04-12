package kr.ds.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kr.zenithcompany.R;

import kr.ds.config.Config;

/**
 * Created by Administrator on 2017-01-31.
 */
public class WebFragment extends BaseFragment {
    private View mView;
    private Context mContext;
    private WebView mWebView;
    private ProgressBar pb;


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_web, null);
        mWebView = (WebView)mView.findViewById(R.id.web);
        pb = (ProgressBar) mView.findViewById(R.id.progressBar);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getWebView(Config.URL+  Config.URL_WEB);
    }

    public void getWebView(final String url) {

        mWebView.getSettings().setLoadWithOverviewMode(true);// 축소된상태
        mWebView.getSettings().setUseWideViewPort(true); // Web페이지사이즈
        mWebView.setVerticalScrollbarOverlay(true);
        //mWebView.getSettings().setSupportZoom(true);// 줌컨트롤
        //mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setSavePassword(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClients());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                }).setCancelable(false).create().show();
                return true;

            }

            ;
        });
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class WebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            pb.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pb.setVisibility(View.GONE);
        }
    }
}
