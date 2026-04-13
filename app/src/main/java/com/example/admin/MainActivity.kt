package com.example.admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError

import androidx.annotation.NonNull

import com.google.firebase.database.DataSnapshot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user:EditText=findViewById(R.id.usernumber)
        val password:EditText=findViewById(R.id.password)
        val login:Button=findViewById(R.id.login)
        val reference:DatabaseReference = FirebaseDatabase.getInstance().reference
        val share: SharedPreferences =getSharedPreferences("Detail", Context.MODE_PRIVATE)


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                login.setOnClickListener{
                    if(user.text.toString()!="" && password.text.toString()!="") {
                        val adminNumber = snapshot.child("Admin").child("UserID").value.toString().toInt()
                        val adminPassword = snapshot.child("Admin").child("Password").value.toString()
                        if (user.text.toString() == adminNumber.toString() && password.text.toString() == adminPassword) {
                            Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_LONG)
                                .show()
                            startActivity(Intent(this@MainActivity, Admin::class.java))
                            finish()
                        } else {
                            var shead = snapshot.child("Class").child("Head").value.toString()
                            if (shead != "NULL") {
                                while (shead != "NULL") {
                                    if ((user.text.toString() == snapshot.child("Class")
                                            .child(shead)
                                            .child("Staff1").value.toString() && password.text.toString() == snapshot.child(
                                            "Class"
                                        ).child(shead)
                                            .child("Password1").value.toString()) || (user.text.toString() == snapshot.child(
                                            "Class"
                                        ).child(shead)
                                            .child("Staff2").value.toString() && password.text.toString() == snapshot.child(
                                            "Class"
                                        ).child(shead).child("Password2").value.toString())
                                    ) {
                                        val editor: SharedPreferences.Editor = share.edit()
                                        editor.putString("ClassName", shead).apply()
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Login Success",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                StaffAdvisor::class.java
                                            )
                                        )
                                        finish()
                                        break
                                    } else {
                                        shead = snapshot.child("Class").child(shead)
                                            .child("Next").value.toString()
                                    }
                                }
                                if(shead=="NULL"){
                                    Toast.makeText(this@MainActivity,"Enter correct User ID and PASSWORD",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Enter correct detail to enter",Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}