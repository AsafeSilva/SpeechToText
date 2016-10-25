package guia.robo.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    TextView txtSpeech;
    Button btnToSpeak, btnToListen;
    TextToSpeech tts;

    private final int REQ_CODE_SPEECH_INPUT = 1;
    private final int CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeech = (TextView)findViewById(R.id.txtSpeech);
        btnToSpeak = (Button)findViewById(R.id.btnToSpeak);
        btnToListen = (Button)findViewById(R.id.btnToListen);

        btnToSpeak.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SpeakActivity.class);
                startActivityForResult(intent, CODE);
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        btnToListen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = txtSpeech.getText().toString();
                if(text.isEmpty())
                    Toast.makeText(getApplicationContext(), "Texto vazio", Toast.LENGTH_SHORT).show();
                else
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeech.setText(result.get(0));
                }
                break;
            case CODE:
                if(resultCode == RESULT_OK && data != null)
                    // Toast.makeText(MainActivity.this, data.getStringExtra("TEXT"), Toast.LENGTH_SHORT).show();
                    txtSpeech.setText(data.getStringExtra("TEXT"));
                break;
        }
    }

}
