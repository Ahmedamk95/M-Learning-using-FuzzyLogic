package com.saboo.mlearning;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import util.LoginSession;

public class StaffDashboard extends AppCompatActivity {


    Button bt_announcements, bt_studymaterials, bt_quizzes, bt_progressreport, bt_feedback, bt_queries, bt_profile, bt_logout;
    final static String TAG = "StaffDashboard";
    Context context = this;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_staff_dashboard );

        init();
    }

    private void init(){
        initViews();

        //feedbackButtonListener();
        queriesButtonListener();
        announcementButtonListener();
        studyMaterialsButtonListener();
        quizButtonListener();
        logoutButtonListener();
    }

    private void initViews(){
        bt_announcements = (Button) findViewById( R.id.bt_announcements );
        bt_studymaterials = (Button) findViewById( R.id.bt_studymaterials );
        bt_quizzes = (Button) findViewById( R.id.bt_quizzes );
        bt_progressreport = (Button) findViewById( R.id.bt_progressreport );
        bt_feedback = (Button) findViewById( R.id.bt_feedback );
        bt_queries = (Button) findViewById( R.id.bt_queries );
        bt_profile = (Button) findViewById( R.id.bt_profile );
        bt_logout = (Button) findViewById( R.id.bt_logout );
    }


    private void feedbackButtonListener(){
        bt_feedback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent in = new Intent( context, Feedback.class );
                startActivity( in );

            }
        });
    }

    private void queriesButtonListener(){
        bt_queries.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent in = new Intent( context, StaffQueriesActivity.class );
                startActivity( in );

            }
        });
    }

    private void announcementButtonListener(){
        bt_announcements.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent in = new Intent( context, CreateAnnouncementActivity.class );
                startActivity( in );

            }
        });
    }

    private void studyMaterialsButtonListener(){
        bt_studymaterials.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent in = new Intent( context, UploadStudyActivity.class );
                startActivity( in );

            }
        });
    }

    private void quizButtonListener(){
        bt_quizzes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                Intent in = new Intent( context, CreateQuizActivity.class );
                startActivity( in );

            }
        });
    }

    private void logoutButtonListener(){
        bt_logout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                LoginSession.logOut( context );

                Toast.makeText( context, "You have been logged out successfully 1", Toast.LENGTH_SHORT ).show();

                Intent in = new Intent( context, MainActivity.class );
                startActivity( in );
                finish();

            }
        });
    }
}
