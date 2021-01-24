package org.gorczyca.futuremindrecruitmenttask.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.gorczyca.futuremindrecruitmenttask.R
import org.gorczyca.futuremindrecruitmenttask.database.list_item.ListItem
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by: Radosław Gorczyca
 * 29.12.2020 10:11
 */
class ItemListAdapter(
    private val itemsList: List<ListItem>?,
    private val listener: (ListItem) -> Unit
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textDescription: TextView = view.findViewById(R.id.text_description)
        val textDate: TextView = view.findViewById(R.id.text_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!itemsList.isNullOrEmpty()) {
            val item: ListItem = itemsList[position]

            Picasso.get().load(item.imageUrl).fit().centerCrop()
                .placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image)
                .into(holder.image)

            holder.textTitle.apply {
                text = item.title
            }
            holder.textDescription.apply {
                text = item.description
            }
            holder.textDate.apply {
                var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val newDate: Date? = format.parse(item.modificationDate)
                val date: String

                if (newDate != null) {
                    format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                    date = format.format(newDate)
                } else {
                    date = item.modificationDate
                }
                text = date
            }

            holder.itemView.setOnClickListener {
                listener(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList?.size ?: 0
    }

}