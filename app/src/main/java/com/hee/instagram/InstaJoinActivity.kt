package com.hee.instagram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaJoinActivity : AppCompatActivity() {

    var username: String = ""
    var password1: String = ""
    var password2: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_join)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        findViewById<TextView>(R.id.login_btn).setOnClickListener{
            startActivity(Intent(this, InstaLoginActivity::class.java))
        }

        findViewById<EditText>(R.id.id_input).doAfterTextChanged {
            username = it.toString()
        }

        findViewById<EditText>(R.id.pw_input1).doAfterTextChanged {
            password1 = it.toString()
        }

        findViewById<EditText>(R.id.pw_input2).doAfterTextChanged {
            password2 = it.toString()
        }

        findViewById<TextView>(R.id.insta_join).setOnClickListener{
            val user = HashMap<String, Any>()
            user.put("username", username)
            user.put("password1", password1)
            user.put("password2", password2)
            retrofitService.instaJoin(user).enqueue(object:Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        val user = response.body()!!
                        val sharedPreferences =
                            getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("token", user.token)
                        editor.putString("user_id", user.id.toString())
                        editor.commit()

                        Toast.makeText(this@InstaJoinActivity, "가입 성공", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                }
            })
        }
    }
}