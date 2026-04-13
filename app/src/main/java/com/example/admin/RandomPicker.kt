package com.example.admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*


class RandomPicker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_picker)
        val share: SharedPreferences = getSharedPreferences("Detail", Context.MODE_PRIVATE)
        val rollno:TextView=findViewById(R.id.rollno)
        val name:TextView=findViewById(R.id.name)
        val randomNumber:TextView=findViewById(R.id.random)
        val generate: Button =findViewById(R.id.generate)
        val next:FloatingActionButton=findViewById(R.id.next)

        val gender = if(share.getInt("GenderPosition",0)==1){
            "MaleStudent"
        } else{
            "FemaleStudent"
        }
        val className=share.getString("ClassName","")
        var ihead:Int
        var count=1

        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noofStudent=snapshot.child("Class").child(className.toString()).child("Mentor").child(gender).child("Size").value.toString().toInt()
                val studentOrder = IntArray(noofStudent)
                ihead=snapshot.child("Class").child(className.toString()).child(gender).child("Head").value.toString().toInt()
                var i=0
                while (ihead>0){
                    studentOrder[i]=ihead
                    i++
                    ihead=snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Next").value.toString().toInt()
                }
                ihead=snapshot.child("Class").child(className.toString()).child(gender).child("Head").value.toString().toInt()
                i=0
                while (i<noofStudent-1){
                    if(snapshot.child("Student").child(studentOrder[i].toString()).child("CGPA").value.toString().toDouble()<snapshot.child("Student").child(studentOrder[i+1].toString()).child("CGPA").value.toString().toDouble())
                    {
                        val tempHead:Int=studentOrder[i]
                        studentOrder[i]=studentOrder[i+1]
                        studentOrder[i+1]=tempHead
                        i=-1
                    }
                    i++
                }

                name.text=snapshot.child("Student").child(studentOrder[count-1].toString()).child("Name").value.toString()
                rollno.text=snapshot.child("Student").child(studentOrder[count-1].toString()).child("RollNumber").value.toString()
                generate.setOnClickListener {
                    generate.isEnabled=false
                    next.isEnabled=true
                    randomNumber.text=snapshot.child("Class").child(className.toString()).child("Mentor").child(gender).child(count.toString()).value.toString()

                    var size=snapshot.child("Staff").child(randomNumber.text.toString()).child("Student").child("Size").value.toString().toInt()
                    reference.child("Staff").child(randomNumber.text.toString()).child("Student").child((++size).toString()).setValue(studentOrder[count-1])
                    reference.child("Staff").child(randomNumber.text.toString()).child("Student").child("Size").setValue(size)

                    reference.child("Student").child(studentOrder[count-1].toString()).child("Mentor").setValue(randomNumber.text.toString().toInt())
                }
                next.setOnClickListener {
                    count++
                    if(count<=noofStudent) {
                        next.isEnabled=false
                        generate.isEnabled=true
                        randomNumber.text = ""
                        name.text =
                            snapshot.child("Student").child(studentOrder[count - 1].toString())
                                .child("Name").value.toString()
                        rollno.text =
                            snapshot.child("Student").child(studentOrder[count - 1].toString())
                                .child("RollNumber").value.toString()
                    }else{
                        startActivity(Intent(this@RandomPicker,StaffAdvisor::class.java))
                        finish()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    override fun onBackPressed() {
        Toast.makeText(this,"Unable to GO BACK",Toast.LENGTH_LONG).show()
    }
}