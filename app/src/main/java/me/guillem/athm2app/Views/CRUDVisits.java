package me.guillem.athm2app.Views;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import me.guillem.athm2app.Model.AdapterImages;
import me.guillem.athm2app.Model.Visita;
import me.guillem.athm2app.Utils.DriveServiceHelper;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;

import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.DatePickerFragment;
import me.guillem.athm2app.Utils.TimePickerFragment;

import static com.fxn.utility.PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS;
import static me.guillem.athm2app.Views.TestingDrive.verifyStoragePermissions;

/**
 * * Created by Guillem on 02/12/20.
 */
public class CRUDVisits extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";
    private static final int REQUEST_CODE_SIGN_IN = 1;
    public static final Integer RecordAudioRequestCode = 1;

    private DriveServiceHelper mDriveServiceHelper;

    private FirebaseCRUD crudHelper = new FirebaseCRUD();
    private DatabaseReference db = Utils.getDatabaseRefence();

    private SpeechRecognizer speechRecognizer;
    private AdapterImages imagesAdapter;
    private Handler handler;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter;
    private Options options;
    private Bundle extras;

    private Animation mic_in, mic_out, bilink;
    private ImageView icon_recording, takephoto;
    private ImageButton micButton;
    private Button save;
    private TextView title_obra;
    private EditText timer;
    private TextInputEditText pickdata, picktime, nom_visita, descripcion;
    private AutoCompleteTextView nom_respo;
    private ProgressBar mprogressBar;
    protected RecyclerView recyclerView;
    private ActionBar actionBar;

    private ArrayList<String> returnValue = new ArrayList<>();
    private int audioTotalTime;
    private String ids, title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        //Get Bundle (title name and )
        extras = getIntent().getExtras();
        title = extras.getString("Obra");
        ids = extras.getString("ids");

        //Get name of obra
        title_obra = findViewById(R.id.titol_obra);
        title_obra.setText("Nova visita a l'obra " + title);

        //UI
        nom_visita = findViewById(R.id.nom_visita);
        nom_respo = findViewById(R.id.nom_respo);
        nom_respo.setText("Ramon");
        //Menu respos
        populateUserDrownMenu();

        descripcion = findViewById(R.id.descripcion);

        pickdata = findViewById(R.id.pickdata);
        pickdata.setOnClickListener(this);
        picktime = findViewById(R.id.picktime);
        picktime.setOnClickListener(this);

        //Date and time getters
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        pickdata.setText(currentDate);
        picktime.setText(currentTime);

        micButton = findViewById(R.id.mic_button);
        timer = findViewById(R.id.anim_mic);
        icon_recording = findViewById(R.id.icon_recording);

        takephoto = findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);
        findViewById(R.id.takephoto).setOnClickListener((View view) -> {
            options.setPreSelectedUrls(returnValue);

            Pix.start(CRUDVisits.this, options);
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        mprogressBar = findViewById(R.id.mProgressBarSave);

        recyclerView = findViewById(R.id.rv_images);

        //Voice methods
        voicerecognizr();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL));
        imagesAdapter = new AdapterImages(this);
        recyclerView.setAdapter(imagesAdapter);

        options = Options.init()
                .setRequestCode(100)
                .setCount(10)
                .setFrontfacing(false)
                .setPreSelectedUrls(returnValue)
                .setExcludeVideos(false)
                .setVideoDurationLimitinSeconds(30)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
                .setPath("/ATHM2");

        requestSignIn();
        verifyStoragePermissions(this);


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
                descripcion.setHint(R.string.waiting);
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
                speechRecognizer.stopListening();
                timer.setVisibility(View.GONE);
                icon_recording.setVisibility(View.GONE);
                descripcion.setVisibility(View.VISIBLE);
                micButton.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                icon_recording.clearAnimation();
                timerTask.cancel();

                //Snackbar.make(getactivi, "Esto es una prueba", Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onResults(Bundle bundle) {
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

        if (Utils.validar(nom_visita, nom_respo, pickdata, picktime, descripcion)) {
            nom_visit = nom_visita.getText().toString();
            responsable = nom_respo.getText().toString();
            data_visita = pickdata.getText().toString();
            hora_visita = picktime.getText().toString();
            descripcio = descripcion.getText().toString();

            for(int i=0; i<returnValue.size();i++) {
                Log.e("LOOK ARRAY",returnValue.toString());
                media.add(returnValue.get(i));
            }

            Visita newVisit = new Visita(data_visita,hora_visita,responsable, nom_visit, descripcio,media);
            createFile(returnValue);
            crudHelper.insertVisit(this,db,mprogressBar,newVisit, ids);

        }
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }

    private void createFile(ArrayList<String> returnValue) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Creating a file.");
            mDriveServiceHelper.createFolder();
            mDriveServiceHelper.createFile(returnValue, title_obra.getText().toString());
/*                    .addOnSuccessListener(fileId -> readFile(fileId))
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't create file.", exception));*/
        }
    }

    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }
/*
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

*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(CRUDVisits.this, options);
                } else {
                    Toast.makeText(CRUDVisits.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        switch (requestCode) {
            case (100): {
                if (resultCode == Activity.RESULT_OK) {
                    returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

                    System.out.println(returnValue);

                    imagesAdapter.addImage(returnValue);
                }
            }
            break;
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleSignInResult(data);
                }
                break;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(CRUDVisits.this, permissions, requestCode);
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
            case R.id.save:
                insertData();
                break;
        }
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
}
