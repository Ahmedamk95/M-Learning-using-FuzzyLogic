package com.saboo.mlearning;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

import util.URLFunctions;

public class RegisterActivity extends Activity {

    EditText et_username, et_email, et_password1, et_password2;
    Button bt_register;
    TextView tv_already_registered;
    Spinner sp_role, sp_sem;
    Context context = this;
    LinearLayout ll_student, ll_staff;
    CheckBox cb_sem3, cb_sem4, cb_sem5, cb_sem6, cb_sem7, cb_sem8;
    final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        init();
    }

    private void init(){
        initViews();
    }

    private void initViews(){
        et_username = (EditText) findViewById( R.id.et_username);
        et_email = (EditText) findViewById( R.id.et_email );
        et_password1 = (EditText) findViewById( R.id.et_password1 );
        et_password2 = (EditText) findViewById( R.id.et_password2 );
        bt_register = (Button) findViewById( R.id.bt_register );
        tv_already_registered = (TextView) findViewById( R.id.tv_already_registered );
        sp_role = (Spinner) findViewById( R.id.sp_role);
        sp_sem = (Spinner) findViewById( R.id.sp_sem );
        ll_student = (LinearLayout) findViewById( R.id.ll_student );
        ll_staff = (LinearLayout) findViewById( R.id.ll_staff );
        cb_sem3 = (CheckBox) findViewById( R.id.cb_sem3 );
        cb_sem4 = (CheckBox) findViewById( R.id.cb_sem4 );
        cb_sem5 = (CheckBox) findViewById( R.id.cb_sem5 );
        cb_sem6 = (CheckBox) findViewById( R.id.cb_sem6 );
        cb_sem7 = (CheckBox) findViewById( R.id.cb_sem7 );
        cb_sem8 = (CheckBox) findViewById( R.id.cb_sem8 );

        startLoginActivity();
        fillSpinnerData();
        registerButtonListener();
    }

    private void startLoginActivity(){
        tv_already_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent in = new Intent( context, MainActivity.class );
                startActivity( in );
            }
        });
    }

    private void fillSpinnerData(){
        String[] who_are_you = new String[]{ "staff", "student" };
        ArrayAdapter<String> spr = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, who_are_you );
        sp_role.setAdapter( spr );

        sp_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( sp_role.getSelectedItem().equals( "student" ) ){
                    ll_student.setVisibility( View.VISIBLE );
                    ll_staff.setVisibility( View.GONE );
                }
                else{
                    ll_student.setVisibility( View.GONE );
                    ll_staff.setVisibility( View.VISIBLE );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] semesters = new String[]{ "sem3", "sem4", "sem5", "sem6", "sem7", "sem8" };
        ArrayAdapter<String> sps = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, semesters );
        sp_sem.setAdapter( sps );

    }

    String role, username, password1, password2, email, semester;

    private boolean validateRegistrationFields(){
        role = sp_role.getSelectedItem().toString();
        username = et_username.getText().toString();
        password1 = et_password1.getText().toString();
        password2 = et_password2.getText().toString();
        email = et_email.getText().toString();
        if( role.equals( "student" ) )
            semester = sp_sem.getSelectedItem().toString();
        else{
            Vector<String> v = new Vector<String>();

            if( cb_sem3.isChecked() )
                v.add( cb_sem3.getText().toString() );
             if( cb_sem4.isChecked() )
                v.add( cb_sem4.getText().toString() );
             if( cb_sem5.isChecked() )
                v.add( cb_sem5.getText().toString() );
             if( cb_sem6.isChecked() )
                v.add( cb_sem6.getText().toString() );
             if( cb_sem7.isChecked() )
                v.add( cb_sem7.getText().toString() );
             if( cb_sem8.isChecked() )
                v.add( cb_sem8.getText().toString() );

            //String temp[] = new String[ v.size() ];
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for( int i = 0 ; i < v.size() ; i++ ){
                // temp[ i ] = v.get( i );
                jsonArray.put( v.get( i ) );
            }
            semester = jsonArray.toString();
            Log.d( TAG, "semester : "+semester );
        }

        if( username.equals( "" ) ){
            Toast.makeText( context, "Roll Number / Username cannot be empty",Toast.LENGTH_LONG ).show();
            return false;
        }
        else if( password1.equals( "" ) ){
            Toast.makeText( context, "Password cannot be empty", Toast.LENGTH_LONG ).show();
            return false;
        }
        else if( password2.equals( "" ) ){
            Toast.makeText( context, "Re-Type Password cannot be empty", Toast.LENGTH_LONG ).show();
            return false;
        }
        else if( ! password1.equals( password2 ) ){
            Toast.makeText( context, "Password and Re-Type passwords do not match", Toast.LENGTH_LONG ).show();
            return false;
        }
        else if( email.equals( "" ) ){
            Toast.makeText( context, "Email cannot be empty", Toast.LENGTH_LONG ).show();
            return false;
        }
        return true;
    }

    String result = "";
    Handler handler;
    private void registerButtonListener(){
        //handler = new Handler( Looper.getMainLooper() );

        bt_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                if( ! validateRegistrationFields() )
                    return;



                AlertDialog.Builder ab = new AlertDialog.Builder( context );
                ab.setTitle( "Confirmation" );
                String msg = "";
                if( role.equals( "student" ) ){
                    msg = "Are you a Student currently in Semester "+semester+" ? Your semester cannot be changed once chosen !";
                }
                else{
                    msg = "Are you sure you are a Staff ?";
                }
                ab.setMessage( msg );
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                        { "what_do_you_want" , "register_mlearning_user" },
                                        { "role" , role },
                                        { "username" , username },
                                        { "password" , password1 },
                                        { "email" , email },
                                        { "semester" , semester }

                                } ) );
                                Log.d( TAG, "result : "+result );

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if( result != null )
                                            processResult( result );
                                        else
                                            Toast.makeText( context, "Failed to connect to the Server !", Toast.LENGTH_LONG ).show();
                                    }
                                });

                            }
                        }).start();


                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {

                    }
                });
                ab.show();

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
                Intent in = new Intent( context, MainActivity.class );
                startActivity( in );
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
