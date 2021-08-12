package com.saboo.mlearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class CreateQuizActivity extends AppCompatActivity {

    final static String TAG = "CreateQuizActivity";
    Context context = this;
    Spinner sp_sem;
    ProgressDialog pd;
    String result = "";
    String selected_sem = "sem3";
    EditText et_title;
    LinearLayout ll_quiz_content;
    Button bt_create_quiz;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_quiz );

        init();

    }

    private void init(){
        initViews();

        spinnerItemSelected();
        generateQuizQuestions();

        createQuizButtonListener();
    }

    private void initViews(){
        sp_sem = (Spinner) findViewById( R.id.sp_sem );
        et_title = (EditText) findViewById( R.id.et_title );
        ll_quiz_content = (LinearLayout) findViewById( R.id.ll_quiz_content );
        bt_create_quiz = (Button) findViewById( R.id.bt_create_quiz );
    }

    private void spinnerItemSelected(){
        String[] semesters = new String[]{ "sem3", "sem4", "sem5", "sem6", "sem7", "sem8" };
        ArrayAdapter<String> sps = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, semesters );
        sp_sem.setAdapter( sps );

        sp_sem.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l ) {
                selected_sem = sp_sem.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected( AdapterView<?> adapterView ) {

            }
        });
    }

    private void generateQuizQuestions(){
        for( int i = 1 ; i <= 5 ; i++ ){
            LinearLayout ll = (LinearLayout) View.inflate( context, R.layout.quiz_question, null );
            TextView tv_question_no = (TextView) ll.findViewById( R.id.tv_question_no );
            tv_question_no.setText( "Q" + i );
            EditText et_quiz_question = (EditText) ll.findViewById( R.id.et_quiz_question );
            et_quiz_question.setText( et_quiz_question.getText() + " " +i );

            ll_quiz_content.addView( ll );
        }
    }
    JSONArray jsonArray;
    private void createQuizButtonListener(){
        bt_create_quiz.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                // valdate if all the fields have been filled or not
                if( et_title.getText().toString().trim().equals( "" ) ){
                    Toast.makeText( context, "Please give a Title to the Quiz !", Toast.LENGTH_LONG ).show();
                    return;
                }

                for( int i = 0 ; i < 5 ; i++ ){
                    LinearLayout ll = (LinearLayout) ll_quiz_content.getChildAt( i );
                    EditText et_quiz_question = (EditText) ll.findViewById( R.id.et_quiz_question );
                    EditText et_op1 = (EditText) ll.findViewById( R.id.et_op1 );
                    EditText et_op2 = (EditText) ll.findViewById( R.id.et_op2 );
                    EditText et_op3 = (EditText) ll.findViewById( R.id.et_op3 );
                    EditText et_op4 = (EditText) ll.findViewById( R.id.et_op4 );

                    if( et_quiz_question.getText().toString().trim().equals( "" ) ||
                            et_op1.getText().toString().trim().equals( "" ) ||
                            et_op2.getText().toString().trim().equals( "" ) ||
                            et_op3.getText().toString().trim().equals( "" ) ||
                            et_op4.getText().toString().trim().equals( "" )
                            ){
                        Toast.makeText( context, "Please fill all the fields before submitting !", Toast.LENGTH_LONG ).show();
                        return;
                    }
                }

                // Put the contents of the quiz into JSON
                jsonArray = new JSONArray();
                JSONObject jsonObject = null;
                for( int i = 0 ; i < 5 ; i++ ){
                    LinearLayout ll = (LinearLayout) ll_quiz_content.getChildAt( i );
                    EditText et_quiz_question = (EditText) ll.findViewById( R.id.et_quiz_question );
                    EditText et_op1 = (EditText) ll.findViewById( R.id.et_op1 );
                    EditText et_op2 = (EditText) ll.findViewById( R.id.et_op2 );
                    EditText et_op3 = (EditText) ll.findViewById( R.id.et_op3 );
                    EditText et_op4 = (EditText) ll.findViewById( R.id.et_op4 );
                    RadioGroup rg_answer = (RadioGroup) ll.findViewById( R.id.rg_answer );

                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put( "question", et_quiz_question.getText().toString() );
                        jsonObject.put( "op1", et_op1.getText().toString() );
                        jsonObject.put( "op2", et_op2.getText().toString() );
                        jsonObject.put( "op3", et_op3.getText().toString() );
                        jsonObject.put( "op4", et_op4.getText().toString() );
                        jsonObject.put( "answer", ll.findViewById( rg_answer.getCheckedRadioButtonId() ).getTag().toString() );

                        jsonArray.put( i, jsonObject );

                    } catch ( JSONException e ) {
                        e.printStackTrace();
                    }

                }

                /*jsonObject = new JSONObject();
                try {
                    jsonObject.put( "sem", selected_sem );
                    jsonObject.put( "quiz_title", et_title.getText().toString() );
                    jsonArray.put( jsonObject );

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                Log.d( TAG, jsonArray.toString() );

                pd = new ProgressDialog( context );
                pd.setMessage( "Creating Quiz !" );
                pd.setIndeterminate( true );

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "create_quiz" },
                                { "username" , LoginSession.getUsernameOfLoggedInUser( context ) },
                                { "sem" , selected_sem },
                                { "quiz_title" , et_title.getText().toString() },
                                { "quiz_content" , jsonArray.toString() }
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
                finish();
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
