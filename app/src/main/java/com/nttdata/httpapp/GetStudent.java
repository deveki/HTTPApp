package com.nttdata.httpapp;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GetStudent extends AppCompatActivity {

    EditText etResponse;
    EditText edtEnterId;
    Button btnGetStudent;
    TextView txtEnterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student);

        txtEnterId = (TextView) findViewById(R.id.txtEnterId);
        edtEnterId = (EditText) findViewById(R.id.edtEnterId);
        btnGetStudent = (Button) findViewById(R.id.btnGetStudent);
        etResponse = (EditText) findViewById(R.id.etResponse);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
                btnGetStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isConnected()) {
                    String url = "http://172.26.56.25:8081/springJson/students";
                    new HttpAsyncTask().execute(url);
                } else {
                    Toast.makeText(getBaseContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Student> {
        @Override
        protected Student doInBackground(String... urls) {
            return post(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Student student) {
            if (student == null) {
                etResponse.setText("");
                Toast.makeText(getBaseContext(), "No record found", Toast.LENGTH_LONG).show();
            } else {
                etResponse.setText(student.getName());
            }
        }

        public Student post(String url){
            Student student = null;
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Student stud = new Student();
                stud.setId((Integer.parseInt(edtEnterId.getText().toString().trim())));
                student = restTemplate.postForObject(url, stud, Student.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return student;
        }
    }
}
