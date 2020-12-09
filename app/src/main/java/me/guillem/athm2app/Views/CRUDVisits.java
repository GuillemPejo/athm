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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import me.guillem.athm2app.Model.Visita;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;
import me.guillem.athm2app.image_viewer.ChooserType;
import me.guillem.athm2app.image_viewer.ImageViewAdapter;
import me.guillem.athm2app.image_viewer.MediaFile;
import me.guillem.athm2app.image_viewer.MediaSource;


import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.DatePickerFragment;
import me.guillem.athm2app.Utils.TimePickerFragment;
import me.guillem.athm2app.image_viewer.EasyImage;
import pl.aprilapps.easyphotopicker.DefaultCallback;

/**
 * * Created by Guillem on 02/12/20.
 */
public class CRUDVisits extends AppCompatActivity implements View.OnClickListener {

    private static final String PHOTOS_KEY = "LIST_IMAGE";
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CAMERA_VIDEO_REQUEST_CODE = 7501;
    private static final int GALLERY_REQUEST_CODE = 7502;
    private static final int DOCUMENTS_REQUEST_CODE = 7503;

    private FirebaseCRUD crudHelper = new FirebaseCRUD();
    private DatabaseReference db = Utils.getDatabaseRefence();

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private ImageButton micButton;
    private Animation mic_in, mic_out, bilink;
    ImageView icon_recording;
    Button gogallery, takephoto, save;
    EditText timer;
    TextInputEditText pickdata, picktime, nom_visita, descripcion;
    AutoCompleteTextView nom_respo;

    private Handler handler;
    private EasyImage easyImage;

    protected RecyclerView recyclerView;

    protected View galleryButton;

    private ImageViewAdapter imagesAdapter;

    private ArrayList<MediaFile> photos = new ArrayList<>();


    private int audioTotalTime;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter;

    private ProgressBar mprogressBar;
    String ids;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("Obra");
        ids = extras.getString("ids");

        descripcion = findViewById(R.id.descripcion);
        nom_respo = findViewById(R.id.nom_respo);
        nom_visita = findViewById(R.id.nom_visita);
        micButton = findViewById(R.id.mic_button);
        timer = findViewById(R.id.anim_mic);
        icon_recording = findViewById(R.id.icon_recording);

        mprogressBar = findViewById(R.id.mProgressBarSave);

        pickdata = findViewById(R.id.pickdata);
        pickdata.setOnClickListener(this);
        picktime = findViewById(R.id.picktime);
        picktime.setOnClickListener(this);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        pickdata.setText(currentDate);
        picktime.setText(currentTime);
        nom_respo.setText("Ramon");

        gogallery = findViewById(R.id.gogallery);
        gogallery.setOnClickListener(this);
        takephoto = findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);

        save = findViewById(R.id.save);
        save.setOnClickListener(this);

        recyclerView = findViewById(R.id.rv_images);

        voicerecognizr();

        populateUserDrownMenu();


        if (savedInstanceState != null) {
            photos = savedInstanceState.getParcelableArrayList(PHOTOS_KEY);
        }

        imagesAdapter = new ImageViewAdapter(this, photos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(imagesAdapter);

        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("EasyImage sample")
                .allowMultiple(true)
                .build();


        if (extras != null) {
            TextView t1 = findViewById(R.id.titol_obra);
            t1.setText("Nova visita a l'obra " + title);
        }

    }

    private void populateUserDrownMenu() {
        String[] USERS = new String[]{"Ramon", "Guillem", "Jose", "Admin"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, USERS);
        nom_respo.setAdapter(adaptador);
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
                descripcion.setHint("Espera un moment...");
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

    private void insertData() {
        String data_visita, hora_visita, responsable, nom_visit, descripcio;
        ArrayList<String> media = new ArrayList<String>();

        if (
                //Utils.validar(nomtxt, descripciotxt, categoriatxt, msgbeacontxt, bIdtxt)
        true) {
            nom_visit = nom_visita.getText().toString();
            responsable = nom_respo.getText().toString();
            data_visita = pickdata.getText().toString();
            hora_visita = picktime.getText().toString();
            descripcio = descripcion.getText().toString();
            for(int i=0; i<photos.size();i++) {
                media.add(photos.get(i).component2().toString());
            }

            Visita newVisit = new Visita(data_visita,hora_visita,responsable, nom_visit, descripcio,media);

            crudHelper.insertVisit(this,db,mprogressBar,newVisit, ids);

        }
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
            case R.id.takephoto:
                takephoto();
                break;
            case R.id.gogallery:
                gogallery();
                break;
            case R.id.save:
                insertData();
                break;
        }
    }

    private void takephoto() {
        String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
        if (arePermissionsGranted(necessaryPermissions)) {
            Log.e("EEERROR","Entra");
            easyImage.openCameraForImage(CRUDVisits.this);
            //System.out.println(photos.get(0).component2().toString());

        } else {
            Log.e("EEERROR","BAD ENTRY");
            requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE);
        }
    }

    private void gogallery() {
        String[] necessaryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (arePermissionsGranted(necessaryPermissions)) {
            easyImage.openGallery(CRUDVisits.this);
        } else {
            requestPermissionsCompat(necessaryPermissions, GALLERY_REQUEST_CODE);
        }
    }

    private void onPhotosReturned(@NonNull MediaFile[] returnedPhotos) {
        photos.addAll(Arrays.asList(returnedPhotos));
        imagesAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(photos.size() - 1);
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTOS_KEY, photos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(CRUDVisits.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(CRUDVisits.this);
        } else if (requestCode == CAMERA_VIDEO_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForVideo(CRUDVisits.this);
        } else if (requestCode == GALLERY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openGallery(CRUDVisits.this);
        } else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(CRUDVisits.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
                }
                onPhotosReturned(imageFiles);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }


    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(CRUDVisits.this, permissions, requestCode);
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
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
                final String selectedTime = hourOfDay + ":" + min;
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

}
