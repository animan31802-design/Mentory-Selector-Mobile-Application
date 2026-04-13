package com.example.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import android.widget.AdapterView

import android.widget.Spinner
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

class StaffAdvisor : AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_advisor)

        val gendertv: TextView = findViewById(R.id.gendertv)
        val picker: Button = findViewById(R.id.picker)
        val genderSpinner: Spinner = findViewById(R.id.gender)
        val nextbtn: Button = findViewById(R.id.next)

        lateinit var list: List<String>
        var genderpos=0
        var gender=""
        var ihead=0
        var noofStudent =0
        var noofStaff = 0

        val share: SharedPreferences = getSharedPreferences("Detail", Context.MODE_PRIVATE)
        val className=share.getString("ClassName","")

        list = ArrayList()
        list.add("Gender")
        list.add("Male")
        list.add("Female")
        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            list
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = arrayAdapter;
        genderSpinner.setSelection(0)

        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        when (position) {
                            1 -> {
                                genderpos=1
                                gender="MaleStudent"
                                gendertv.text="Male"
                                picker.isEnabled = snapshot.child("Class").child(className.toString()).child("Mentor").child("MaleStudent").child("Size").value.toString().toInt() == 0
                            }
                            2 -> {
                                genderpos=2
                                gender="FemaleStudent"
                                gendertv.text="Female"
                                picker.isEnabled = snapshot.child("Class").child(className.toString()).child("Mentor").child("FemaleStudent").child("Size").value.toString().toInt() == 0
                            }
                            else -> {
                                genderpos=0
                                gender=""
                                gendertv.text="GENDER"
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                nextbtn.setOnClickListener {
                    when {
                        !picker.isEnabled && genderpos!=0 -> {
                            val editor: SharedPreferences.Editor = share.edit()
                            editor.putInt("GenderPosition",genderpos)
                            editor.apply()
                            startActivity(Intent(this@StaffAdvisor,RandomPicker::class.java))
                            finish()
                        }
                        genderpos!=0 -> {
                            Toast.makeText(
                                this@StaffAdvisor,
                                "Pick Staff And Click Next",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this@StaffAdvisor,
                                "Select Gender And Click Next",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                picker.setOnClickListener {
                    if(snapshot.child("Wait").value.toString().toInt()==0) {
                        reference.child("Wait").setValue(1)
                        picker.isEnabled=false
                        noofStudent = 0
                        ihead = snapshot.child("Class").child(className.toString()).child(gender)
                            .child("Head").value.toString().toInt()
                        while (ihead != 0) {
                            noofStudent++
                            ihead =
                                snapshot.child("Class").child(className.toString()).child(gender)
                                    .child(ihead.toString()).child("Next").value.toString().toInt()
                        }
                        noofStaff = 0
                        ihead = snapshot.child("Staff").child("Head").value.toString().toInt()
                        while (ihead != 0) {
                            noofStaff++
                            ihead = snapshot.child("Staff").child(ihead.toString())
                                .child("Next").value.toString().toInt()
                        }
                        val picked=snapshot.child("Class").child(className.toString()).child("Mentor").child("Picked").value.toString().toInt()
                        noofStaff-=(picked/2)
                        val staff = IntArray(noofStaff)
                        ihead = snapshot.child("Staff").child("Head").value.toString().toInt()
                        noofStaff = 0
                        while (ihead != 0) {
                            if(picked>0) {
                                if (gender == "MaleStudent") {
                                    for(i in 1..picked){
                                        if(ihead==snapshot.child("Class").child(className.toString()).child("Mentor").child("FemaleStudent").child(i.toString()).value.toString().toInt()){
                                            break
                                        }
                                        if(i==picked){
                                            staff[noofStaff] = ihead
                                            noofStaff++
                                        }
                                    }
                                } else {
                                    for(i in 1..picked){
                                        if(ihead==snapshot.child("Class").child(className.toString()).child("Mentor").child("MaleStudent").child(i.toString()).value.toString().toInt()){
                                            break
                                        }
                                        if(i==picked){
                                            staff[noofStaff] = ihead
                                            noofStaff++
                                        }
                                    }
                                }
                            }else{
                                staff[noofStaff] = ihead
                                noofStaff++
                            }
                            ihead = snapshot.child("Staff").child(ihead.toString())
                                .child("Next").value.toString().toInt()
                        }

                        val mentor = randomize(staff, noofStudent)
                        for(i in mentor.indices){
                            reference.child("Class").child(className.toString()).child("Mentor").child(gender).child((i+1).toString()).setValue(mentor[i])
                        }
                        reference.child("Class").child(className.toString()).child("Mentor").child(gender).child("Size").setValue(mentor.size)
                        reference.child("Class").child(className.toString()).child("Mentor").child("Picked").setValue(snapshot.child("Class").child(className.toString()).child("Mentor").child("Picked").value.toString().toInt()+mentor.size)
                        Toast.makeText(this@StaffAdvisor, "Picked", Toast.LENGTH_LONG).show()

                        reference.child("Wait").setValue(0)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun randomize(staff: IntArray, n: Int): IntArray {
        val mentor=IntArray(n)
        val r = Random()
        if(n/2 <= staff.size) {
            for (i in (n / 2) - 1 downTo 1) {
                val j = r.nextInt(staff.size)
                val temp: Int = staff[i]
                staff[i] = staff[j]
                staff[j] = temp
            }
            for (i in 0 until n/2) {
                mentor[i]=staff[i]
            }
            for (i in (n / 2) - 1 downTo 1) {
                val j = r.nextInt(i + 1)
                val temp: Int = staff[i]
                staff[i] = staff[j]
                staff[j] = temp
            }
            var j=n/2
            for (i in 0 until n/2) {
                mentor[j]=staff[i]
                j++
            }
            if(n%2==1){
                mentor[n-1]=mentor[r.nextInt(n-1)]
            }
        }
        return mentor
    }
}
