package intro.android.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class Nota : AppCompatActivity() {
    private lateinit var titleView : EditText
    private lateinit var contentView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota)

        titleView = findViewById(R.id.titleInput)
        contentView = findViewById(R.id.contentInput)



        var button = findViewById<Button>(R.id.confirm_button)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(contentView.text) && TextUtils.isEmpty(titleView.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                var values = arrayOf<String>(titleView.text.toString(), contentView.text.toString())
                replyIntent.putExtra(EXTRA_REPLY, values)
                setResult(Activity.RESULT_OK, replyIntent)
            }
        }
        finish()


    }

    companion object{
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }


}