package com.nguyen.emojistatus2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UserAdapter(options: FirestoreRecyclerOptions<User>): FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder>(options) {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {
        val text1 = holder.itemView.findViewById<TextView>(android.R.id.text1)
        text1.text = model.displayName
        val text2 = holder.itemView.findViewById<TextView>(android.R.id.text2)
        text2.text = model.emojis
    }
}