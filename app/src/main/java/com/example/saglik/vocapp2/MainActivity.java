package com.example.saglik.vocapp2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.saglik.vocapp2.Record.databaseSize;
import static com.example.saglik.vocapp2.Record.getAll;
import static com.example.saglik.vocapp2.Record.getByInitial;

public class MainActivity extends AppCompatActivity {

    //Permission checker
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE
    };

    ListView alphabetListView;
    ListView recordListView;
    Button saveButton;
    PopupMenu popupMenu;
    Button quizButton;
    Button exportButton;
    Button importButton;
    EditText editTextFr;
    EditText editTextEn;
    private int letterPosition;

    //Speech Elements for French Pronunciation
    TextToSpeech toSpeech;
    int result;

    //letters will be assigned to alphabetListView
    static final String[] letters = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", " I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "\u2200"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);
        checkPermissions();


        alphabetListView = findViewById(R.id.alphabetListView);
        recordListView = findViewById(R.id.recordListView);
        saveButton = findViewById(R.id.saveButton);
        quizButton = findViewById(R.id.quizButton);
        exportButton = findViewById(R.id.exportButton);
        importButton = findViewById(R.id.importButton);
        editTextFr = findViewById(R.id.editTextFr);
        editTextEn = findViewById(R.id.editTextEn);

        //Assign letters to a  sliding list
        ArrayAdapter<String> alphabetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, letters);

        refreshList(26);

        alphabetListView.setAdapter(alphabetAdapter);

        alphabetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                letterPosition = position;
                refreshList(position);
                //Show which letter is clicked
                if(position==26)
                    Toast.makeText(getApplicationContext(), "ALL RECORDS", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        //Create popup menu on record item click
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                //Listener for popup menu items. Must be placed before popupMenu.show()
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Delete clicked record
                        String recordText = (String) recordListView.getItemAtPosition(position);
                        String recordTextFr  = "";
                        String recordTextEn = "";
                        for(int i=0; i<recordText.length();i++){
                            if(recordText.substring(i,i+5).equals(" <-> ")) {
                                recordTextEn = recordText.substring(0, i);
                                recordTextFr = recordText.substring(i+5);
                                break;
                            }
                        }
                        if(item.getTitle().equals("Delete")||item.getTitle().equals("Update")){
                            //Remove deleted record form recordListView as well
                            new Delete().from(Record.class).where("French = ?", recordTextFr).execute();
             //Crash        Toast.makeText(getApplicationContext(), item.getTitle()+"d", Toast.LENGTH_SHORT).show();
                            if(item.getTitle().equals("Update")) {
                                editTextFr.setText(recordTextFr);
                                editTextEn.setText(recordTextEn);
                                Snackbar.make(view, "RECORD DELETED! PRESS SAVE TO UNDO", Snackbar.LENGTH_SHORT).setAction("", null).show();
                            }
                            else {
                                Snackbar.make(view, "RECORD DELETED!", Snackbar.LENGTH_SHORT).setAction("", null).show();
                            }
                            refreshList(letterPosition);
                        }
                        else{
                            final String finalRecordTextFr = recordTextFr;
                            toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener(){
                                @Override
                                public void onInit(int status) {
                                    if(status == TextToSpeech.SUCCESS){
                                        result = toSpeech.setLanguage(Locale.FRENCH);
                                        toSpeech.speak(finalRecordTextFr,TextToSpeech.QUEUE_FLUSH,null, null);
                                        //toSpeech.shutdown();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Feature is not supported for this device!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Save non-empty entries
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                String saved = "SAVED";
                EditText fr = findViewById(R.id.editTextFr);
                EditText en = findViewById(R.id.editTextEn);
                String english = en.getText().toString().toLowerCase();
                String french = fr.getText().toString().toLowerCase();

                //Empty record check
                if(english.equals("") || french.equals("")){
                    String emptyWarning = "Please fill both fields!";
                    Toast.makeText(getApplicationContext(), emptyWarning, Toast.LENGTH_SHORT).show();
                }
                //Duplicate record check
                else if(duplicateCheck(french, english)){
                    String duplicateWarning = "Record Already Exists!";
                    Toast.makeText(getApplicationContext(), duplicateWarning, Toast.LENGTH_SHORT).show();
                }
                //Save and display new record
                else{
                    String initial = String.valueOf(english.charAt(0)).toUpperCase();
                    TextView display = findViewById(R.id.display);
                    String newRecord = french+"\n"+english;
                    //Clean editviews after adding
                    fr.getText().clear();
                    en.getText().clear();

                    //Add new record to database
                    Record record = new Record();
                    record.initial = initial.toUpperCase();
                    record.english = english;
                    record.french = french;
                    record.save();
                    display.setText(newRecord);
                    int i;
                    //List filtered with saved items capital
                    for(i=0;i<26;i++){
                        if(letters[i].equals(record.initial))
                            break;
                    }
                    refreshList(i);
                    Toast.makeText(getApplicationContext(), saved, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Start QUIZ
        quizButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (databaseSize() < 30) {
                    Toast.makeText(getApplicationContext(), "At least 30 records are required to take a quiz!", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
            }
        });

        importButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                importWords();
                refreshList(26);
            }
        });

        //Export
        exportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                exportDB();
            }
        });
    }

    //Returns true if duplicate, false otherwise
    private boolean duplicateCheck(String fr, String en) {
        List<Record> allRecords = getAll();
        for(Record record : allRecords){
            if(record.french.toLowerCase().equals(fr.toLowerCase()) && record.english.toLowerCase().equals(en.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    //Update the displayed list
    public void refreshList(int position){
        List<Record> allRecords;
        allRecords = getAll();
        //Create the string array to be displayed by recordsListView
        List<String> wordsByLetter = new ArrayList<>();
        if(position!=26) {
            for (Record record : allRecords) {
                if (record.initial.equals(letters[position]))
                    wordsByLetter.add(record.english + " <-> " + record.french);
            }
        }
        else{
            for(Record record : allRecords)
                wordsByLetter.add(record.english + " <-> " + record.french);
        }
        //Display updated recordListView with the adapter
        ArrayAdapter<String> wordAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, wordsByLetter);
        recordListView.setAdapter(wordAdapter);
    }

    //import words
    private void importWords() {
        //From raw directory read
  /*    InputStream is = getResources().openRawResource(R.raw.input);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line;
        try {
            while((line = reader.readLine()) != null){
                //Split by ","
                String[] tokens = line.split(",");
                //Read
                Record sample = new Record();
                sample.initial = String.valueOf(tokens[0].charAt(0)).toUpperCase();
                sample.french = tokens[0];
                sample.english = tokens[1];
                sample.save();
            }
        } catch (IOException e) {
            Log.wtf("My activity", "Error in importing words");
            e.printStackTrace();
        }
   */
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"input.txt");
        String line;
        if(file.exists()){
            try{
            //  BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));
                while((line = reader.readLine()) != null){
                    //Split by ","
                    if(line.charAt(0)==',')
                        line = "n/a"+line;
                    else if(line.charAt(line.length()-1)==',')
                        line = line+"n/a";
                    String[] tokens = line.split(",");
                    //Read
                    Record sample = new Record();
                    sample.initial = String.valueOf(tokens[0].charAt(0)).toUpperCase();
                    sample.english = tokens[0];
                    sample.french = tokens[1];
                    if (!(duplicateCheck(tokens[0], tokens[1])))
                        sample.save();
                }
                Toast.makeText(getApplicationContext(), "Data is imported from 'Files', duplicate records are disregarded!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "File not found!", Toast.LENGTH_SHORT).show();
                Log.wtf("My activity", "Error in importing words!");
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "File not found!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("File not found!");
        }
    }

    //Export database to file method
    private void exportDB() {
        String newLine = "\n";
        List<Record> allRecords = getAll();
        List<String[]> data = new ArrayList<>();
        for(Record r: allRecords){
            data.add(new String[]{r.english,r.french});
        }
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File Root = Environment.getExternalStorageDirectory();
            Log.d("Export to", Root.getAbsolutePath());
            File Dir = new File(Root.getAbsolutePath()+"/Download");
            File file = new File(Dir,"output.txt");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //Export records
                for(String[] d : data){
                    fos.write(d[0].getBytes());
                    fos.write(",".getBytes());
                    fos.write(d[1].getBytes());
                    fos.write(newLine.getBytes());
                }
                fos.close();
                Toast.makeText(getApplicationContext(), "Data exported to Files/Download", Toast.LENGTH_SHORT).show();
            } catch (IOException e){
                Log.wtf("My activity", "Error in exporting words");
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"External Storage Not Available!",Toast.LENGTH_LONG).show();
        }
    }

    //Check if permission is granted and get if not
    private void checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
        }
    }
}
