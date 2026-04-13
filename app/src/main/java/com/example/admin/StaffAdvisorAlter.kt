package com.example.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class  StaffAdvisorAlter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_advisor_alter)

        val add: FloatingActionButton = findViewById(R.id.add)
        val listView: ListView = findViewById(R.id.listview)
        val arrayList: ArrayList<ThreeStrings> = ArrayList()
        val share: SharedPreferences = getSharedPreferences("Detail", Context.MODE_PRIVATE);
        var shead: String = ""
        var ihead: Int = 0
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference

        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("InflateParams")
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                shead = snapshot.child("Class").child("Head").value.toString()
                while (shead != "NULL") {
                    arrayList.add(
                        ThreeStrings(
                            shead,
                            snapshot.child("Staff").child(
                                snapshot.child("Class").child(shead).child("Staff1").value.toString()
                            ).child("Name").value.toString(),
                            snapshot.child("Staff").child(
                                snapshot.child("Class").child(shead).child("Staff2").value.toString()
                            ).child("Name").value.toString()
                    ))
                    shead = snapshot.child("Class").child("" + shead).child("Next").value.toString()
                }
                val arrayAdapter=
                    StringAdapter(this@StaffAdvisorAlter, R.layout.custome_list1, arrayList)
                listView.adapter = arrayAdapter;

                add.setOnClickListener {
                    val alert = AlertDialog.Builder(this@StaffAdvisorAlter)
                    val view: View = layoutInflater.inflate(R.layout.custom_dialog_2, null)
                    val classname: EditText = view.findViewById(R.id.classname)
                    val staffid1: EditText = view.findViewById(R.id.staffid1)
                    val staffid2: EditText = view.findViewById(R.id.staffid2)
                    val staffname1: TextView = view.findViewById(R.id.staffname1)
                    val staffname2: TextView = view.findViewById(R.id.staffname2)
                    val password1: EditText = view.findViewById(R.id.password1)
                    val password2: EditText = view.findViewById(R.id.password2)
                    val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
                    val btn_add: Button = view.findViewById(R.id.btn_add)
                    var notpossible = 0
                    alert.setView(view)
                    val alertDialog = alert.create()
                    alertDialog.setCanceledOnTouchOutside(false)

                    val tw: TextWatcher = object : TextWatcher {
                        override fun beforeTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                        }

                        override fun onTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                            if (staffid1.text.toString() != "") {
                                ihead =
                                    snapshot.child("Staff").child("Head").value.toString().toInt()
                                if (ihead > 0) {
                                    while (ihead > 0) {
                                        if (ihead == staffid1.text.toString().toInt()) {
                                            break
                                        } else {
                                            ihead = snapshot.child("Staff").child("" + ihead)
                                                .child("Next").value.toString()
                                                .toInt()
                                        }
                                    }
                                    if (ihead != 0) {
                                        staffname1.text =
                                            snapshot.child("Staff").child(ihead.toString())
                                                .child("Name").value.toString()
                                    } else {
                                        staffname1.text = ""
                                    }
                                } else {
                                    staffname1.text = ""
                                }
                            } else {
                                staffname1.text = ""
                            }
                            if (staffid2.text.toString() != "") {
                                ihead =
                                    snapshot.child("Staff").child("Head").value.toString().toInt()
                                if (ihead > 0) {
                                    while (ihead > 0) {
                                        if (ihead == staffid2.text.toString().toInt()) {
                                            break
                                        } else {
                                            ihead = snapshot.child("Staff").child("" + ihead)
                                                .child("Next").value.toString()
                                                .toInt()
                                        }
                                    }
                                    if (ihead != 0) {
                                        staffname2.text =
                                            snapshot.child("Staff").child(ihead.toString())
                                                .child("Name").value.toString()
                                    } else {
                                        staffname2.text = ""
                                    }
                                } else {
                                    staffname2.text = ""
                                }
                            } else {
                                staffname2.text = ""
                            }
                        }

                        override fun afterTextChanged(editable: Editable) {}
                    }
                    staffid1.addTextChangedListener(tw)
                    staffid2.addTextChangedListener(tw)

                    btn_cancel.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    btn_add.setOnClickListener {
                        if (classname.text.toString() != "" && staffname1.text.toString() != "" && staffname2.text.toString() != "" && password1.text.toString() != "" && password2.text.toString() != "") {
                            if (staffid1.text.toString().toInt() != staffid2.text.toString()
                                    .toInt()
                            ) {
                                if (snapshot.child("Staff").child(staffid1.text.toString())
                                        .child("StaffAdvisor").value.toString().toInt() == 1
                                ) {
                                    notpossible = 1
                                    Toast.makeText(
                                        this@StaffAdvisorAlter,
                                        snapshot.child("Staff").child(ihead.toString())
                                            .child("Name").value.toString() + " already a STAFF ADVISOR",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (snapshot.child("Staff").child(staffid2.text.toString())
                                        .child("StaffAdvisor").value.toString().toInt() == 1
                                ) {
                                    notpossible = 1
                                    Toast.makeText(
                                        this@StaffAdvisorAlter,
                                        snapshot.child("Staff").child(ihead.toString())
                                            .child("Name").value.toString() + " already a STAFF ADVISOR",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (notpossible != 1) {
                                    shead = snapshot.child("Class").child("Head").value.toString()
                                    if (shead != "NULL") {
                                        while (shead != "NULL") {
                                            if (shead == classname.text.toString()) {
                                                shead = "NULL1"
                                                break
                                            } else {
                                                shead = snapshot.child("Class").child("" + shead)
                                                    .child("Next").value.toString()
                                            }
                                        }
                                        if (shead != "NULL1") {
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Front")
                                                .setValue(
                                                    snapshot.child("Class")
                                                        .child("Tail").value.toString()
                                                )
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Next")
                                                .setValue("NULL")
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Password1")
                                                .setValue(password1.text.toString())
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Password2")
                                                .setValue(password2.text.toString())
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Staff1")
                                                .setValue(staffid1.text.toString())
                                            reference.child("Class")
                                                .child(classname.text.toString()).child("Staff2")
                                                .setValue(staffid2.text.toString())
                                            reference.child("Class")
                                                .child(classname.text.toString())
                                                .child("FemaleStudent").child("Head").setValue(0)
                                            reference.child("Class")
                                                .child(classname.text.toString())
                                                .child("FemaleStudent").child("Tail").setValue(0)
                                            reference.child("Class")
                                                .child(classname.text.toString())
                                                .child("MaleStudent").child("Head").setValue(0)
                                            reference.child("Class")
                                                .child(classname.text.toString())
                                                .child("MaleStudent").child("Tail").setValue(0)
                                            reference.child("Class").child(classname.text.toString()).child("Mentor").child("Picked").setValue(0)
                                            reference.child("Class").child(classname.text.toString()).child("Mentor").child("FemaleStudent").child("Size").setValue(0)
                                            reference.child("Class").child(classname.text.toString()).child("Mentor").child("MaleStudent").child("Size").setValue(0)
                                            reference.child("Class").child(
                                                snapshot.child("Class")
                                                    .child("Tail").value.toString()
                                            ).child("Next").setValue(classname.text.toString())
                                            reference.child("Class").child("Tail")
                                                .setValue(classname.text.toString())
                                            reference.child("Staff").child(staffid1.text.toString()).child("StaffAdvisor").setValue(1)
                                            reference.child("Staff").child(staffid2.text.toString()).child("StaffAdvisor").setValue(1)
                                            alertDialog.dismiss()
                                            Toast.makeText(
                                                this@StaffAdvisorAlter,
                                                "Added Successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@StaffAdvisorAlter,
                                                "Class already available",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Front")
                                            .setValue("NULL")
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Next")
                                            .setValue("NULL")
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Password1")
                                            .setValue(password1.text.toString())
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Password2")
                                            .setValue(password2.text.toString())
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Staff1")
                                            .setValue(staffid1.text.toString())
                                        reference.child("Class")
                                            .child(classname.text.toString()).child("Staff2")
                                            .setValue(staffid2.text.toString())
                                        reference.child("Class")
                                            .child(classname.text.toString())
                                            .child("FemaleStudent").child("Head").setValue(0)
                                        reference.child("Class")
                                            .child(classname.text.toString())
                                            .child("FemaleStudent").child("Tail").setValue(0)
                                        reference.child("Class")
                                            .child(classname.text.toString())
                                            .child("MaleStudent").child("Head").setValue(0)
                                        reference.child("Class")
                                            .child(classname.text.toString())
                                            .child("MaleStudent").child("Tail").setValue(0)
                                        reference.child("Class").child(classname.text.toString()).child("Mentor").child("Picked").setValue(0)
                                        reference.child("Class").child(classname.text.toString()).child("Mentor").child("FemaleStudent").child("Size").setValue(0)
                                        reference.child("Class").child(classname.text.toString()).child("Mentor").child("MaleStudent").child("Size").setValue(0)
                                        reference.child("Staff").child(staffid1.text.toString()).child("StaffAdvisor").setValue(1)
                                        reference.child("Staff").child(staffid2.text.toString()).child("StaffAdvisor").setValue(1)
                                        reference.child("Class").child("Head").setValue(classname.text.toString())
                                        reference.child("Class").child("Tail").setValue(classname.text.toString())
                                        alertDialog.dismiss()
                                        Toast.makeText(
                                            this@StaffAdvisorAlter,
                                            "Added Successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@StaffAdvisorAlter,
                                    "Same Staff Advisor Or NOT Allowed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@StaffAdvisorAlter,
                                "Complete All Details",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    alertDialog.show()
                }

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        var count = 0
                        shead = snapshot.child("Class").child("Head").value.toString()
                        while (shead != "NULL") {
                            if (count == position) {
                                break
                            } else {
                                shead = snapshot.child("Class").child(shead)
                                    .child("Next").value.toString()
                                count++
                            }
                        }
                        val editor: SharedPreferences.Editor = share.edit()
                        editor.putString("ClassName",shead)
                        editor.apply()
                        startActivity(Intent(this@StaffAdvisorAlter, Student::class.java))
                    }

                listView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { parent, view, position, id -> //ArrayList arrayList=databaseHelper.getAllText();

                        val alert = AlertDialog.Builder(this@StaffAdvisorAlter)
                        val view1: View = layoutInflater.inflate(R.layout.custom_dialog_1, null)
                        val classname: TextView = view1.findViewById(R.id.classname)
                        val staffname1: TextView = view1.findViewById(R.id.staffname1)
                        val staffname2: TextView = view1.findViewById(R.id.staffname2)
                        val staffid1: EditText = view1.findViewById(R.id.staffid1)
                        val staffid2: EditText = view1.findViewById(R.id.staffid2)
                        val password1: EditText = view1.findViewById(R.id.password1)
                        val password2: EditText = view1.findViewById(R.id.password2)
                        val btn_cancel: Button = view1.findViewById(R.id.btn_cancel)
                        val btn_alter: Button = view1.findViewById(R.id.btn_alter)
                        alert.setView(view1)
                        val alertDialog = alert.create()
                        alertDialog.setCanceledOnTouchOutside(false)

                        val tw: TextWatcher = object : TextWatcher {
                            override fun beforeTextChanged(
                                charSequence: CharSequence,
                                i: Int,
                                i1: Int,
                                i2: Int
                            ) {
                            }

                            override fun onTextChanged(
                                charSequence: CharSequence,
                                i: Int,
                                i1: Int,
                                i2: Int
                            ) {
                                if (staffid1.text.toString() != "") {
                                    ihead =
                                        snapshot.child("Staff").child("Head").value.toString()
                                            .toInt()
                                    if (ihead > 0) {
                                        while (ihead > 0) {
                                            if (ihead == staffid1.text.toString().toInt()) {
                                                break
                                            } else {
                                                ihead = snapshot.child("Staff").child("" + ihead)
                                                    .child("Next").value.toString()
                                                    .toInt()
                                            }
                                        }
                                        if (ihead != 0) {
                                            staffname1.text =
                                                snapshot.child("Staff").child(ihead.toString())
                                                    .child("Name").value.toString()
                                        } else {
                                            staffname1.text = ""
                                        }
                                    } else {
                                        staffname1.text = ""
                                    }
                                } else {
                                    staffname1.text = ""
                                }
                                if (staffid2.text.toString() != "") {
                                    ihead =
                                        snapshot.child("Staff").child("Head").value.toString()
                                            .toInt()
                                    if (ihead > 0) {
                                        while (ihead > 0) {
                                            if (ihead == staffid2.text.toString().toInt()) {
                                                break
                                            } else {
                                                ihead = snapshot.child("Staff").child("" + ihead)
                                                    .child("Next").value.toString()
                                                    .toInt()
                                            }
                                        }
                                        if (ihead != 0) {
                                            staffname2.text =
                                                snapshot.child("Staff").child(ihead.toString())
                                                    .child("Name").value.toString()
                                        } else {
                                            staffname2.text = ""
                                        }
                                    } else {
                                        staffname2.text = ""
                                    }
                                } else {
                                    staffname2.text = ""
                                }
                            }

                            override fun afterTextChanged(editable: Editable) {}
                        }
                        staffid1.addTextChangedListener(tw)
                        staffid2.addTextChangedListener(tw)

                        var count = 0
                        shead = snapshot.child("Class").child("Head").value.toString()
                        while (shead != "NULL") {
                            if (count == position) {
                                break
                            } else {
                                shead = snapshot.child("Class").child(shead)
                                    .child("Next").value.toString()
                                count++
                            }
                        }
                        Toast.makeText(this@StaffAdvisorAlter, "" + count, Toast.LENGTH_LONG).show()
                        classname.text = shead
                        //staffname1.text=snapshot.child("Class").child(shead).child("Staff1").value.toString()
                        //staffname2.text=snapshot.child("Class").child(shead).child("Staff2").value.toString()
                        staffid1.setText(
                            snapshot.child("Class").child(shead).child("Staff1").value.toString()
                        )
                        staffid2.setText(
                            snapshot.child("Class").child(shead).child("Staff2").value.toString()
                        )
                        password1.setText(
                            snapshot.child("Class").child(shead).child("Password1").value.toString()
                        )
                        password2.setText(
                            snapshot.child("Class").child(shead).child("Password2").value.toString()
                        )

                        val oldStaffid1:Int =
                            snapshot.child("Class").child(shead).child("Staff1").value.toString().toInt()
                        val oldStaffid2:Int =
                            snapshot.child("Class").child(shead).child("Staff2").value.toString().toInt()

                        btn_cancel.setOnClickListener {
                            alertDialog.dismiss()
                        }
                        btn_alter.setOnClickListener {
                            if (staffname1.text.toString() != "" && staffname2.text.toString() != "" && password1.text.toString() != "" && password2.text.toString() != "") {
                                if (staffid1.text.toString()
                                        .toInt() != staffid2.text.toString()
                                        .toInt()
                                ) {
                                    reference.child("Staff").child(oldStaffid1.toString()).child("StaffAdvisor").setValue(0)
                                    reference.child("Staff").child(oldStaffid2.toString()).child("StaffAdvisor").setValue(0)
                                    reference.child("Class").child(shead).child("Password1")
                                        .setValue(password1.text.toString())
                                    reference.child("Class").child(shead).child("Password2")
                                        .setValue(password2.text.toString())
                                    reference.child("Class").child(shead).child("Staff1")
                                        .setValue(staffid1.text.toString())
                                    reference.child("Class").child(shead).child("Staff2")
                                        .setValue(staffid2.text.toString())
                                    reference.child("Staff").child(staffid1.text.toString()).child("StaffAdvisor").setValue(1)
                                    reference.child("Staff").child(staffid2.text.toString()).child("StaffAdvisor").setValue(1)
                                    alertDialog.dismiss()
                                    Toast.makeText(
                                        this@StaffAdvisorAlter,
                                        "Altered Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@StaffAdvisorAlter,
                                        "Same Staff Advisor are NOT Allowed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@StaffAdvisorAlter,
                                    "Complete All Details",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        alertDialog.show()
                        return@OnItemLongClickListener false
                    }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}