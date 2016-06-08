package com.project.nicky.medicalmanager.Startup;

import android.content.Context;
import android.os.AsyncTask;

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

public class BackgroundTask extends AsyncTask<String, Void, String> {

    public BackgroundResponse backgroundResponse = null;
    private Context context;
    String username, password, userType, address;

    public BackgroundTask(Context context){
            this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String uri = "";
        String[] keys = new String[params.length];
        switch(type){
            case "login": uri = "http://medicalmanager.x10host.com/database/login.php";
                keys[1] = "username"; keys[2] = "password";
                break;
            case "registerPatient": uri = "http://medicalmanager.x10host.com/database/register_patient.php";
                keys[1] = "firstName"; keys[2] = "lastName"; keys[3] = "username"; keys[4] = "password";
                keys[5] = "addressLine"; keys[6] = "city"; keys[7] = "county"; keys[8] = "country";
                address = params[5] + ", " + params[6] + ", " + params[7] + ", " + params[8];
                break;
            case "registerDoctor": uri = "http://medicalmanager.x10host.com/database/register_doctor.php";
                keys[1] = "firstName"; keys[2] = "lastName"; keys[3] = "username"; keys[4] = "password";
                keys[5] = "background"; keys[6] = "qualifications"; keys[7] = "experience";
                keys[8] = "addressLine"; keys[9] = "city"; keys[10] = "county"; keys[11] = "country";
                username = params[3]; password = params[4]; userType = "doctor";
                address = params[8] + ", " + params[9] + ", " + params[10] + ", " + params[11];
                break;
            case "getUserInformation": uri = "http://medicalmanager.x10host.com/database/get_user_information.php";
                keys[1] = "username";
                break;
            case "search": uri = "http://medicalmanager.x10host.com/database/search.php";
                keys[1] = "searchQuery";
                break;
            case "addMedication": uri = "http://medicalmanager.x10host.com/database/add_medication.php";
                keys[1] = "username"; keys[2] = "name"; keys[3] = "startDate"; keys[4] = "time";
                keys[5] = "duration";
                break;
            case "getDoctors": uri = "http://medicalmanager.x10host.com/database/get_doctors.php";
                break;
            case "getProfile": uri = "http://medicalmanager.x10host.com/database/get_doctor.php";
                keys[1] = "profileId";
                break;
            case "addContact": uri = "http://medicalmanager.x10host.com/database/add_contact.php";
                keys[1] = "profileId"; keys[2] = "loggedInUsername";
                break;
            case "addAppointments": uri = "http://medicalmanager.x10host.com/database/add_appointments.php";
                keys[1] = "username"; keys[2] = "name"; keys[3] = "startDate"; keys[4] = "startTime";
                keys[5] = "minutesDuration"; keys[6] = "amount"; keys[7] = "daysDuration";
                break;
            case "getAppointments": uri = "http://medicalmanager.x10host.com/database/get_appointments.php";
                keys[1] = "date"; keys[2] = "doctorId";
                break;
            case "bookAppointment": uri = "http://medicalmanager.x10host.com/database/book_appointment.php";
                keys[1] = "appointmentId"; keys[2] = "userId";
                break;
            case "getEvents": uri = "http://medicalmanager.x10host.com/database/get_events.php";
                keys[1] = "username"; keys[2] = "userType"; keys[3] = "date";
                break;
            case "getContacts": uri = "http://medicalmanager.x10host.com/database/get_contacts.php";
                keys[1] = "loggedInUsername"; keys[2] = "userType";
                break;
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = "";
            if(params.length > 1) {
                for (int i = 1; i < params.length; i++) {
                    data += URLEncoder.encode(keys[i], "UTF-8") + "=" + URLEncoder.encode(params[i], "UTF-8") + "&";
                }
            }
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String response = "";
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                response += line;
            }
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
            backgroundResponse.processResponse(result);
    }
}
