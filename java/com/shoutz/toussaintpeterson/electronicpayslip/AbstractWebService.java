package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by toussaintpeterson on 1/16/16.
 */
public abstract class AbstractWebService extends AsyncTask<Void,Void,Object> {
    private boolean secureSSO;
    private boolean secureAPI;
    protected String urlPath;
    private static String baseURL;
    protected Context context;
    private Map<String,String> queryParams;
    private static final String ACCEPT_GZIP = "gzip";
    private static final String USER_AGENT = "Mozilla/5.0";
    private URL testURL;
    private static final String GET_URL = "http://localhost:9090/SpringMVCExample";
    private static final String POST_PARAMS = "userName=Pankaj";
    private static final String POST_URL = "http://dev-eps.royalfortunes.com/pingtest.php";
    protected abstract void onSuccess(Object Response);
    protected abstract void onFinish();
    protected abstract void onError(Object response);
    protected abstract Object doWebOperation() throws Exception;


    public AbstractWebService(String urlPath, boolean secureSSO, boolean secureAPI, Context context){
        this.urlPath = urlPath;
        this.secureSSO = secureSSO;
        this.secureAPI = secureAPI;
        this.context = context;
    }

    private static void doGetTest() throws IOException{
        URL test = new URL(GET_URL);
        HttpURLConnection connection = (HttpURLConnection) test.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", ACCEPT_GZIP);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){

        }

    }

    public JSONObject doPost(JSONObject json) throws IOException, JSONException{
        return doInputRequest(getBaseURL(), json);
    }

   public JSONObject doPost(JSONObject object, Map<String, String> params) throws IOException, JSONException{
       StringBuilder str = new StringBuilder();
       JSONObject answer = null;

       URL obj = new URL("https://test-api-eps.azurewebsites.net:80/api/user/register");
       HttpURLConnection con = (HttpURLConnection)obj.openConnection();
       con.setRequestProperty("Accept", "application/json");
       con.setRequestProperty("Accept-Encoding", ACCEPT_GZIP);
       con.setRequestProperty("Content-type", "application/json");
       con.setReadTimeout(10000);
       con.setRequestMethod("POST");
       con.setConnectTimeout(15000);
       con.setDoInput(true);
       con.setDoOutput(true);

       if(object != null){
           String requestString = object.toString();

           OutputStream os = new BufferedOutputStream(con.getOutputStream());
           OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
           osw.write(requestString);
           osw.flush();
       }

       //params = new HashMap<String, String>();

/*       if(params != null && params.size()>0) {
           boolean first = true;

           str.append("?");
           for (Map.Entry<String, String> entry : params.entrySet()) {
               if(!first) {
                   str.append("&");
               }
               str.append(entry.getKey());
               str.append("=");
               str.append(entry.getValue());
               first = false;
           }
       }*/
       /*Uri.Builder builder = new Uri.Builder()
               .appendQueryParameter("gameid", "1")
               .appendQueryParameter("betamount", "World")
               .appendQueryParameter("numbers", "1|2|3|4");
       String query = builder.build().getEncodedQuery();

       OutputStream os = con.getOutputStream();
       BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
       writer.write(query);
       writer.flush();
       writer.close();
       os.close();*/

//       con.connect();

       int responseCode = con.getResponseCode();

       if(responseCode == HttpURLConnection.HTTP_OK){
           BufferedReader in = new BufferedReader(new InputStreamReader(
                   con.getInputStream()));
           String inputLine;
           StringBuffer response = new StringBuffer();

           while ((inputLine = in.readLine()) != null) {
               response.append(inputLine);
           }
           in.close();

           answer = new JSONObject(response.toString());
           // print result
           System.out.println(response.toString());
       } else {
           System.out.println("POST request failed");
       }
        con.disconnect();
        return answer;
   }

    public JSONObject doGet(JSONObject object, Map<String, String> params) throws IOException, JSONException {
        //StringBuilder str = new StringBuilder();
        //str.append("http://dev-eps.royalfortunes.com/pingtest.php?param1=Hi&param2=World&param3=Bob");
        JSONObject answer = null;

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(getBaseURL());

            if(params != null){
                    StringBuilder builder = new StringBuilder();
                    Iterator myVeryOwnIterator =  params.values().iterator();

                    if(getBaseURL().contains(".php") || getBaseURL().contains("google")) {
                        while(myVeryOwnIterator.hasNext()) {
                            builder.append(myVeryOwnIterator.next());

                            if (myVeryOwnIterator.hasNext()) {
                                builder.append("&");
                            }
                        }
                        url = new URL(url + builder.toString());
                    }
                else {
                        while (myVeryOwnIterator.hasNext()) {
                            builder.append(myVeryOwnIterator.next());

                            if (myVeryOwnIterator.hasNext()) {
                                builder.append(",");
                            }
                        }
                        url = new URL(url + builder.toString());
                    }
            }

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setConnectTimeout(5000);

;            int test = urlConnection.getResponseCode();
            String message = urlConnection.getResponseMessage();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                answer = new JSONObject(response.toString());
                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("POST request failed");
            }
            urlConnection.disconnect();
        }
        catch (java.net.SocketTimeoutException e) {
            Toast.makeText(context, "Connection Timeout", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            if(!returnedError(result)) {
                onSuccess(result);
            }
            else {
                onError(result);
            }
        }
        catch (Exception ex) {
            onError(result);
        }
        finally {
            //nothing for now
        }
    }

    private boolean returnedError(Object result) throws JSONException {
        return (result == null || (result instanceof JSONObject && ((JSONObject)result).has("status") && ((JSONObject)result).get("status").equals("Error")) || (result instanceof JSONObject && ((JSONObject)result).has("description") && ((JSONObject)result).get("description").equals("Error")));
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            return doWebOperation();
        }
        catch(Exception e) {
            Log.e(AbstractWebService.class.getName(), "Unable to call webservice", e);
        }

        return null;
    }

    private String getBaseURL(){
        boolean devMode = Boolean.parseBoolean(context.getResources().getString(R.string.dev_mode));
        boolean demoMode = Boolean.parseBoolean(context.getResources().getString(R.string.demo_mode));
        boolean tribal = Boolean.parseBoolean(context.getResources().getString(R.string.tribal_mode));

        if(urlPath != null && urlPath.contains("http")){
            return urlPath;
        }

        StringBuilder str = new StringBuilder();
        if(devMode){
            str.append(context.getResources().getString(((secureAPI || secureSSO) ? R.string.dev_protocol: R.string.prod_protocol)));
            str.append(((secureSSO)? context.getResources().getString(R.string.dev_test_shoutz) :context.getResources().getString(R.string.dev_secure_url)));
        }
        else if(demoMode){
            str.append("http://");
            str.append(((secureSSO)? context.getResources().getString(R.string.demo_view) :context.getResources().getString(R.string.demo_url)));
        }
        else if(tribal){
            str.append("http://");
            str.append(((secureSSO)? context.getResources().getString(R.string.tribal_view) :context.getResources().getString(R.string.tribal_url)));
        }
        else{
            str.append(((secureAPI || secureSSO)? R.string.prod_protocol: R.string.dev_protocol));
            str.append(((secureSSO)? R.string.prod_secure_url: R.string.prod_url));
        }

        baseURL = str.toString()+urlPath;
        //baseURL = "http://test-api-eps.azurewebsites.net/api/v1/tickets/submit";
        //String url = ((secureSSO)? R.string.dev_secure_url: R.string.dev_test_shoutz)+urlPath;
        //String full = "http://test-api-eps.azurewebsites.net"+urlPath;

        return baseURL;
    }

    private JSONObject doInputRequest(String request , JSONObject json) throws IOException, JSONException{
        JSONObject answer;

        String url = getBaseURL();

        HttpURLConnection con = (HttpURLConnection)new URL(getBaseURL()).openConnection();
        try {
            //con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            //con.setRequestProperty("Accept-Encoding", ACCEPT_GZIP);
            con.setRequestProperty("Content-type", "application/json");
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setUseCaches(false);
            //con.connect();
            if(json != null){
                String requestString = json.toString();

                OutputStream os = new BufferedOutputStream(con.getOutputStream());
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(requestString);
                osw.flush();
            }

            int test = con.getResponseCode();
            String message = con.getResponseMessage();

            InputStream is = con.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseStr = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStr.append(inputStr);
            }
            answer = new JSONObject(responseStr.toString());
        }

            finally {
                con.disconnect();
            }

        return answer;
    }
}
