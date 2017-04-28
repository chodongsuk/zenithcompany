package kr.ds.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.kr.zenithcompany.R;

import kr.ds.handler.PopupHandler;
import kr.ds.utils.DsObjectUtils;

/*
 * 종료 시 팝업 관련
 * 티스토어 및 마켓 구분 onclick 리스너 수정
 */
@SuppressLint("ValidFragment")
public class PopupDialog extends DialogFragment implements OnClickListener{
	private TextView mTextView1;
	private WebView mWebView;
	private PopupHandler mPopupHandler;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public PopupDialog(PopupHandler popupHandler) {
		// TODO Auto-generated constructor stub
		mPopupHandler = popupHandler;
	}

	private DialogResultListner mDialogResultListner;
	public interface DialogResultListner {
		public void onCancel();
	}
	public PopupDialog callback (DialogResultListner dialogresultlistner){
		this.mDialogResultListner = dialogresultlistner;
		return this;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
		View view = mLayoutInflater.inflate(R.layout.popup_dialog, null);
		mWebView = (WebView) view.findViewById(R.id.web);
		if(!DsObjectUtils.isEmpty(mPopupHandler.getContent())){
			mWebView.loadData(mPopupHandler.getContent(), "text/html; charset=utf-8", "UTF-8");
			mWebView.setWebViewClient(new DsWebViewClient());
		}
		(mTextView1 = (TextView)view.findViewById(R.id.textView1)).setOnClickListener(this);
		mBuilder.setView(view);
		return mBuilder.create();
	}

	private class DsWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.textView1:
				dismiss();
				break;
			default:
				break;
		}
	}
}
