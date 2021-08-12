package com.saboo.mlearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class MainActivity extends Activity {

    EditText et_username, et_password;
    Button bt_login;
    TextView tv_register;
    Context context = this;
    final static String TAG = "MainActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        init();

    }

    public void init(){
        if( LoginSession.isLoggedIn( context ) ) {
            redirect();
            finish();
        }

        initViews();
    }

    private void initViews(){
        et_username = (EditText) findViewById( R.id.et_username );
        et_password = (EditText) findViewById( R.id.et_password );
        bt_login = (Button) findViewById( R.id.bt_login );
        tv_register = (TextView) findViewById( R.id.tv_register );

        startRegisterActivity();
        loginButtonListener();

    }

    private void startRegisterActivity(){
        tv_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent in = new Intent( context, RegisterActivity.class );
                startActivity( in );
            }
        });
    }

    String username, password;

    private boolean validateLoginFields(){
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if( username.equals( "" ) || password.equals( "" ) ){
            Toast.makeText( context, "Please fill all the fields before proceeding !",Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }

    String result = "";

    private void loginButtonListener(){
        bt_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                if( ! validateLoginFields() )
                    return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "login_mlearning_user" },
                                { "username" , username },
                                { "password" , password }
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
    }

    private void redirect(){
        String role = LoginSession.getRoleOfLoggedInUser( context );
        if( role.equals( "student" ) ){
            Intent in = new Intent( context, StudentDashboard.class );
            startActivity( in );
        }
        else{
            Intent in = new Intent( context, StaffDashboard.class );
            startActivity( in );
        }
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

                // Decrypt the info to determine if the Logged in User is Staff or Student
                jsonObject = new JSONObject( info );
                jsonArray = jsonObject.getJSONArray( "semester" );
                String role = jsonObject.getString( "role" );

                String s = "";
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    s += jsonArray.getString( i ) + ",";
                }
                s = s.substring( 0, s.length() - 1 );

                Log.d( TAG, String.format( "role : %s, sem : %s", role, s ) );

                // Save the Login Session
                LoginSession.hasLoggedIn( context, username, role, s );

                // Based on Role Name, redirect to appropriate Dashboard
                redirect();

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
