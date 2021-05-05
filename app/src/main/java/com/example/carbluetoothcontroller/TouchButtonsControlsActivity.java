package com.example.carbluetoothcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import java.util.Set;

public class TouchButtonsControlsActivity extends AppCompatActivity {

    ConstraintLayout cLayout;
    ImageView left_button;
    ImageView right_button;
    ImageView up_button;
    ImageView down_button;
    ImageView home_button;
    ImageView bluetooth_button;
    ImageView red_power_button;
    ImageView current_left;
    ImageView current_right;
    ImageView current_up;
    ImageView current_down;
    ImageView current_left2;
    ImageView current_right2;
    ImageView current_up2;
    ImageView current_down2;

    ImageView frontLights_on;
    ImageView frontLights_off;
    ImageView backLights_on;
    ImageView backLights_off;



    TextView bluetooth_status;
    TextView paired_devices;
    Button paired_button;
    Button discoverable_button;
    Button connect_button;
    ImageView bluetooth_on_button;
    ImageView bluetooth_off_button;
    ImageView bluetooth_on_off;


    EditText input_address;

    Dialog dialog;

    Vibrator vibrator;


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


    /*Using BluetoothAdapter class we will do the following operations:

      Check if Bluetooth is available or not.
      Turn On/Off Bluetooth.
      Make Bluetooth Discoverable.
      Display Paired/Bounded devices.*/

    BluetoothAdapter mBlueAdapter;
    BluetoothSocket btSocket;
    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_DISCOVER_BT=1;


    //The UUID/GUID is set by the device driver. It claims that the device is a member of a
    // particular class of devices that support a specific set of I/O requests (Read, Write, DeviceIoControl).
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    int flagConnected;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_buttons_controls);


        Toast.makeText(getBaseContext(), "Connect to a Bluetooth device🥵🥵", Toast.LENGTH_LONG).show();


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



        cLayout=(ConstraintLayout) findViewById(R.id.cLayout);
        left_button=(ImageView) findViewById(R.id.left_button);
        right_button=(ImageView) findViewById(R.id.right_button);
        up_button=(ImageView) findViewById(R.id.up_button);
        down_button=(ImageView) findViewById(R.id.down_button);
        red_power_button=(ImageView) findViewById(R.id.red_power_button);
        bluetooth_button=(ImageView) findViewById(R.id.bluetooth_button);
        home_button=(ImageView) findViewById(R.id.home_button);

        current_left=(ImageView) findViewById(R.id.current_left);
        current_right=(ImageView) findViewById(R.id.current_right);
        current_up=(ImageView) findViewById(R.id.current_up);
        current_down=(ImageView) findViewById(R.id.current_down);

        current_left2=(ImageView) findViewById(R.id.current_left2);
        current_right2=(ImageView) findViewById(R.id.current_right2);
        current_up2=(ImageView) findViewById(R.id.current_up2);
        current_down2=(ImageView) findViewById(R.id.current_down2);

        frontLights_on=(ImageView) findViewById(R.id.frontLights_on);
        frontLights_off=(ImageView) findViewById(R.id.frontLights_off);
        backLights_on=(ImageView) findViewById(R.id.backLights_on);
        backLights_off=(ImageView) findViewById(R.id.backLights_off);





        AnimationDrawable animationDrawable=(AnimationDrawable) cLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        current_left.setVisibility(View.INVISIBLE);
        current_right.setVisibility(View.INVISIBLE);
        current_up.setVisibility(View.INVISIBLE);
        current_down.setVisibility(View.INVISIBLE);

        current_left2.setVisibility(View.VISIBLE);
        current_right2.setVisibility(View.VISIBLE);
        current_up2.setVisibility(View.VISIBLE);
        current_down2.setVisibility(View.VISIBLE);

        frontLights_off.setVisibility(View.VISIBLE);
        backLights_off.setVisibility(View.VISIBLE);
        frontLights_on.setVisibility(View.INVISIBLE);
        backLights_on.setVisibility(View.INVISIBLE);





        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

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
        input_address=(EditText) dialog.findViewById(R.id.input_address);

        mBlueAdapter=BluetoothAdapter.getDefaultAdapter();

        btSocket = null;
         flagConnected=0;




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
                    Toast.makeText(TouchButtonsControlsActivity.this,"Turn on bluetooth to get paired devices 🙄",Toast.LENGTH_LONG).show();

                }
            }
        });


        discoverable_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(!mBlueAdapter.isDiscovering())
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Making Your Device Discoverable ☺",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DISCOVER_BT);

                }




            }
        });


        bluetooth_on_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                if(!mBlueAdapter.isEnabled())
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Turning On Bluetooth...",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);


                }

                else
                    {
                        Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth is Already Turned On 🤷‍♂️",Toast.LENGTH_LONG).show();

                    }


            }
        });



        bluetooth_off_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                //Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth Turned Off",Toast.LENGTH_LONG).show();

                if (mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    Toast.makeText(TouchButtonsControlsActivity.this," Bluetooth Turned Off😪...",Toast.LENGTH_LONG).show();
                    bluetooth_on_off.setImageResource(R.drawable.bluetooth_off);


                    bluetooth_on_button.setVisibility(View.VISIBLE);
                    bluetooth_off_button.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth Is Already Off 🤷‍♂️",Toast.LENGTH_LONG).show();

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
                           // BluetoothDevice  hc05 = mBlueAdapter.getRemoteDevice("98:D3:36:F5:A8:E2");
                            btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                            System.out.println(btSocket);
                            btSocket.connect();
                            System.out.println(btSocket.isConnected());
                            Toast.makeText(TouchButtonsControlsActivity.this,"Connecting 🤗🤗...",Toast.LENGTH_SHORT).show();

                            if(btSocket.isConnected())
                            {
                                Toast.makeText(TouchButtonsControlsActivity.this,"Connected Succefully 🤩",Toast.LENGTH_LONG).show();
                                flagConnected=1;
                            }



                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(),"Connection Unsuccessful...Make sure (HCO5) is turned on and in range😨😨😨",Toast.LENGTH_LONG).show();
                        }
                        catch (IllegalArgumentException h)
                        {

                            Toast.makeText(TouchButtonsControlsActivity.this,"Connection Failed😫😫..Please Enter The Device Address Correctly And Try Again🤷‍♂🤷‍♂",Toast.LENGTH_LONG).show();
                            break;
                        }
                        counter++;
                    } while (!btSocket.isConnected() && counter < 3);

                }


                else
                    {
                        Toast.makeText(TouchButtonsControlsActivity.this,"Please Turn On Your Bluetooth First 🙄",Toast.LENGTH_LONG).show();

                    }

            }
        });




        frontLights_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    frontLights_off.setVisibility(View.INVISIBLE);
                    frontLights_on.setVisibility(View.VISIBLE);


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
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });



        frontLights_on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    frontLights_off.setVisibility(View.VISIBLE);
                    frontLights_on.setVisibility(View.INVISIBLE);


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
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });



        backLights_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    backLights_off.setVisibility(View.INVISIBLE);
                    backLights_on.setVisibility(View.VISIBLE);


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
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });




        backLights_on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    backLights_off.setVisibility(View.VISIBLE);
                    backLights_on.setVisibility(View.INVISIBLE);


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
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });








        left_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    current_left.setVisibility(View.VISIBLE);
                    current_left2.setVisibility(View.INVISIBLE);


                    vibrator.vibrate(400);

                    try {

                        if(flagSaveButtonPressed==1) {
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


                else
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });




        right_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1  && mBlueAdapter.isEnabled()  )
                {


                    current_right.setVisibility(View.VISIBLE);
                    current_right2.setVisibility(View.INVISIBLE);

                    vibrator.vibrate(400);

                    try {
                        if(flagSaveButtonPressed==1) {
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


                else
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });




        up_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1  && mBlueAdapter.isEnabled())
                {


                    current_up.setVisibility(View.VISIBLE);
                    current_up2.setVisibility(View.INVISIBLE);

                    vibrator.vibrate(400);

                    try {
                        if(flagSaveButtonPressed==1) {
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


                else
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });




        down_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {
                    current_down.setVisibility(View.VISIBLE);
                    current_down2.setVisibility(View.INVISIBLE);

                    vibrator.vibrate(400);

                    try {
                        if(flagSaveButtonPressed==1) {
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


                else
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }

            }
        });







        red_power_button.setOnClickListener(new View.OnClickListener() {//lama 2adoos 3al red ya2fl w ya2lb 2a5dr
            public void onClick(View view) {


                if(flagConnected==1 && mBlueAdapter.isEnabled()  )
                {

                    current_left.setVisibility(View.INVISIBLE);
                    current_right.setVisibility(View.INVISIBLE);
                    current_up.setVisibility(View.INVISIBLE);
                    current_down.setVisibility(View.INVISIBLE);

                    current_left2.setVisibility(View.VISIBLE);
                    current_right2.setVisibility(View.VISIBLE);
                    current_up2.setVisibility(View.VISIBLE);
                    current_down2.setVisibility(View.VISIBLE);


                    vibrator.vibrate(400);

                    try {
                        if(flagSaveButtonPressed==1) {
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


                else
                {
                    Toast.makeText(TouchButtonsControlsActivity.this,"Please Connect To a Bluetooth Device First 🤬",Toast.LENGTH_LONG).show();

                }


            }
        });













        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                HomePageActivity();

            }
        });









    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    //bluetooth is on
                    bluetooth_on_off.setImageResource(R.drawable.bluetooth_on);
                    Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth Is On 😁",Toast.LENGTH_LONG).show();
                    bluetooth_on_button.setVisibility(View.INVISIBLE);
                    bluetooth_off_button.setVisibility(View.VISIBLE);
                }
                else {
                    //user denied to turn bluetooth on
                    Toast.makeText(TouchButtonsControlsActivity.this,"Bluetooth Is Off 😪",Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void HomePageActivity(){
        Intent intent=new Intent(this, HomePage.class);
        startActivity(intent);
    }
}