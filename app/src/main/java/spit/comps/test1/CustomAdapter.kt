package spit.comps.test1


import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast


class CustomAdapter(val userList: MutableList<ListMain>, val context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])

        holder.cardView.setOnClickListener {
            val intent = Intent(context,Main2Activity::class.java)
            intent.putExtra("title",userList.get(position).title)
            intent.putExtra("description",userList.get(position).description)
            context.startActivity(intent)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewName = itemView.findViewById(R.id.recycler_item_text) as TextView
        val textViewDesc = itemView.findViewById(R.id.recyler_item_description) as TextView
        val cardView = itemView.findViewById(R.id.recycler_card) as CardView

        fun bindItems(user: ListMain) {
            textViewName.text = user.title
            textViewDesc.text = user.description
        }

    }
}