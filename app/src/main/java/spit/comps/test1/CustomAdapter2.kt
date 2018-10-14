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
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*


class CustomAdapter2(val userList: MutableList<ListItem>, val context: Context) : RecyclerView.Adapter<CustomAdapter2.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter2.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item2, parent, false)
        return ViewHolder(v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter2.ViewHolder, position: Int) {
        holder.bindItems(userList[position])

        holder.checkBox.setOnClickListener{

            if (userList.get(position).check.equals("1"))
            {
                val reference = FirebaseDatabase.getInstance().getReference() as DatabaseReference

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var parent_key=""

                        dataSnapshot.child("sublist").children.forEach {
                            val obj= it.getValue<ListItem>(ListItem::class.java)
                            val main_key = it.key

                            if (obj!!.title.equals(userList.get(position).title))
                            {
                                parent_key = main_key
                            }
                        }

                        reference.child("sublist").child(parent_key).child("check").setValue("0")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                })
            }

            else
            {
                val reference = FirebaseDatabase.getInstance().getReference() as DatabaseReference

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var parent_key=""

                        dataSnapshot.child("sublist").children.forEach {
                            val obj= it.getValue<ListItem>(ListItem::class.java)
                            val main_key = it.key

                            if (obj!!.title.equals(userList.get(position).title))
                            {
                                parent_key = main_key
                            }
                        }

                        reference.child("sublist").child(parent_key).child("check").setValue("1")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                })
            }
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewTitle = itemView.findViewById(R.id.item2_title) as TextView
        val checkBox = itemView.findViewById(R.id.item2_check) as CheckBox

        fun bindItems(user: ListItem) {
            textViewTitle.text = user.title

            if (user.check.equals("1"))
            {
                checkBox.isChecked = true
            }
        }

    }
}