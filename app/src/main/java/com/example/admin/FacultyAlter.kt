package com.example.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.database.*
import android.text.Editable

import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import android.widget.AdapterView.OnItemLongClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.DialogInterface

import android.content.Intent

class FacultyAlter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_alter)

        val add:FloatingActionButton=findViewById(R.id.add)
        val listView: ListView = findViewById(R.id.listview)
        val arrayList: ArrayList<String> = ArrayList()

        var head:Int
        val reference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Staff")
        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("InflateParams")
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                head=snapshot.child("Head").value.toString().toInt()
                while(head>0){
                    arrayList.add(snapshot.child(""+head).child("Name").value.toString())
                    head=snapshot.child(""+head).child("Next").value.toString().toInt()
                }
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@FacultyAlter,R.layout.list_view,arrayList)
                listView.adapter = arrayAdapter;

                add.setOnClickListener{
                    val alert = AlertDialog.Builder(this@FacultyAlter)
                    val view: View = layoutInflater.inflate(R.layout.custom_dialog,null)
                    val  staffid:EditText=view.findViewById(R.id.staffid)
                    val  staffname:EditText=view.findViewById(R.id.staffname)
                    val btn_cancel:Button=view.findViewById(R.id.btn_cancel)
                    val btn_add:Button=view.findViewById(R.id.btn_okay)
                    alert.setView(view)
                    val alertDialog = alert.create()
                    alertDialog.setCanceledOnTouchOutside(false)
                    btn_cancel.setOnClickListener{
                        alertDialog.dismiss()
                    }
                    btn_add.setOnClickListener {
                        if (staffname.text.toString() != "" && staffid.text.toString() != "") {
                            head=snapshot.child("Head").value.toString().toInt()
                            if(head>0){
                                while(head>0){
                                    if(head==staffid.text.toString().toInt()){
                                        head=-1
                                        Toast.makeText(this@FacultyAlter,"Staff ID already available",Toast.LENGTH_LONG).show()
                                        break
                                    }else {
                                        head = snapshot.child("" + head).child("Next").value.toString()
                                            .toInt()
                                    }
                                }
                                if(head!=-1){
                                    reference.child(staffid.text.toString()).child("Name").setValue(staffname.text.toString())
                                    reference.child(staffid.text.toString()).child("Front").setValue(snapshot.child("Tail").value.toString().toInt())
                                    reference.child(staffid.text.toString()).child("Next").setValue(0)
                                    reference.child(staffid.text.toString()).child("Student").child("Size").setValue(0)
                                    reference.child(staffid.text.toString()).child("StaffAdvisor").setValue(0)
                                    reference.child(snapshot.child("Tail").value.toString()).child("Next").setValue(staffid.text.toString().toInt())
                                    reference.child("Tail").setValue(staffid.text.toString().toInt())
                                    alertDialog.dismiss()
                                    Toast.makeText(this@FacultyAlter,"Added Successfully",Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this@FacultyAlter,"Staff ID already available",Toast.LENGTH_LONG).show()
                                }
                            }else{
                                reference.child(staffid.text.toString()).child("Name").setValue(staffname.text.toString())
                                reference.child(staffid.text.toString()).child("Front").setValue(0)
                                reference.child(staffid.text.toString()).child("Next").setValue(0)
                                reference.child(staffid.text.toString()).child("Student").child("Size").setValue(0)
                                reference.child(staffid.text.toString()).child("StaffAdvisor").setValue(0)
                                reference.child("Head").setValue(staffid.text.toString().toInt())
                                reference.child("Tail").setValue(staffid.text.toString().toInt())
                                alertDialog.dismiss()
                                Toast.makeText(this@FacultyAlter,"Added Successfully",Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this@FacultyAlter,"Enter All Details",Toast.LENGTH_LONG).show()
                        }
                    }
                    alertDialog.show()
                }
                listView.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id -> //ArrayList arrayList=databaseHelper.getAllText();

                        val builder = AlertDialog.Builder(this@FacultyAlter)
                        builder.setMessage("Do you want to Remove ?")
                        builder.setTitle("REMOVE")
                        builder.setCancelable(false)
                        builder.setPositiveButton(
                            "Yes"
                        ) { dialog, which ->

                            var count:Int=0
                            head=snapshot.child("Head").value.toString().toInt()
                            while (head>0){
                                if(count==position){
                                    break
                                }else {
                                    head = snapshot.child("" + head).child("Next").value.toString()
                                        .toInt()
                                    count++
                                }
                            }
                            if(snapshot.child(head.toString()).child("Student").child("Size").value.toString().toInt()==0){
                                when {
                                    count==0 -> {
                                        reference.child(snapshot.child(head.toString()).child("Next").value.toString()).child("Front").setValue(0)
                                        reference.child("Head").setValue(snapshot.child(head.toString()).child("Next").value.toString().toInt())
                                    }
                                    snapshot.child("Tail").value.toString().toInt()==head -> {
                                        reference.child(snapshot.child(head.toString()).child("Front").value.toString()).child("Next").setValue(0)
                                        reference.child("Tail").setValue(snapshot.child(head.toString()).child("Front").value.toString().toInt())
                                    }
                                    else -> {
                                        reference.child(
                                            snapshot.child(head.toString())
                                                .child("Front").value.toString()
                                        ).child("Next").setValue(
                                            snapshot.child(head.toString())
                                                .child("Next").value.toString().toInt()
                                        )
                                        reference.child(
                                            snapshot.child(head.toString())
                                                .child("Next").value.toString()
                                        ).child("Front").setValue(
                                            snapshot.child(head.toString())
                                                .child("Front").value.toString().toInt()
                                        )
                                    }
                                }
                            }else{
                                Toast.makeText(this@FacultyAlter,"Staff cannot be Removed\nHe/She is a Mentor",Toast.LENGTH_LONG).show()
                            }
                        }
                        builder.setNegativeButton(
                            "No"
                        ) { dialog, which -> dialog.cancel() }
                        val alertDialog = builder.create()
                        alertDialog.show()

                        return@OnItemLongClickListener false
                    }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}