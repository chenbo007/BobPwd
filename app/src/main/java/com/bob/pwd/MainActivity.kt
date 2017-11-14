package com.bob.pwd

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bob.pwd.views.PwdEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var pwdEdit=findViewById<PwdEditText>(R.id.input_pwd);
        pwdEdit.textStr;


    }
}
