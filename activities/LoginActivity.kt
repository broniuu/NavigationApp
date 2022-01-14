package com.example.navigationdrawer.activities

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawer.R

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val connection = isNetworkConnected()

        disableLoginButton(connection)
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

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    }

    private fun disableLoginButton(connection: Boolean) {
        if (!connection) {
            val button = findViewById<View>(R.id.buttonSetLogin)
            button.isEnabled = false
            findViewById<TextView>(R.id.textViewNoConnection).visibility = View.VISIBLE
        }
    }
}