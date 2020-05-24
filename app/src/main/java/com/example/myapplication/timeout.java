package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class timeout extends AsyncTask <String, Void, String> {

    private TextView textView;

    AlertDialog dialog;
    Context context;
    public timeout(Context context){
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {

        JsonObject phpreturn = new JsonParser().parse(s).getAsJsonObject();
        String pass = phpreturn.getAsJsonObject("timeout").get("verify").getAsString();

        if (pass.equals("true")){
            EventBus.getDefault().post(new VerifyOut("Time-out Clocked"));
            //textView.setText("Time-out recorded");
        }else{
            EventBus.getDefault().post(new VerifyOutProblem("Time-out clocking Error"));

            //textView.setTextColor(Color.RED);
            //textView.setText("Time-out failure");
        }

    }

    @Override
    protected String doInBackground(String... voids) {

        String result = "";
        String id = voids[0];

        String connstr = "http://10.0.0.12/showtime/timeout.php";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id, "UTF-8");

            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();


            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));
            String line = "";
            while ((line = reader.readLine()) != null){
                result += line;
            }
            reader.close();
            ips.close();
            http.disconnect();

            return result;


        } catch (MalformedURLException e) {
            result = e.getMessage();
        } catch (IOException e) {
            result = e.getMessage();
        }

        return result;
    }

    public class VerifyOut{
        private String text;

        public VerifyOut(String text){
            this.text=text;
        }
        public String getText(){
            return text;
        }
        public void setText(String text){
            this.text=text;
        }
    }

    public class VerifyOutProblem{
        private String text;

        public VerifyOutProblem(String text){
            this.text=text;
        }
        public String getText(){
            return text;
        }
        public void setText(String text){
            this.text=text;
        }
    }

}