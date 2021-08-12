package com.saboo.mlearning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class FeedbackFormActivity extends Activity {

    final static String TAG = "FeedbackFormActivity";
    Context context = this;
    Button bt_submit_feedback;

    RadioGroup rg1, rg2, rg3, rg4, rg5;
    RadioButton rb1_rg1, rb2_rg1, rb3_rg1, rb4_rg1, rb5_rg1;
    RadioButton rb1_rg2, rb2_rg2, rb3_rg2, rb4_rg2, rb5_rg2;
    RadioButton rb1_rg3, rb2_rg3, rb3_rg3, rb4_rg3, rb5_rg3;
    RadioButton rb1_rg4, rb2_rg4, rb3_rg4, rb4_rg4, rb5_rg4;
    RadioButton rb1_rg5, rb2_rg5, rb3_rg5, rb4_rg5, rb5_rg5;

    String username, current_sem;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feedback_form );

        init();


    }

    private void init(){
        Intent in = getIntent();
        username = in.getStringExtra( "username" );
        current_sem = in.getStringExtra( "current_sem" );

        Toast.makeText( context, username + "," + current_sem, Toast.LENGTH_LONG ).show();

        initViews();

        submitFeedbackListener();
    }

    private void initViews(){
        bt_submit_feedback = (Button) findViewById( R.id.bt_submit_feedback );

        rg1 = (RadioGroup) findViewById( R.id.rg1 );
        rg2 = (RadioGroup) findViewById( R.id.rg2 );
        rg3 = (RadioGroup) findViewById( R.id.rg3 );
        rg4 = (RadioGroup) findViewById( R.id.rg4 );
        rg5 = (RadioGroup) findViewById( R.id.rg5 );
        rb1_rg1 = (RadioButton) findViewById( R.id.rb1_rg1 );
        rb2_rg1 = (RadioButton) findViewById( R.id.rb2_rg1 );
        rb3_rg1 = (RadioButton) findViewById( R.id.rb3_rg1 );
        rb4_rg1 = (RadioButton) findViewById( R.id.rb4_rg1 );
        rb5_rg1 = (RadioButton) findViewById( R.id.rb5_rg1 );
        rb1_rg2 = (RadioButton) findViewById( R.id.rb1_rg2 );
        rb2_rg2 = (RadioButton) findViewById( R.id.rb2_rg2 );
        rb3_rg2 = (RadioButton) findViewById( R.id.rb3_rg2 );
        rb4_rg2 = (RadioButton) findViewById( R.id.rb4_rg2 );
        rb5_rg2 = (RadioButton) findViewById( R.id.rb5_rg2 );
        rb1_rg3 = (RadioButton) findViewById( R.id.rb1_rg3 );
        rb2_rg3 = (RadioButton) findViewById( R.id.rb2_rg3 );
        rb3_rg3 = (RadioButton) findViewById( R.id.rb3_rg3 );
        rb4_rg3 = (RadioButton) findViewById( R.id.rb4_rg3 );
        rb5_rg3 = (RadioButton) findViewById( R.id.rb5_rg3 );
        rb1_rg4 = (RadioButton) findViewById( R.id.rb1_rg4 );
        rb2_rg4 = (RadioButton) findViewById( R.id.rb2_rg4 );
        rb3_rg4 = (RadioButton) findViewById( R.id.rb3_rg4 );
        rb4_rg4 = (RadioButton) findViewById( R.id.rb4_rg4 );
        rb5_rg4 = (RadioButton) findViewById( R.id.rb5_rg4 );
        rb1_rg5 = (RadioButton) findViewById( R.id.rb1_rg5 );
        rb2_rg5 = (RadioButton) findViewById( R.id.rb2_rg5 );
        rb3_rg5 = (RadioButton) findViewById( R.id.rb3_rg5 );
        rb4_rg5 = (RadioButton) findViewById( R.id.rb4_rg5 );
        rb5_rg5 = (RadioButton) findViewById( R.id.rb5_rg5 );


    }

    int i1 = 4, i2 = 4, i3 = 4, i4 = 4, i5 = 4;
    String result = "";
    ProgressDialog pd;


    private void submitFeedbackListener(){
        /*rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( RadioGroup radioGroup, @IdRes int i ) {
                View radioButton = rg1.findViewById( i );
                i1 = rg1.indexOfChild(radioButton);
            }
        });

        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( RadioGroup radioGroup, @IdRes int i ) {
                i4 = i;
            }
        });*/
        pd = new ProgressDialog( context );
        pd.setMessage( "Loading List of Professors !" );
        pd.setIndeterminate( true );

        bt_submit_feedback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                pd.show();

                //int i1, i2, i3, i4, i5;
                i1 = rg1.indexOfChild( rg1.findViewById( rg1.getCheckedRadioButtonId() ) );
                i2 = rg2.indexOfChild( rg2.findViewById( rg2.getCheckedRadioButtonId() ) );
                i3 = rg3.indexOfChild( rg3.findViewById( rg3.getCheckedRadioButtonId() ) );
                i4 = rg4.indexOfChild( rg4.findViewById( rg4.getCheckedRadioButtonId() ) );
                i5 = rg5.indexOfChild( rg5.findViewById( rg5.getCheckedRadioButtonId() ) );

                //Toast.makeText( context, i1+"-"+i2, Toast.LENGTH_LONG ).show();

                final String answers = String.format( "%d,%d,%d,%d,%d", i1+1, i2+1, i3+1, i4+1, i5+1 );

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "submit_faculty_feedback" },
                                { "username" , LoginSession.getUsernameOfLoggedInUser( context ) },
                                { "staff_username" , username },
                                { "current_sem" , current_sem },
                                { "answers" , answers }
                        }));

                        Log.d( TAG, "result : "+result );

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if( result != null )
                                    processResult( result );
                                else{
                                    Toast.makeText( context, "Failed to connect to the Server !", Toast.LENGTH_LONG ).show();
                                    pd.hide();
                                    finish();
                                }
                            }
                        });

                    }
                }).start();
                //Toast.makeText( context, i1 + "-" + i4, Toast.LENGTH_LONG ).show();

            }
        });
    }

    private void processResult( String result ){
        String info = null;
        try{
            JSONArray jsonArray = new JSONArray( result );
            JSONObject jsonObject = jsonArray.getJSONObject( 0 );
            String type = jsonObject.getString( "type" );
            info = jsonObject.getString( "info" );

            if( type.equals( "error" ) ){
                Log.e( TAG, info );
                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                pd.hide();
                finish();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }
}
