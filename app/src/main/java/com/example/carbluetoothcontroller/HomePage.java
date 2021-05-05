package com.example.carbluetoothcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    RelativeLayout cLayout;
    private ViewPager viewPager;
    private ArrayList<HomePageOptions>homePageOptionsArrayList;
    private MyAdapter myAdapter;

    EditText f_input;
    EditText b_input;
    EditText l_input;
    EditText r_input;
    EditText o_input;
    EditText frontLightOn_input;
    EditText frontLightOff_input;
    EditText backLightOn_input;
    EditText backLightOff_input;
    ImageView save_button_touchControls;
    ImageView save_button_tiltMotionControls;
    ImageView save_button_voiceControl;





    ImageView settings_button;
    Dialog configurationCodesDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_home_page);


        cLayout=(RelativeLayout) findViewById(R.id.cLayout);
        settings_button=(ImageView) findViewById(R.id.settings_button);


        AnimationDrawable animationDrawable=(AnimationDrawable) cLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        viewPager = (ViewPager) findViewById(R.id.viewPager);

        homePageOptionsArrayList=new ArrayList<>();
        homePageOptionsArrayList.add(new HomePageOptions(R.drawable.joystick,"Touch Buttons "));
        homePageOptionsArrayList.add(new HomePageOptions(R.drawable.gesture_control_image,"Tilt Motion"));
        homePageOptionsArrayList.add(new HomePageOptions(R.drawable.voice_control2,"Voice Control"));


        myAdapter=new MyAdapter(this,homePageOptionsArrayList);
        viewPager.setAdapter(myAdapter);
        viewPager.setPadding(100,570,100,0);



        //DIALOG INI
        configurationCodesDialog=new Dialog(this);
        configurationCodesDialog.setContentView(R.layout.configuration_codes_dialog);





        f_input = (EditText) configurationCodesDialog.findViewById(R.id.f_input);
        b_input = (EditText) configurationCodesDialog.findViewById(R.id.b_input);
        l_input = (EditText) configurationCodesDialog.findViewById(R.id.l_input);
        r_input = (EditText) configurationCodesDialog.findViewById(R.id.r_input);
        o_input = (EditText) configurationCodesDialog.findViewById(R.id.o_input);

        frontLightOn_input = (EditText) configurationCodesDialog.findViewById(R.id.frontLightOn_input);
        frontLightOff_input = (EditText) configurationCodesDialog.findViewById(R.id.frontLightOff_input);
        backLightOn_input = (EditText) configurationCodesDialog.findViewById(R.id.backLightOn_input);
        backLightOff_input = (EditText) configurationCodesDialog.findViewById(R.id.backLightOff_input);



        save_button_touchControls = (ImageView) configurationCodesDialog.findViewById(R.id.save_button_touchControls);
        save_button_tiltMotionControls = (ImageView) configurationCodesDialog.findViewById(R.id.save_button_tiltMotionControls);
        save_button_voiceControl = (ImageView) configurationCodesDialog.findViewById(R.id.save_button_voiceControl);




        save_button_touchControls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(f_input.getText().toString().equals("")){f_input.setText("F");}
                if(b_input.getText().toString().equals("")){b_input.setText("B");}
                if(l_input.getText().toString().equals("")){l_input.setText("L");}
                if(r_input.getText().toString().equals("")){r_input.setText("R");}
                if(o_input.getText().toString().equals("")){o_input.setText("O");}

                if(frontLightOn_input.getText().toString().equals("")){frontLightOn_input.setText("Q");}
                if(frontLightOff_input.getText().toString().equals("")){frontLightOff_input.setText("q");}
                if(backLightOn_input.getText().toString().equals("")){backLightOn_input.setText("W");}
                if(backLightOff_input.getText().toString().equals("")){backLightOff_input.setText("w");}


                Intent intent = new Intent(getApplicationContext(), TouchButtonsControlsActivity.class);
                intent.putExtra("forwardInput", f_input.getText().toString().charAt(0));
                intent.putExtra("backInput", b_input.getText().toString().charAt(0));
                intent.putExtra("leftInput", l_input.getText().toString().charAt(0));
                intent.putExtra("rightInput", r_input.getText().toString().charAt(0));
                intent.putExtra("stopInput", o_input.getText().toString().charAt(0));

                intent.putExtra("fronLightOn", frontLightOn_input.getText().toString().charAt(0));
                intent.putExtra("frontLightOff", frontLightOff_input.getText().toString().charAt(0));
                intent.putExtra("backLightON", backLightOn_input.getText().toString().charAt(0));
                intent.putExtra("backLightOff", backLightOff_input.getText().toString().charAt(0));

                intent.putExtra("flagSaveButtonPressed", 1);

                startActivity(intent);

            }
        });


        save_button_tiltMotionControls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(f_input.getText().toString().equals("")){f_input.setText("F");}
                if(b_input.getText().toString().equals("")){b_input.setText("B");}
                if(l_input.getText().toString().equals("")){l_input.setText("L");}
                if(r_input.getText().toString().equals("")){r_input.setText("R");}
                if(o_input.getText().toString().equals("")){o_input.setText("O");}

                if(frontLightOn_input.getText().toString().equals("")){frontLightOn_input.setText("Q");}
                if(frontLightOff_input.getText().toString().equals("")){frontLightOff_input.setText("q");}
                if(backLightOn_input.getText().toString().equals("")){backLightOn_input.setText("W");}
                if(backLightOff_input.getText().toString().equals("")){backLightOff_input.setText("w");}


                Intent intent = new Intent(getApplicationContext(), GesturesMobileControls.class);
                intent.putExtra("forwardInput", f_input.getText().toString().charAt(0));
                intent.putExtra("backInput", b_input.getText().toString().charAt(0));
                intent.putExtra("leftInput", l_input.getText().toString().charAt(0));
                intent.putExtra("rightInput", r_input.getText().toString().charAt(0));
                intent.putExtra("stopInput", o_input.getText().toString().charAt(0));

                intent.putExtra("fronLightOn", frontLightOn_input.getText().toString().charAt(0));
                intent.putExtra("frontLightOff", frontLightOff_input.getText().toString().charAt(0));
                intent.putExtra("backLightON", backLightOn_input.getText().toString().charAt(0));
                intent.putExtra("backLightOff", backLightOff_input.getText().toString().charAt(0));

                intent.putExtra("flagSaveButtonPressed", 1);

                startActivity(intent);

            }
        });



        save_button_voiceControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(f_input.getText().toString().equals("")){f_input.setText("F");}
                if(b_input.getText().toString().equals("")){b_input.setText("B");}
                if(l_input.getText().toString().equals("")){l_input.setText("L");}
                if(r_input.getText().toString().equals("")){r_input.setText("R");}
                if(o_input.getText().toString().equals("")){o_input.setText("O");}

                if(frontLightOn_input.getText().toString().equals("")){frontLightOn_input.setText("Q");}
                if(frontLightOff_input.getText().toString().equals("")){frontLightOff_input.setText("q");}
                if(backLightOn_input.getText().toString().equals("")){backLightOn_input.setText("W");}
                if(backLightOff_input.getText().toString().equals("")){backLightOff_input.setText("w");}


                Intent intent = new Intent(getApplicationContext(), VoiceControl.class);
                intent.putExtra("forwardInput", f_input.getText().toString().charAt(0));
                intent.putExtra("backInput", b_input.getText().toString().charAt(0));
                intent.putExtra("leftInput", l_input.getText().toString().charAt(0));
                intent.putExtra("rightInput", r_input.getText().toString().charAt(0));
                intent.putExtra("stopInput", o_input.getText().toString().charAt(0));

                intent.putExtra("fronLightOn", frontLightOn_input.getText().toString().charAt(0));
                intent.putExtra("frontLightOff", frontLightOff_input.getText().toString().charAt(0));
                intent.putExtra("backLightON", backLightOn_input.getText().toString().charAt(0));
                intent.putExtra("backLightOff", backLightOff_input.getText().toString().charAt(0));

                intent.putExtra("flagSaveButtonPressed", 1);

                startActivity(intent);

            }
        });







        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                configurationCodesDialog.show();


            }
        });





    }
}