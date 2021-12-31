package com.example.navigationdrawer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawer.R

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun saveLogin(view: View){
        val loginTextView = findViewById<TextView>(R.id.editTextLogin)
        val login = loginTextView.text.toString()
        saveData(login)

        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun saveData(text: String){
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("MY_LOGIN_KEY", text)
        editor.apply()
    }
}