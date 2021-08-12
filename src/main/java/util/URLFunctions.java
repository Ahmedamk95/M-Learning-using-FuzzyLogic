package util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sohail on 14-04-2017.
 */

public class URLFunctions {

    //final static String DOMAIN_NAME = "http://silentcoders.net/";
    final static String DOMAIN_NAME = "http://192.168.1.200/";
    final static String SUB_DOMAIN_NAME = "mlearning";
    final static String WEBSERVICE = "webservice.php";
    final static String TAG = "URLFunctions";

    public static String getWebserviceURLPath(){
        return DOMAIN_NAME + SUB_DOMAIN_NAME + "/" + WEBSERVICE;
    }

    public static String getServerRootPath(){
        return DOMAIN_NAME + SUB_DOMAIN_NAME + "/";
    }

    public static String makeRequestForData(String url, String request_method, String urlParameters){
        StringBuffer response = null;
        String resp = "";

        try{
            URL obj = null;
            HttpURLConnection con = null;

            if( request_method.equals( "GET" ) ){
                String encodedURL = url + "?" + urlParameters;
                //encodedURL = encodedURL.replaceAll("+", "%20");

                obj = new URL( encodedURL );
                con = (HttpURLConnection) obj.openConnection();
                //con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestMethod( "GET" );
                con.setDoOutput( true );
                con.setConnectTimeout( 10000 );
            }
            else{
                String encodedURL = url;//URLEncoder.encode(url, "UTF-8");
                //urlParameters     = URLEncoder.encode(urlParameters, "UTF-8");
                //urlParameters     = urlParameters.replaceAll("+", "%20");

                obj = new URL( encodedURL );
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod( "POST" );
                con.setDoOutput( true );
                con.setConnectTimeout( 10000 );
                DataOutputStream wr = new DataOutputStream( con.getOutputStream() );
                wr.writeBytes( urlParameters );
                wr.flush();
                wr.close();
            }

            int responseCode = con.getResponseCode();
            if( responseCode == 200 ){
                BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
                String inputLine;
                resp = "";
                response = new StringBuffer();
                char buff[] = new char[ 65535 ];

                //FileOutputStream fos = new FileOutputStream( UtilFile.createFileIfNotExist( "Launcher", "temp.txt" ) );

                while ( ( inputLine = in.readLine() ) != null ) {
                    response.append( inputLine );
                }
                //fos.close();
                in.close();
            }
            else{
                throw new Exception( "No Response from server." );
            }
        }
        catch( Exception e ){
            Log.i( TAG, "Exception : "+e.toString() );
            return null;
        }
        return response.toString();



    }



    public static String uploadFile( String url1, String request_method, String urlParameters, final String selectedFilePath ){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);
        String serverResponseMessage = "";
        String resp = "";
        StringBuffer response = null;


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(url1);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
                    String inputLine;
                    resp = "";
                    response = new StringBuffer();
                    char buff[] = new char[ 65535 ];

                    //FileOutputStream fos = new FileOutputStream( UtilFile.createFileIfNotExist( "Launcher", "temp.txt" ) );

                    while ( ( inputLine = in.readLine() ) != null ) {
                        response.append( inputLine );
                    }
                    //fos.close();
                    in.close();
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (Exception e) {
                e.printStackTrace();

            }

            return response.toString();


    }
}
