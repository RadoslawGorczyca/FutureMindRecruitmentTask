package org.gorczyca.futuremindrecruitmenttask.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.gorczyca.futuremindrecruitmenttask.ui.viewmodels.ItemListViewModel
import org.gorczyca.futuremindrecruitmenttask.ui.viewmodels.ItemListViewModelFactory
import org.gorczyca.futuremindrecruitmenttask.R
import org.gorczyca.futuremindrecruitmenttask.database.list_item.ListItem
import org.gorczyca.futuremindrecruitmenttask.databinding.ActivityItemListBinding

import org.gorczyca.futuremindrecruitmenttask.repositories.MainRepository
import org.gorczyca.futuremindrecruitmenttask.utils.Utils

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityItemListBinding

    private var itemsList: List<ListItem>? = null

    private lateinit var itemsViewModel: ItemListViewModel
    private lateinit var mainRepository: MainRepository

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private val onItemClickListener: (ListItem) -> Unit = { item ->
        if (twoPane) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {

                    putString(ItemDetailFragment.ARG_ITEM_URL, item.url)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM_URL, item.url)
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupViewModel()
        downloadItems()
    }

    private fun setupViewModel() {
        mainRepository = MainRepository(this)
        val viewModelFactory = ItemListViewModelFactory(mainRepository)
        itemsViewModel =
            ViewModelProvider(this, viewModelFactory).get(ItemListViewModel::class.java)
        itemsViewModel.itemList.observe(this, { items ->
            itemsList = items
            setupRecyclerView(items)
        })
    }

    private fun downloadItems() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        if (isConnectedToInternet()) {
            swipeRefreshLayout.post {
                swipeRefreshLayout.isRefreshing = true
                mainRepository.refreshItemsList()
                hideSwipeRefreshDelayed()
            }
        }
    }

    private fun setupRecyclerView(items: List<ListItem>) {
        swipeRefreshLayout.isRefreshing = false
        val recycler = findViewById<RecyclerView>(R.id.item_list)
        recycler.adapter = ItemListAdapter(items, onItemClickListener)
    }

    private fun hideSwipeRefreshDelayed() {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefreshLayout.isRefreshing = false
        }, 5000)
    }

    private fun isConnectedToInternet(): Boolean {
        val isConnected = Utils.isConnectedToInternet(this)
        if (!isConnected) {
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(
                this,
                getString(R.string.error_no_internet),
                Toast.LENGTH_LONG
            ).show()
        }
        return isConnected
    }

    override fun onRefresh() {
        if (isConnectedToInternet()) {
            mainRepository.refreshItemsList()
            swipeRefreshLayout.isRefreshing = true
            hideSwipeRefreshDelayed()
        }
    }
}
