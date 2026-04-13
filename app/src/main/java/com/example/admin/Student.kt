package com.example.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import android.widget.RadioGroup

class Student : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        val add: FloatingActionButton =findViewById(R.id.add)
        val listView: ListView = findViewById(R.id.list_view)
        //val arrayList0: ArrayList<String> = ArrayList()
        val arrayList: ArrayList<ThreeStrings> = ArrayList()
        val share: SharedPreferences =getSharedPreferences("Detail", Context.MODE_PRIVATE);

        val className=share.getString("ClassName","")
        var ihead: Int = 0

        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("InflateParams")
            override fun onDataChange(snapshot: DataSnapshot) {
                //arrayList0.clear()
                arrayList.clear()
                ihead = snapshot.child("Class").child(className.toString()).child("FemaleStudent").child("Head").value.toString().toInt()
                while (ihead != 0) {
                    val threeStrings=ThreeStrings(snapshot.child("Student").child(ihead.toString()).child("RollNumber").value.toString(),
                        snapshot.child("Student").child(ihead.toString()).child("Name").value.toString(),
                        snapshot.child("Student").child(ihead.toString()).child("CGPA").value.toString()
                    )
                    arrayList.add(threeStrings)
                        /*arrayList0.add(
                        snapshot.child("Student").child(ihead.toString()).child("RollNumber").value.toString()+"\n"+
                        snapshot.child("Student").child(ihead.toString()).child("Name").value.toString()+"\n"+
                        snapshot.child("Student").child(ihead.toString()).child("CGPA").value.toString()
                    )*/
                    ihead = snapshot.child("Class").child(className.toString()).child("FemaleStudent").child(ihead.toString()).child("Next").value.toString().toInt()
                }
                ihead = snapshot.child("Class").child(className.toString()).child("MaleStudent").child("Head").value.toString().toInt()
                while (ihead != 0) {
                    val threeStings=ThreeStrings(snapshot.child("Student").child(ihead.toString()).child("RollNumber").value.toString(),
                        snapshot.child("Student").child(ihead.toString()).child("Name").value.toString(),
                        snapshot.child("Student").child(ihead.toString()).child("CGPA").value.toString()
                    )
                    arrayList.add(threeStings)
                    /*arrayList0.add(
                        snapshot.child("Student").child(ihead.toString()).child("RollNumber").value.toString()+"\n"+
                                snapshot.child("Student").child(ihead.toString()).child("Name").value.toString()+"\n"+
                                snapshot.child("Student").child(ihead.toString()).child("CGPA").value.toString()
                    )*/
                    ihead = snapshot.child("Class").child(className.toString()).child("MaleStudent").child(ihead.toString()).child("Next").value.toString().toInt()
                }

                val arrayAdapter = StringAdapter(this@Student,R.layout.custom_list,arrayList)
                listView.adapter = arrayAdapter

                add.setOnClickListener{
                    val alert = AlertDialog.Builder(this@Student)
                    val view: View = layoutInflater.inflate(R.layout.custom_dialog_3,null)
                    val  admission_number: EditText =view.findViewById(R.id.admission_number)
                    val  name: EditText =view.findViewById(R.id.name)
                    val  rollno: EditText =view.findViewById(R.id.rollno)
                    val  cgpa: EditText =view.findViewById(R.id.cgpa)
                    val btn_cancel: Button =view.findViewById(R.id.btn_cancel)
                    val btn_add: Button =view.findViewById(R.id.btn_add)
                    val male:RadioButton=view.findViewById(R.id.male)
                    val female:RadioButton=view.findViewById(R.id.female)
                    val gender:RadioGroup=view.findViewById(R.id.gender)
                    var gendernumber:Int=0
                    alert.setView(view)
                    val alertDialog = alert.create()
                    alertDialog.setCanceledOnTouchOutside(false)

                    gender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                        gendernumber = checkedId - male.id +1
                        if(gendernumber!=1){
                            gendernumber=2
                        }
                    })
                    btn_cancel.setOnClickListener{
                        alertDialog.dismiss()
                    }
                    btn_add.setOnClickListener {
                        if (admission_number.text.toString()!="" && name.text.toString()!="" && rollno.text.toString()!="" && cgpa.text.toString()!="" && gendernumber!=0) {
                            ihead=snapshot.child("Student").child("Head").value.toString().toInt()
                            if(ihead>0) {
                                while (ihead > 0) {
                                    if (ihead == admission_number.text.toString().toInt()) {
                                        ihead = -1
                                        Toast.makeText(
                                            this@Student,
                                            "Admission Number already available",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        break
                                    } else {
                                        ihead =
                                            snapshot.child("Student").child("" + ihead).child("Next").value.toString()
                                                .toInt()
                                    }
                                }
                                if (ihead != -1) {
                                    reference.child("Student").child(admission_number.text.toString()).child("Name").setValue(name.text.toString())
                                    reference.child("Student").child(admission_number.text.toString()).child("RollNumber").setValue(rollno.text.toString())
                                    reference.child("Student").child(admission_number.text.toString()).child("CGPA").setValue(cgpa.text.toString())
                                    reference.child("Student").child(admission_number.text.toString()).child("ClassName").setValue(className)
                                    reference.child("Student").child(admission_number.text.toString()).child("Front").setValue(snapshot.child("Student").child("Tail").value.toString().toInt())
                                    reference.child("Student").child(admission_number.text.toString()).child("Next").setValue(0)
                                    reference.child("Student").child(snapshot.child("Student").child("Tail").value.toString()).child("Next").setValue(admission_number.text.toString().toInt())
                                    reference.child("Student").child("Tail").setValue(admission_number.text.toString().toInt())


                                    if (gendernumber == 1) {
                                        reference.child("Student").child(admission_number.text.toString()).child("Gender").setValue("Male")
                                        reference.child("Class").child(className.toString()).child("MaleStudent").child(admission_number.text.toString()).child("Front").setValue(snapshot.child("Class").child(className.toString()).child("MaleStudent").child("Tail").value.toString().toInt())
                                        reference.child("Class").child(className.toString()).child("MaleStudent").child(admission_number.text.toString()).child("Next").setValue(0)
                                        if(snapshot.child("Class").child(className.toString()).child("MaleStudent").child("Head").value.toString().toInt()==0){
                                            reference.child("Class").child(className.toString()).child("MaleStudent").child("Head").setValue(admission_number.text.toString().toInt())
                                            reference.child("Class").child(className.toString()).child("MaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                        }else{
                                            reference.child("Class").child(className.toString()).child("MaleStudent").child(snapshot.child("Class").child(className.toString()).child("MaleStudent").child("Tail").value.toString()).child("Next").setValue(admission_number.text.toString().toInt())
                                        }
                                        reference.child("Class").child(className.toString()).child("MaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                    }else{
                                        reference.child("Student").child(admission_number.text.toString()).child("Gender").setValue("Female")
                                        reference.child("Class").child(className.toString()).child("FemaleStudent").child(admission_number.text.toString()).child("Front").setValue(snapshot.child("Class").child(className.toString()).child("FemaleStudent").child("Tail").value.toString().toInt())
                                        reference.child("Class").child(className.toString()).child("FemaleStudent").child(admission_number.text.toString()).child("Next").setValue(0)
                                        if(snapshot.child("Class").child(className.toString()).child("FemaleStudent").child("Head").value.toString().toInt()==0){
                                            reference.child("Class").child(className.toString()).child("FemaleStudent").child("Head").setValue(admission_number.text.toString().toInt())
                                            reference.child("Class").child(className.toString()).child("FemaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                        }else{
                                            reference.child("Class").child(className.toString()).child("FemaleStudent").child(snapshot.child("Class").child(className.toString()).child("FemaleStudent").child("Tail").value.toString()).child("Next").setValue(admission_number.text.toString().toInt())
                                        }
                                        reference.child("Class").child(className.toString()).child("FemaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                    }
                                    alertDialog.dismiss()
                                    Toast.makeText(this@Student,"Added Successfully", Toast.LENGTH_LONG).show()

                                }else{
                                    Toast.makeText(this@Student,"Admission Number already available",Toast.LENGTH_LONG).show()
                                }
                            }else{
                                reference.child("Student").child(admission_number.text.toString()).child("Name").setValue(name.text.toString())
                                reference.child("Student").child(admission_number.text.toString()).child("RollNumber").setValue(rollno.text.toString())
                                reference.child("Student").child(admission_number.text.toString()).child("CGPA").setValue(cgpa.text.toString())
                                reference.child("Student").child(admission_number.text.toString()).child("ClassName").setValue(className)
                                reference.child("Student").child(admission_number.text.toString()).child("Front").setValue(0)
                                reference.child("Student").child(admission_number.text.toString()).child("Next").setValue(0)
                                reference.child("Student").child("Head").setValue(admission_number.text.toString().toInt())
                                reference.child("Student").child("Tail").setValue(admission_number.text.toString().toInt())

                                if (gendernumber == 1) {
                                    reference.child("Student").child(admission_number.text.toString()).child("Gender").setValue("Male")
                                    reference.child("Class").child(className.toString()).child("MaleStudent").child(admission_number.text.toString()).child("Front").setValue(0)
                                    reference.child("Class").child(className.toString()).child("MaleStudent").child(admission_number.text.toString()).child("Next").setValue(0)
                                    reference.child("Class").child(className.toString()).child("MaleStudent").child("Head").setValue(admission_number.text.toString().toInt())
                                    reference.child("Class").child(className.toString()).child("MaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                }else{
                                    reference.child("Student").child(admission_number.text.toString()).child("Gender").setValue("Female")
                                    reference.child("Class").child(className.toString()).child("FemaleStudent").child(admission_number.text.toString()).child("Front").setValue(0)
                                    reference.child("Class").child(className.toString()).child("FemaleStudent").child(admission_number.text.toString()).child("Next").setValue(0)
                                    reference.child("Class").child(className.toString()).child("FemaleStudent").child("Head").setValue(admission_number.text.toString().toInt())
                                    reference.child("Class").child(className.toString()).child("FemaleStudent").child("Tail").setValue(admission_number.text.toString().toInt())
                                }
                                alertDialog.dismiss()
                                Toast.makeText(this@Student,"Added Successfully", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this@Student,"Complete All Details", Toast.LENGTH_LONG).show()
                        }
                    }
                    alertDialog.show()
                }
                listView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { parent, view, position, id -> //ArrayList arrayList=databaseHelper.getAllText();

                        val alert = AlertDialog.Builder(this@Student)
                        val myview: View = layoutInflater.inflate(R.layout.custom_dialog_4,null)
                        val  admission_number: TextView =myview.findViewById(R.id.admission_number)
                        val  name: TextView =myview.findViewById(R.id.name)
                        val  rollno: TextView =myview.findViewById(R.id.rollno)
                        val  cgpa: EditText =myview.findViewById(R.id.cgpa)
                        var gendernumber:Int=0
                        var gender=""
                        val btn_cancel: Button =myview.findViewById(R.id.btn_cancel)
                        val btn_remove: Button =myview.findViewById(R.id.btn_remove)
                        val btn_alter: Button =myview.findViewById(R.id.btn_alter)

                        var count=0
                        var noofgirl=0
                        ihead=snapshot.child("Class").child(className.toString()).child("FemaleStudent").child("Head").value.toString().toInt()
                        while (ihead>0){
                            if(count==position){
                                count++
                                break
                            }else{
                                count++
                                ihead=snapshot.child("Class").child(className.toString()).child("FemaleStudent").child(ihead.toString()).child("Next").value.toString().toInt()
                            }
                        }
                        noofgirl=count
                        if(ihead==0){
                            ihead=snapshot.child("Class").child(className.toString()).child("MaleStudent").child("Head").value.toString().toInt()
                            while (ihead>0){
                                if(count==position){
                                    count++
                                    break
                                }else{
                                    count++
                                    ihead=snapshot.child("Class").child(className.toString()).child("MaleStudent").child(ihead.toString()).child("Next").value.toString().toInt()
                                }
                            }
                        }
                        if(ihead>0){
                            admission_number.text=ihead.toString()
                            name.text=snapshot.child("Student").child(ihead.toString()).child("Name").value.toString()
                            rollno.text=snapshot.child("Student").child(ihead.toString()).child("RollNumber").value.toString()
                            cgpa.setText(snapshot.child("Student").child(ihead.toString()).child("CGPA").value.toString())
                        }

                        alert.setView(myview)
                        val alertDialog = alert.create()
                        alertDialog.setCanceledOnTouchOutside(false)
                        btn_cancel.setOnClickListener{
                            alertDialog.dismiss()
                        }
                        btn_alter.setOnClickListener {
                            if(cgpa.text.toString()!="") {
                                reference.child("Student").child(admission_number.text.toString()).child("CGPA")
                                    .setValue(cgpa.text.toString().toInt())
                                alertDialog.dismiss()
                                Toast.makeText(this@Student,"Altered Successfully", Toast.LENGTH_LONG).show()
                            }
                        }
                        btn_remove.setOnClickListener {
                            var ccount=0
                            if(count-noofgirl==0){
                                gender="FemaleStudent"
                                ccount = if(count==1 && noofgirl==1){
                                    0
                                }else{
                                    -1
                                }
                            }else{
                                gender="MaleStudent"
                                ccount=count-noofgirl-1
                            }
                            if(ihead.toString().toInt()>0){
                                when {
                                    ccount==0 -> {
                                        reference.child("Class").child(className.toString()).child(gender).child(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Next").value.toString()).child("Front").setValue(0)
                                        reference.child("Class").child(className.toString()).child(gender).child("Head").setValue(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Next").value.toString().toInt())
                                    }
                                    snapshot.child("Class").child(className.toString()).child(gender).child("Tail").value.toString().toInt()==ihead -> {
                                        reference.child("Class").child(className.toString()).child(gender).child(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Front").value.toString()).child("Next").setValue(0)
                                        reference.child("Class").child(className.toString()).child(gender).child("Tail").setValue(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Front").value.toString().toInt())
                                    }
                                    else -> {
                                        reference.child("Class").child(className.toString()).child(gender).child(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Front").value.toString()).child("Next").setValue(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Next").value.toString().toInt())
                                        reference.child("Class").child(className.toString()).child(gender).child(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Next").value.toString()).child("Front").setValue(snapshot.child("Class").child(className.toString()).child(gender).child(ihead.toString()).child("Front").value.toString().toInt())
                                    }
                                }

                                when {
                                    count==0 -> {
                                        reference.child("Student").child(snapshot.child("Student").child(ihead.toString()).child("Next").value.toString()).child("Front").setValue(0)
                                        reference.child("Student").child("Head").setValue(snapshot.child("Student").child(ihead.toString()).child("Next").value.toString().toInt())
                                    }
                                    snapshot.child("Student").child("Tail").value.toString().toInt()==ihead -> {
                                        reference.child("Student").child(snapshot.child("Student").child(ihead.toString()).child("Front").value.toString()).child("Next").setValue(0)
                                        reference.child("Student").child("Tail").setValue(snapshot.child("Student").child(ihead.toString()).child("Front").value.toString().toInt())
                                    }
                                    else -> {
                                        reference.child("Student").child(
                                            snapshot.child("Student").child(ihead.toString())
                                                .child("Front").value.toString()
                                        ).child("Next").setValue(
                                            snapshot.child("Student").child(ihead.toString())
                                                .child("Next").value.toString().toInt()
                                        )
                                        reference.child("Student").child(
                                            snapshot.child("Student").child(ihead.toString())
                                                .child("Next").value.toString()
                                        ).child("Front").setValue(
                                            snapshot.child("Student").child(ihead.toString())
                                                .child("Front").value.toString().toInt()
                                        )
                                    }
                                }
                            }else{
                                Toast.makeText(this@Student,"Student Not Removed",Toast.LENGTH_LONG).show()
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