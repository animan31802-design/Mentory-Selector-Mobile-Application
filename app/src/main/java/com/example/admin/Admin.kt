package com.example.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val faculty:Button=findViewById(R.id.faculty)
        val staffAdvisor:Button=findViewById(R.id.staffadvisor)
        faculty.setOnClickListener{
            startActivity(Intent(this,FacultyAlter::class.java))
        }
        staffAdvisor.setOnClickListener{
            startActivity(Intent(this,StaffAdvisorAlter::class.java))
        }
    }
}