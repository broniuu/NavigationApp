package com.example.navigationdrawer.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationdrawer.ExamplePost
import com.example.navigationdrawer.JsonPlaceholderAPI
import com.example.navigationdrawer.Post
import com.example.navigationdrawer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ItemActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://tgryl.pl/shoutbox/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val login = intent.getStringExtra("KEY_LOGIN").toString()
        val date = intent.getStringExtra("KEY_DATE").toString()
        val content = intent.getStringExtra("KEY_CONTENT").toString()
        val id = intent.getStringExtra("KEY_ID").toString()

        val contentEditText = findViewById<EditText>(R.id.contentView2)
        contentEditText.setText(content)
        val dateView = findViewById<TextView>(R.id.dateView2)
        dateView.text = date
        val loginView = findViewById<TextView>(R.id.loginView2)
        loginView.text = login

        contentEditText.setOnClickListener {
            myEnter(contentEditText, id, login)
        }
    }

    fun myEnter(editText: EditText, id:String, login:String) {
        editText.setOnKeyListener(View.OnKeyListener() {v, keyCode, event ->
            if ( keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val contentEditText = findViewById<EditText>(R.id.contentView2)
                val content = contentEditText.text.toString()
                updatePost(id, login, content)
                return@OnKeyListener true
            }
            false
        })
    }
    private fun startMainActivity(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun updatePost(id:String, login:String, content:String){

        val putPost = Post(content, login)
        val call = jsonPlaceholderAPI.putPost(id, putPost)
        call.enqueue(object : Callback<ExamplePost>{
            override fun onResponse(call: Call<ExamplePost>, response: Response<ExamplePost>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@ItemActivity,
                        "Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                startMainActivity()
            }

            override fun onFailure(call: Call<ExamplePost>, t: Throwable) {
                Toast.makeText(this@ItemActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun deletePost(view: View){
        val id = intent.getStringExtra("KEY_ID").toString()
        val call = jsonPlaceholderAPI.deletePost(id)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@ItemActivity,
                        "Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                startMainActivity()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ItemActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



