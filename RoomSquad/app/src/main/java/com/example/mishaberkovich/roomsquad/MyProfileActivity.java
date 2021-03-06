package com.example.mishaberkovich.roomsquad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MyProfileActivity extends AppCompatActivity {


    Firebase roomsquad_firebase = new Firebase("https://roomsquad.firebaseio.com/");
    Firebase current_user = roomsquad_firebase.child("users").child(roomsquad_firebase.getAuth().getUid().toString());
    final static ArrayList<String> profile_information = new ArrayList<String>();
    final static int PROF_INFO_SIZE = 10;//will change
    final static int name_loc = 0;//used to track the location of the profile name
    final static int photo_loc = 1;
    final static int tagline_loc = 2;//used to track location of profile tagline
    final static int birthdate_loc = 3;//used to track the location of the birthdate in the profile_information arraylist
    final static int gender_loc = 4;//used to track the gender
    final static int smoking_loc = 5; //used to track the index of the smoking preference
    final static int pets_loc = 6; //used to track the index of the pets preference
    final static int nights_out_loc = 7; //used to track the index of the number of nights out preference
    final static int job_loc = 8; //used to track the index of the full time job yes or no question
    final static int wake_time_loc = 9; //used to track the index of the wake up in the morning preference

    ValueEventListener myProfileValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Firebase.setAndroidContext(this);



        //initialize the arraylist
        for (int i=0; i<PROF_INFO_SIZE; i++){
            profile_information.add("");
        }



        final TextView profile_name_view_text = (TextView) findViewById(R.id.user_profile_name);//edit text for profile name
        final TextView profile_tagline_view_text = (TextView) findViewById(R.id.user_profile_tagline);//edit text for tagline
        final ImageView profile_pic = (ImageView) findViewById(R.id.profile_photo);//profile picture
        profile_name_view_text.setVisibility(View.INVISIBLE);
        profile_tagline_view_text.setVisibility(View.INVISIBLE);
        profile_pic.setVisibility(View.INVISIBLE);//don't show the default photo right away

        myProfileValueEventListener = new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if (dataSnapshot.getChildren()!=null) {//if profile is initialized
                    String name = (String) dataSnapshot.child("name").getValue();
                    String tagline = (String) dataSnapshot.child("tagline").getValue();
                    String photo = (String) dataSnapshot.child("photo").getValue();
                    String birthdate = (String) dataSnapshot.child("birthdate").getValue();
                    String gender = (String) dataSnapshot.child("gender").getValue();
                    String smoking = (String) dataSnapshot.child("smoking").getValue();
                    String pets = (String) dataSnapshot.child("pets").getValue();
                    String nights_out = (String) dataSnapshot.child("nights_out").getValue();
                    String job = (String) dataSnapshot.child("job").getValue();
                    String wake_time = (String) dataSnapshot.child("wake_time").getValue();

                    profile_information.remove(name_loc);
                    profile_information.add(name_loc, name);
                    if (profile_information.get(name_loc)!=null){
                        profile_name_view_text.setText(profile_information.get(name_loc));
                    }
                    profile_name_view_text.setVisibility(View.VISIBLE);
                    profile_information.remove(tagline_loc);
                    profile_information.add(tagline_loc, tagline);
                    if (profile_information.get(tagline_loc)!=null){
                        profile_tagline_view_text.setText(profile_information.get(tagline_loc));
                    }
                    profile_tagline_view_text.setVisibility(View.VISIBLE);
                    profile_information.remove(photo_loc);
                    profile_information.add(photo_loc, photo);
                    if (profile_information.get(photo_loc) != null) {
                        byte[] image_in_bytes = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap pic_bm = BitmapFactory.decodeByteArray(image_in_bytes, 0, image_in_bytes.length);
                        profile_pic.setImageBitmap(pic_bm);
                    }
                    profile_pic.setVisibility(View.VISIBLE);
                    profile_information.remove(birthdate_loc);
                    profile_information.add(birthdate_loc, birthdate);
                    //get age to display
                    if (profile_information.get(birthdate_loc)!=null){
                        displayAge(MyProfileActivity.this);
                    }
                    profile_information.remove(gender_loc);
                    profile_information.add(gender_loc, gender);
                    if (profile_information.get(gender_loc)!=null){
                        displayGender();
                    }

                    profile_information.remove(smoking_loc);
                    profile_information.add(smoking_loc, smoking);
                    if (profile_information.get(smoking_loc) != null) {
                        displaySmoking();
                    }

                    profile_information.remove(pets_loc);
                    profile_information.add(pets_loc, pets);
                    if (profile_information.get(pets_loc) != null) {
                        displayPets();
                    }

                    profile_information.remove(nights_out_loc);
                    profile_information.add(nights_out_loc, nights_out);
                    if (profile_information.get(nights_out_loc) != null) {
                        displayNightsOut();
                    }

                    profile_information.remove(job_loc);
                    profile_information.add(job_loc, job);
                    if (profile_information.get(job_loc) != null) {
                        displayJob();
                    }

                    profile_information.remove(wake_time_loc);
                    profile_information.add(wake_time_loc, wake_time);
                    if (profile_information.get(wake_time_loc) != null) {
                        displayWakeTime();
                    }
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        };

        //listens for changes of value to display them to UI
        current_user.child("profile").addValueEventListener(myProfileValueEventListener);


        Button GoToMyPostings = (Button) findViewById(R.id.my_profile_to_my_postings_button);
        GoToMyPostings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_my_postings = new Intent(MyProfileActivity.this, MyPostingsActivity.class);
                go_to_my_postings.putExtra("user_id", roomsquad_firebase.getAuth().getUid().toString());
                MyProfileActivity.this.startActivity(go_to_my_postings);
            }
        });

        ImageButton GoToEditProfile = (ImageButton) findViewById(R.id.my_profile_to_edit_profile_button);
        GoToEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_edit_profile = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                MyProfileActivity.this.startActivity(go_to_edit_profile);
            }
        });

        Button GoToMainMenuButton = (Button) findViewById(R.id.my_profile_to_main_menu_button);
        GoToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent go_to_main_menu = new Intent(MyProfileActivity.this, MainMenuActivity.class);
                    MyProfileActivity.this.startActivity(go_to_main_menu);
            }
        });
    }

    @Override
    protected void onStart(){
        System.out.println("onStart method for MyProfileActivity being called");
        super.onStart();
    }

    @Override
    protected void onRestart(){
        System.out.println("onRestart method for MyProfileActivity being called");
        super.onRestart();
    }

    @Override
    protected void onPause(){
        System.out.println("onPause method for MyProfileActivity being called");
        super.onPause();
    }

    @Override
    protected void onResume(){
        System.out.println("onResume method for MyProfileActivity being called");
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
        for(int i=0; i <PROF_INFO_SIZE; i++){
            profile_information.remove(0);
        }
        current_user.child("profile").removeEventListener(myProfileValueEventListener);

    }

    @Override
    protected void onDestroy(){
        System.out.println("onDestroy method for MyProfileActivity being called");
        super.onDestroy();
    }

    //this method gets the age
    public static void displayAge(Activity activity){

        String birthdate = profile_information.get(birthdate_loc);
        //gets the day month and year into integers
        int day = Integer.parseInt(profile_information.get(birthdate_loc).substring(0, profile_information.get(birthdate_loc).indexOf('/')));
        int month = Integer.parseInt(profile_information.get(birthdate_loc).substring(profile_information.get(birthdate_loc).indexOf('/') + 1, profile_information.get(birthdate_loc).lastIndexOf('/')))-1;
        //due to way it is encoded with january starting at 0, need to subtract 1
        int year = Integer.parseInt(profile_information.get(birthdate_loc).substring(profile_information.get(birthdate_loc).lastIndexOf('/') + 1, profile_information.get(birthdate_loc).length()));

        //adds the age based on the birthdate to show up on the UI
        final Calendar c = Calendar.getInstance();
        int current_year = c.get(Calendar.YEAR);
        int current_month = c.get(Calendar.MONTH);
        int current_day = c.get(Calendar.DAY_OF_MONTH);
        int age;
        if (current_month >= month-1){
            if (current_day >= day) {
                age = current_year - year;
            }else{
                age = current_year - year - 1;
            }
        } else {
            age = current_year - year - 1;
        }
        TextView profile_info = (TextView) activity.findViewById(R.id.my_profile_info);
        String profile_info_text="";
        for (int i=0; i < profile_info.getText().length(); i++){
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_age = profile_info_text.indexOf("Age:") + 4;
        int profile_info_gender = profile_info_text.indexOf("Gender:");
        profile_info_text = profile_info_text.substring(0,profile_info_age) +" "+ age +"\n\n"+ profile_info_text.substring(profile_info_gender, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displayGender(){
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text="";
        for (int i=0; i < profile_info.getText().length(); i++){
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_gender = profile_info_text.indexOf("Gender:") + 7;
        int profile_info_smoking = profile_info_text.indexOf("Smoking:");
        profile_info_text = profile_info_text.substring(0,profile_info_gender) +" "+ profile_information.get(gender_loc) +"\n\n"+ profile_info_text.substring(profile_info_smoking, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displaySmoking() {
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text = "";
        for (int i = 0; i < profile_info.getText().length(); i++) {
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_smoking = profile_info_text.indexOf("Smoking:") + 8;
        int profile_info_pets = profile_info_text.indexOf("Okay with Pets?:");
        profile_info_text = profile_info_text.substring(0, profile_info_smoking) + " " + profile_information.get(smoking_loc) + "\n\n" + profile_info_text.substring(profile_info_pets, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displayPets() {
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text = "";
        for (int i = 0; i < profile_info.getText().length(); i++) {
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_pets = profile_info_text.indexOf("Okay with Pets?:") + 16;
        int profile_info_nights_out = profile_info_text.indexOf("Number of nights going out:");
        profile_info_text = profile_info_text.substring(0, profile_info_pets) + " " + profile_information.get(pets_loc) + "\n\n" + profile_info_text.substring(profile_info_nights_out, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displayNightsOut() {
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text = "";
        for (int i = 0; i < profile_info.getText().length(); i++) {
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_nights_out = profile_info_text.indexOf("Number of nights going out:") + 27;
        int profile_info_job = profile_info_text.indexOf("Full Time Job?:");
        profile_info_text = profile_info_text.substring(0, profile_info_nights_out) + " " + profile_information.get(nights_out_loc) + "\n\n" + profile_info_text.substring(profile_info_job, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displayJob() {
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text = "";
        for (int i = 0; i < profile_info.getText().length(); i++) {
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_job = profile_info_text.indexOf("Full Time Job?:") + 15;
        int profile_info_riser = profile_info_text.indexOf("Early Riser vs Late Riser:");
        profile_info_text = profile_info_text.substring(0, profile_info_job) + " " + profile_information.get(job_loc) + "\n\n" + profile_info_text.substring(profile_info_riser, profile_info_text.length());
        profile_info.setText(profile_info_text);
    }

    public void displayWakeTime() {
        TextView profile_info = (TextView) findViewById(R.id.my_profile_info);
        String profile_info_text = "";
        for (int i = 0; i < profile_info.getText().length(); i++) {
            profile_info_text = profile_info_text + profile_info.getText().charAt(i);
        }
        int profile_info_riser = profile_info_text.indexOf("Early Riser vs Late Riser:") + 26;
        int profile_info_end = profile_info_text.length();
        profile_info_text = profile_info_text.substring(0, profile_info_riser) + " " + profile_information.get(wake_time_loc);
        profile_info.setText(profile_info_text);
    }


}
