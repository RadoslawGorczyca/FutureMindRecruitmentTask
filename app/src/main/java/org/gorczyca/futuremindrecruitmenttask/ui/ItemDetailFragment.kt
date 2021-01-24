package org.gorczyca.futuremindrecruitmenttask.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import org.gorczyca.futuremindrecruitmenttask.R


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var itemUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_URL)) {
                itemUrl = it.getString(ARG_ITEM_URL)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        val wb = rootView.findViewById<WebView>(R.id.webview_item_url)

        wb.webViewClient = object : WebViewClient() {
            val progressDialog: ProgressDialog = ProgressDialog(context)
            override fun onPageStarted(
                view: WebView,
                url: String,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                progressDialog.setTitle("Loading...")
                progressDialog.setMessage("Please wait...")
                progressDialog.setCancelable(false)
                progressDialog.show()
            }

            override fun onPageCommitVisible(
                view: WebView,
                url: String
            ) {
                super.onPageCommitVisible(view, url)
                if (progressDialog != null) {
                    progressDialog.dismiss()
                }
            }
        }

        wb.apply {
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            wb.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            wb.loadUrl(itemUrl.toString())
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_URL = "item_url"
    }
}