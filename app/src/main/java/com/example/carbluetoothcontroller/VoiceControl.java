package com.example.carbluetoothcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class VoiceControl extends AppCompatActivity {

    ConstraintLayout cLayout;
    ImageView home_button;
    ImageView bluetooth_button;
    ImageView info;
    ImageView red_power_button;

    TextView bluetooth_status;
    TextView paired_devices;

    Button paired_button;
    Button discoverable_button;
    Button connect_button;

    ImageView bluetooth_on_button;
    ImageView bluetooth_off_button;
    ImageView bluetooth_on_off;
    ImageView current_voice_arrow_left;
    ImageView current_voice_arrow_right;
    ImageView current_voice_arrow_up;
    ImageView current_voice_arrow_down;
    ImageView voice_arrow_left;
    ImageView voice_arrow_right;
    ImageView voice_arrow_up;
    ImageView voice_arrow_down;
    ImageView voice_back_lights_on;
    ImageView voice_back_lights_off;
    ImageView voice_front_lights_on;
    ImageView voice_front_lights_off;
    ImageView mic;



    public static final Integer  RecordAudioRequestCodde=1;
    private SpeechRecognizer speechRecognizer;

    char inputForward;
    char inputBackward;
    char inputLeft;
    char inputRight;
    char inputStop;
    int flagSaveButtonPressed;

    char frontLightOn;
    char frontLightOff;
    char backLightON;
    char backLightOff;


    EditText input_address;

    Dialog dialog;
    Dialog voiceControlINstructionDialog;
    Vibrator vibrator;

    BluetoothAdapter mBlueAdapter;
    BluetoothSocket btSocket;
    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_DISCOVER_BT=1;


    //The UUID/GUID is set by the device driver. It claims that the device is a member of a
    // particular class of devices that support a specific set of I/O requests (Read, Write, DeviceIoControl).
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    int flagConnected;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        cLayout=(ConstraintLayout) findViewById(R.id.cLayout);
        AnimationDrawable animationDrawable=(AnimationDrawable) cLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Toast.makeText(getBaseContext(), "Connect to a Bluetooth device🥵🥵", Toast.LENGTH_LONG).show();


        if(ContextCompat.checkSelfPermission(VoiceControl.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {

                ActivityCompat.requestPermissions(VoiceControl.this,new String[]{
                        Manifest.permission.RECORD_AUDIO},RecordAudioRequestCodde);

            }
        }


        Intent intent = getIntent();
        inputForward = intent.getCharExtra("forwardInput",'f');
        inputBackward = intent.getCharExtra("backInput",'b');
        inputLeft = intent.getCharExtra("leftInput",'l');
        inputRight = intent.getCharExtra("rightInput",'r');
        inputStop = intent.getCharExtra("stopInput",'s');

        frontLightOn = intent.getCharExtra("fronLightOn",'Q');
        frontLightOff = intent.getCharExtra("frontLightOff",'q');
        backLightON = intent.getCharExtra("backLightON",'W');
        backLightOff = intent.getCharExtra("rightInput",'w');

        flagSaveButtonPressed = intent.getIntExtra("flagSaveButtonPressed",0);


        bluetooth_button=(ImageView) findViewById(R.id.bluetooth_button);
        home_button=(ImageView) findViewById(R.id.home_button);
        info=(ImageView) findViewById(R.id.info);
        red_power_button=(ImageView) findViewById(R.id.red_power_button);


        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        voiceControlINstructionDialog=new Dialog(this);
        voiceControlINstructionDialog.setContentView(R.layout.voice_control_instruction_commands_dialog);

        //DIALOG INI
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.bluetooth_dialog);

        bluetooth_status = (TextView) dialog.findViewById(R.id.bluetooth_status);
        paired_devices = (TextView) dialog.findViewById(R.id.paired_devices);
        paired_button = (Button) dialog.findViewById(R.id.paired_button);
        discoverable_button = (Button) dialog.findViewById(R.id.discoverable_button);
        connect_button = (Button) dialog.findViewById(R.id.connect_button);
        bluetooth_on_button = (ImageView) dialog.findViewById(R.id.bluetooth_on_button);
        bluetooth_off_button = (ImageView) dialog.findViewById(R.id.bluetooth_off_button);
        bluetooth_on_off = (ImageView) dialog.findViewById(R.id.bluetooth_on_off);



        current_voice_arrow_left = (ImageView)findViewById(R.id.current_voice_arrow_left);
        current_voice_arrow_right = (ImageView) findViewById(R.id.current_voice_arrow_right);
        current_voice_arrow_up = (ImageView) findViewById(R.id.current_voice_arrow_up);
        current_voice_arrow_down = (ImageView) findViewById(R.id.current_voice_arrow_down);
        voice_arrow_left = (ImageView) findViewById(R.id.voice_arrow_left);
        voice_arrow_right = (ImageView) findViewById(R.id.voice_arrow_right);
        voice_arrow_up = (ImageView) findViewById(R.id.voice_arrow_up);
        voice_arrow_down = (ImageView) findViewById(R.id.voice_arrow_down);
        mic = (ImageView) findViewById(R.id.mic);



        voice_back_lights_on = (ImageView) findViewById(R.id.voice_back_lights_on);
        voice_back_lights_off = (ImageView) findViewById(R.id.voice_back_lights_off);
        voice_front_lights_on = (ImageView) findViewById(R.id.voice_front_lights_on);
        voice_front_lights_off = (ImageView) findViewById(R.id.voice_front_lights_off);


        current_voice_arrow_left.setVisibility(View.INVISIBLE);
        current_voice_arrow_right.setVisibility(View.INVISIBLE);
        current_voice_arrow_up.setVisibility(View.INVISIBLE);
        current_voice_arrow_down.setVisibility(View.INVISIBLE);

        voice_arrow_left.setVisibility(View.VISIBLE);
        voice_arrow_right.setVisibility(View.VISIBLE);
        voice_arrow_up.setVisibility(View.VISIBLE);
        voice_arrow_down.setVisibility(View.VISIBLE);

        voice_back_lights_off.setVisibility(View.VISIBLE);
        voice_front_lights_off.setVisibility(View.VISIBLE);

        voice_back_lights_on.setVisibility(View.INVISIBLE);
        voice_front_lights_on.setVisibility(View.INVISIBLE);




        mic.setImageResource(R.drawable.microphone_off);



        input_address=(EditText) dialog.findViewById(R.id.input_address);

        mBlueAdapter=BluetoothAdapter.getDefaultAdapter();

        btSocket = null;
        flagConnected=0;




        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());




        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

                Toast.makeText(VoiceControl.this,"Listening🧐🧐...",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                mic.setImageResource(R.drawable.microphone_off);

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String>arrayList=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(arrayList.contains("move forward")||arrayList.contains("Wolfblood") || arrayList.contains("Wolford") || arrayList.contains("forward") || arrayList.contains("wolf world")
                        || arrayList.contains("wolf boared") || arrayList.contains("morfboard") || arrayList.contains("morph board")
                        || arrayList.contains("watford")
                        || arrayList.contains("New Forest")|| arrayList.contains("Waterford")|| arrayList.contains("Wolfsburg")
                        || arrayList.contains("warfarin")|| arrayList.contains("Moss Bros")|| arrayList.contains("More 4"))
                {
                    if(flagConnected==1  && mBlueAdapter.isEnabled())
                    {

                        current_voice_arrow_up.setVisibility(View.VISIBLE);
                        voice_arrow_up.setVisibility(View.INVISIBLE);
                        vibrator.vibrate(400);

                        try { if(flagSaveButtonPressed==1) {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write(inputForward);
                        }

                        else
                        {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write('F');
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(arrayList.contains("move backward") ||arrayList.contains("backward") || arrayList.contains("walk backwards") || arrayList.contains("backwoods") || arrayList.contains("backworth")
                || arrayList.contains("move backwards") || arrayList.contains("all backwards") || arrayList.contains("move back")
                        || arrayList.contains("woodpeckers")|| arrayList.contains("love backwards"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {
                        current_voice_arrow_down.setVisibility(View.VISIBLE);
                        voice_arrow_down.setVisibility(View.INVISIBLE);

                        vibrator.vibrate(400);

                        try { if(flagSaveButtonPressed==1) {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write(inputBackward);
                        }

                        else
                        {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write('B');
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                if(arrayList.contains("turn left"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {
                        current_voice_arrow_left.setVisibility(View.VISIBLE);
                        voice_arrow_left.setVisibility(View.INVISIBLE);

                        vibrator.vibrate(400);

                        try { if(flagSaveButtonPressed==1) {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write(inputLeft);
                        }

                        else
                        {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write('L');
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                if(arrayList.contains("turn right"))
                {
                    if(flagConnected==1  && mBlueAdapter.isEnabled()  )
                    {
                        current_voice_arrow_right.setVisibility(View.VISIBLE);
                        voice_arrow_right.setVisibility(View.INVISIBLE);

                        vibrator.vibrate(400);

                        try { if(flagSaveButtonPressed==1) {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write(inputRight);
                        }

                        else
                        {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write('R');
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }


                if(arrayList.contains("front lights on"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {

                        voice_front_lights_off.setVisibility(View.INVISIBLE);
                        voice_front_lights_on.setVisibility(View.VISIBLE);


                        vibrator.vibrate(400);

                        try {

                            if(flagSaveButtonPressed==1) {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write(frontLightOn);
                            }

                            else
                            {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write('q');
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    else
                    {
                        Toast.makeText(VoiceControl.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                    }

                }



                if(arrayList.contains("front lights off"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {

                        voice_front_lights_off.setVisibility(View.VISIBLE);
                        voice_front_lights_on.setVisibility(View.INVISIBLE);


                        vibrator.vibrate(400);

                        try {

                            if(flagSaveButtonPressed==1) {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write(frontLightOff);
                            }

                            else
                            {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write('Q');
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    else
                    {
                        Toast.makeText(VoiceControl.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                    }
                }


                if(arrayList.contains("back lights on"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {

                        voice_back_lights_off.setVisibility(View.INVISIBLE);
                        voice_back_lights_on.setVisibility(View.VISIBLE);


                        vibrator.vibrate(400);

                        try {

                            if(flagSaveButtonPressed==1) {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write(backLightON);
                            }

                            else
                            {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write('w');
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    else
                    {
                        Toast.makeText(VoiceControl.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                    }
                }



                if(arrayList.contains("back lights off"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {

                        voice_back_lights_off.setVisibility(View.VISIBLE);
                        voice_back_lights_on.setVisibility(View.INVISIBLE);


                        vibrator.vibrate(400);

                        try {

                            if(flagSaveButtonPressed==1) {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write(backLightOff);
                            }

                            else
                            {
                                OutputStream outputStream = btSocket.getOutputStream();
                                outputStream.write('W');
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    else
                    {
                        Toast.makeText(VoiceControl.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                    }
                }

                if(arrayList.contains("stop"))
                {
                    if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                    {

                        current_voice_arrow_down.setVisibility(View.INVISIBLE);
                        current_voice_arrow_left.setVisibility(View.INVISIBLE);
                        current_voice_arrow_right.setVisibility(View.INVISIBLE);
                        current_voice_arrow_up.setVisibility(View.INVISIBLE);

                        voice_arrow_left.setVisibility(View.VISIBLE);
                        voice_arrow_right.setVisibility(View.VISIBLE);
                        voice_arrow_up.setVisibility(View.VISIBLE);
                        voice_arrow_down.setVisibility(View.VISIBLE);

                        try { if(flagSaveButtonPressed==1) {
                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write(inputStop);
                        }

                        else
                        {

                            OutputStream outputStream = btSocket.getOutputStream();
                            outputStream.write('O');
                        }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }


                Toast.makeText(VoiceControl.this,arrayList.get(0).toString(),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });










        //check if bluetooth is available
        if(mBlueAdapter==null)
        {
            bluetooth_status.setText("Bluetooth is not available");
            finish();
        }

        else
        {
            bluetooth_status.setText("Bluetooth is available");
        }

        //set image according to bluetooth status(on/off)
        if(mBlueAdapter.isEnabled())
        {
            bluetooth_on_off.setImageResource(R.drawable.bluetooth_on);
            bluetooth_on_button.setVisibility(View.INVISIBLE);
            bluetooth_off_button.setVisibility(View.VISIBLE);
        }

        else
        {
            bluetooth_on_off.setImageResource(R.drawable.bluetooth_off);
            bluetooth_on_button.setVisibility(View.VISIBLE);
            bluetooth_off_button.setVisibility(View.INVISIBLE);
        }




        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dialog.show();

            }
        });


        paired_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()){
                    paired_devices.setText("Paired Devices✔:");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        paired_devices.append("\nDevice: " + device.getName()+ ", " + device);
                    }
                }
                else {
                    //bluetooth is off so can't get paired devices
                    Toast.makeText(VoiceControl.this,"Turn on bluetooth to get paired devices 🙄",Toast.LENGTH_LONG).show();

                }
            }
        });


        discoverable_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(!mBlueAdapter.isDiscovering())
                {
                    Toast.makeText(VoiceControl.this,"Making Your Device Discoverable ☺",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DISCOVER_BT);

                }




            }
        });


        bluetooth_on_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                if(!mBlueAdapter.isEnabled())
                {
                    Toast.makeText(VoiceControl.this,"Turning On Bluetooth...",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);


                }

                else
                {
                    Toast.makeText(VoiceControl.this,"Bluetooth is Already Turned On 🤷‍♂️",Toast.LENGTH_LONG).show();

                }


            }
        });



        bluetooth_off_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                //Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth Turned Off",Toast.LENGTH_LONG).show();

                if (mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    Toast.makeText(VoiceControl.this," Bluetooth Turned Off😪...",Toast.LENGTH_LONG).show();
                    bluetooth_on_off.setImageResource(R.drawable.bluetooth_off);


                    bluetooth_on_button.setVisibility(View.VISIBLE);
                    bluetooth_off_button.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(VoiceControl.this,"Bluetooth Is Already Off 🤷‍♂️",Toast.LENGTH_LONG).show();

                }


            }
        });



        connect_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(mBlueAdapter.isEnabled())
                {

                    int counter = 0;
                    do {
                        try {
                            BluetoothDevice  hc05 = mBlueAdapter.getRemoteDevice(input_address.getText().toString());
                            //BluetoothDevice  hc05 = mBlueAdapter.getRemoteDevice("98:D3:36:F5:A8:E2");
                            btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                            System.out.println(btSocket);
                            btSocket.connect();
                            System.out.println(btSocket.isConnected());
                            Toast.makeText(VoiceControl.this,"Connecting 🤗🤗...",Toast.LENGTH_SHORT).show();

                            if(btSocket.isConnected())
                            {
                                Toast.makeText(VoiceControl.this,"Connected Succefully 🤩",Toast.LENGTH_LONG).show();
                                flagConnected=1;
                            }


                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(),"Connection Unsuccessful...Make sure (HCO5) is turned on and in range😨😨😨",Toast.LENGTH_LONG).show();

                        }
                        catch (IllegalArgumentException h)
                        {

                            Toast.makeText(VoiceControl.this,"Connection Failed😫😫..Please Enter The Device Address Correctly And Try Again🤷‍♂🤷‍♂",Toast.LENGTH_LONG).show();
                            break;
                        }
                        counter++;
                    } while (!btSocket.isConnected() && counter < 3);

                }


                else
                {
                    Toast.makeText(VoiceControl.this,"Please Turn On Your Bluetooth First 🙄",Toast.LENGTH_LONG).show();

                }

            }
        });



        mic.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if(event.getAction()==event.ACTION_UP)
                {
                    speechRecognizer.stopListening();



                }

                if(event.getAction()==event.ACTION_DOWN)
                {
                    speechRecognizer.startListening(speechIntent);
                    mic.setImageResource(R.drawable.microphone_on2);

                }
                return false;
            }
        });



        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                HomePageActivity();

            }
        });



        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                voiceControlINstructionDialog.show();

            }
        });



        red_power_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    current_voice_arrow_down.setVisibility(View.INVISIBLE);
                    current_voice_arrow_left.setVisibility(View.INVISIBLE);
                    current_voice_arrow_right.setVisibility(View.INVISIBLE);
                    current_voice_arrow_up.setVisibility(View.INVISIBLE);

                    voice_arrow_left.setVisibility(View.VISIBLE);
                    voice_arrow_right.setVisibility(View.VISIBLE);
                    voice_arrow_up.setVisibility(View.VISIBLE);
                    voice_arrow_down.setVisibility(View.VISIBLE);

                    try { if(flagSaveButtonPressed==1) {
                        OutputStream outputStream = btSocket.getOutputStream();
                        outputStream.write(inputStop);
                    }

                    else
                    {

                        OutputStream outputStream = btSocket.getOutputStream();
                        outputStream.write('O');
                    }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getBaseContext(), "🤯😖🤯😖🤯😖", Toast.LENGTH_LONG).show();





            }
        });




    }    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    //bluetooth is on
                    bluetooth_on_off.setImageResource(R.drawable.bluetooth_on);
                    Toast.makeText(VoiceControl.this,"Bluetooth Is On 😁",Toast.LENGTH_SHORT).show();
                    bluetooth_on_button.setVisibility(View.INVISIBLE);
                    bluetooth_off_button.setVisibility(View.VISIBLE);
                }
                else {
                    //user denied to turn bluetooth on
                    Toast.makeText(VoiceControl.this,"Bluetooth Is Off 😪",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
        bluetooth_on_off.setImageResource(R.drawable.bluetooth_on);
        bluetooth_on_button.setVisibility(View.INVISIBLE);
        bluetooth_off_button.setVisibility(View.VISIBLE);
    }







    public void HomePageActivity(){
        Intent intent=new Intent(this, HomePage.class);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==RecordAudioRequestCodde && grantResults.length>0)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(VoiceControl.this,"Permission Granted😉😉",Toast.LENGTH_LONG).show();

            }
        }
    }
}