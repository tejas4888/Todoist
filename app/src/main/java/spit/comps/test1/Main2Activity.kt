package spit.comps.test1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        val recyclerView = findViewById(R.id.recyclerView_items) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val reference = FirebaseDatabase.getInstance().getReference() as DatabaseReference

        val menu: MutableList<ListItem> = mutableListOf()


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var parent_key=""

                dataSnapshot.child("lists").children.forEach {
                    val obj= it.getValue<ListMain>(ListMain::class.java)
                    val main_key = it.key

                    if (obj!!.title.equals(intent.getStringExtra("title")))
                    {
                        parent_key = main_key
                    }
                }

                Log.v("PArent",parent_key)
                menu.clear()
                dataSnapshot.child("sublist").children.forEach {
                    val obj= it.getValue<ListItem>(ListItem::class.java)
                    if (obj!!.list.equals(parent_key.toString()))
                    {
                        menu.add(obj)
                    }
                }

                Log.v("Menu", menu.size.toString())

                val adapter = CustomAdapter2(menu, this@Main2Activity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })


        val filter_fab = findViewById(R.id.list_items_fab) as FloatingActionButton

        filter_fab.setOnClickListener(View.OnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.fragment_bottom_sheet2, null)

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(bottomSheetView)
            dialog.show()

            val filter_button = bottomSheetView.findViewById(R.id.list_item_button) as Button
            val title = bottomSheetView.findViewById(R.id.list_item_title) as EditText

            filter_button.setOnClickListener {

                reference.child("lists").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach {
                            val obj= it.getValue<ListMain>(ListMain::class.java)
                            val main_key = it.key
                            if (obj!!.title.equals(intent.getStringExtra("title")))
                            {
                                val key = reference.child("sublist").push().key
                                reference.child("sublist").child(key).child("list").setValue(main_key)
                                reference.child("sublist").child(key).child("title").setValue(title.text.toString())
                                reference.child("sublist").child(key).child("check").setValue("0")
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                })

                dialog.dismiss()
            }
        })

    }
}
