package com.example.carbluetoothcontroller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<HomePageOptions> homePageOptionsList;

    public MyAdapter(Context context, ArrayList<HomePageOptions> homePageOptionsList) {
        this.context = context;
        this.homePageOptionsList = homePageOptionsList;
    }


    @Override
    public int getCount() {
        return homePageOptionsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item,container,false);
        ImageView bannerImage = (ImageView)view.findViewById(R.id.bannerImage);
        TextView titleText = (TextView) view.findViewById(R.id.titleText);

        HomePageOptions homePageOptions=homePageOptionsList.get(position);
        String title=homePageOptions.getTitletext();
        int image=homePageOptions.getImage();

        bannerImage.setImageResource(image);
        titleText.setText(title);
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(position==0)
                {
                    TouchButtonsControlsActivity();

                }

                else if(position==1)
                    {
                        GesturesMobileControlsActivity();
                    }

                else
                    {
                        VoiceControlActivity();
                    }

            }
        });

        container.addView(view,position);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);
    }

    public void TouchButtonsControlsActivity(){
        Intent intent=new Intent(context, TouchButtonsControlsActivity.class);
        context.startActivity(intent);
    }

    public void GesturesMobileControlsActivity(){
        Intent intent=new Intent(context, GesturesMobileControls.class);
        context.startActivity(intent);
    }


    public void VoiceControlActivity(){
        Intent intent=new Intent(context, VoiceControl.class);
        context.startActivity(intent);
    }

}
