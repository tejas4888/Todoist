package spit.comps.test1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val reference = FirebaseDatabase.getInstance().getReference() as DatabaseReference

        val menu: MutableList<ListMain> = mutableListOf()

        reference.child("lists").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                menu.clear()
                println("d:"+dataSnapshot)
                dataSnapshot.children.mapNotNullTo(menu) {
                    Log.v("HEE","SDS")
                    it.getValue<ListMain>(ListMain::class.java) }

                val adapter = CustomAdapter(menu, this@MainActivity)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })

        val filter_fab = findViewById(R.id.list_main_fab) as FloatingActionButton

        filter_fab.setOnClickListener(View.OnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.fragment_list_bottom_sheet, null)

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(bottomSheetView)
            dialog.show()

            val filter_button = bottomSheetView.findViewById(R.id.list_main_button) as Button
            val title = bottomSheetView.findViewById(R.id.list_main_title) as EditText
            val desc = bottomSheetView.findViewById(R.id.list_main_desc) as EditText

            filter_button.setOnClickListener {

                val key = reference.child("lists").push().key
                reference.child("lists").child(key).child("title").setValue(title.text.toString())
                reference.child("lists").child(key).child("description").setValue(desc.text.toString())

                dialog.dismiss() }
        })
    }

}
