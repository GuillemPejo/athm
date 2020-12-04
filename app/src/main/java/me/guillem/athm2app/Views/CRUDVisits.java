package me.guillem.athm2app.Views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.DatePickerFragment;
import me.guillem.athm2app.Utils.TimePickerFragment;

/**
 * * Created by Guillem on 02/12/20.
 */
public class CRUDVisits extends AppCompatActivity implements View.OnClickListener {

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private ImageButton micButton;
    private Animation mic_in, mic_out, bilink;
    ImageView icon_recording;
    EditText pickdata, picktime, descripcion, timer;

    private Handler handler;

    private int audioTotalTime;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        descripcion = findViewById(R.id.descripcion);
        micButton = findViewById(R.id.mic_button);
        timer = findViewById(R.id.anim_mic);
        icon_recording = findViewById(R.id.icon_recording);



        voicerecognizr();



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title= extras.getString("Obra");
            TextView t1 = findViewById(R.id.titol_obra);
            t1.setText("Nova visita a l'obra "+title);


            String data_visita;
            String hora_visita;
            String responsable;
            String nom_visit;
            String descripcio;
            String key;
        }


        pickdata = findViewById(R.id.pickdata);
        pickdata.setOnClickListener(this);
        picktime = findViewById(R.id.picktime);
        picktime.setOnClickListener(this);

    }

    private void voicerecognizr() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mic_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mic_to_click_button);
        mic_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_close_button);
        bilink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bilink);
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());




        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        handler = new Handler(Looper.getMainLooper());


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            String text = String.valueOf(descripcion.getText());

            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                text = String.valueOf(descripcion.getText());
                descripcion.setText("");
                descripcion.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Toast.makeText(CRUDVisits.this, "Sisusplau, mantingui premut el botó", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                descripcion.setText(text +" "+ data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                    timer.setVisibility(View.GONE);
                    icon_recording.setVisibility(View.GONE);
                    descripcion.setVisibility(View.VISIBLE);
                    micButton.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                    icon_recording.clearAnimation();
                    timerTask.cancel();

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                    icon_recording.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    descripcion.setVisibility(View.INVISIBLE);
                    micButton.animate().scaleXBy(1f).scaleYBy(1f).translationX(-50).translationY(-20).setDuration(200).setInterpolator(new LinearInterpolator()).start();
                    icon_recording.startAnimation(bilink);

                    if (audioTimer == null) {
                        audioTimer = new Timer();
                        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    }

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    timer.setText(timeFormatter.format(new Date(audioTotalTime * 1000)));
                                    audioTotalTime++;
                                }
                            });
                        }
                    };

                    audioTotalTime = 0;
                    audioTimer.schedule(timerTask, 0, 1000);

                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickdata:
                showDatePickerDialog();
                break;
            case R.id.picktime:
                showTimePickerDialog();
                break;
        }
    }



    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                pickdata.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String min = String.valueOf(minute);
                if(min.length()==1){
                    min = "0" + min;
                }
                final String selectedTime = hourOfDay + " : " + min;
                picktime.setText(selectedTime);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

}
