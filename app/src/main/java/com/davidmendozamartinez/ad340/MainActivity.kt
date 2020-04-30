package com.davidmendozamartinez.ad340

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipCodeEditText: EditText = findViewById(R.id.zipCodeEditText)
        val enterButton: Button = findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val zipCode: String = zipCodeEditText.text.toString()

            if (zipCode.length != 5) {
                Toast.makeText(this, R.string.zip_code_entry_error, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, zipCode, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
