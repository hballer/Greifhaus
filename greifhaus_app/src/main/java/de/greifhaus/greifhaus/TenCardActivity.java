package de.greifhaus.greifhaus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class TenCardActivity extends Activity {

    private static final String TAG = TenCardActivity.class.getSimpleName();
    private static final String FILENAME = TAG+ ".txt";

    private Button visitButton;
    private Button newCardButton;
    private Button cancleButton;
    private Button saveButton;

    private Switch switchVisit01;
    private Switch switchVisit02;
    private Switch switchVisit03;
    private Switch switchVisit04;
    private Switch switchVisit05;
    private Switch switchVisit06;
    private Switch switchVisit07;
    private Switch switchVisit08;
    private Switch switchVisit09;
    private Switch switchVisit10;
    private ProgressBar progress;
    private TextView textName;

    private boolean editMode = false;

    private String userName = "";

    private String[] visitsArray;
    private String[] visitsArrayBackup;
    private int numberOfVisits=0;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void readUserNameWrapper() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        readUserName();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults != null) {
                    if (grantResults.length>0) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // Permission Granted
                            System.out.println("Habe die Erlaubnis!");
                            readUserName();
                        } else {
                            // Permission Denied
                            //Toast.makeText(MainActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void readUserName() {
        Cursor c = this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        String[] columnNames = c.getColumnNames();
        boolean b = c.moveToFirst();
        int position = c.getPosition();
        String display_name="";
        if (count == 1 && position == 0) {
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                String columnValue = c.getString(c.getColumnIndex(columnName));
                // consume the values here
                if (columnName.equals("display_name")) {
                    display_name = columnValue;
                }
            }
        }
        userName = display_name;
        textName.setText(userName);
        System.out.println("Ende");
        c.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_card);

        visitButton = (Button) findViewById(R.id.buttonVisit);
        newCardButton = (Button) findViewById(R.id.buttonNewCard);

        cancleButton = (Button) findViewById(R.id.buttonCancel);
        saveButton = (Button) findViewById(R.id.buttonSave);

        progress = (ProgressBar) findViewById(R.id.progressBarVisits);

        switchVisit01 = (Switch) findViewById(R.id.switchVisit01);
        switchVisit02 = (Switch) findViewById(R.id.switchVisit02);
        switchVisit03 = (Switch) findViewById(R.id.switchVisit03);
        switchVisit04 = (Switch) findViewById(R.id.switchVisit04);
        switchVisit05 = (Switch) findViewById(R.id.switchVisit05);
        switchVisit06 = (Switch) findViewById(R.id.switchVisit06);
        switchVisit07 = (Switch) findViewById(R.id.switchVisit07);
        switchVisit08 = (Switch) findViewById(R.id.switchVisit08);
        switchVisit09 = (Switch) findViewById(R.id.switchVisit09);
        switchVisit10 = (Switch) findViewById(R.id.switchVisit10);

        textName = (TextView) findViewById(R.id.textViewName);

        readUserNameWrapper();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

        visitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewVisit();
                writeToFile();
                updateVisits();
            }
        });

        newCardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearVisit();
                writeToFile();
                updateVisits();
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i< visitsArray.length;i++) {
                    visitsArray[i] = visitsArrayBackup[i];
                }
                leaveEditMode();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile();
                leaveEditMode();
            }
        });

        switchVisit01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit01.isChecked()) {
                    if (!visitsArrayBackup[0].equals("none")) {
                        visitsArray[0] = visitsArrayBackup[0];
                    } else {
                        visitsArray[0] = dasDatum();
                    }
                } else {
                    visitsArray[0] = "none";
                }
                updateVisits();
            }
        });

        switchVisit02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit02.isChecked()) {
                    if (!visitsArrayBackup[1].equals("none")) {
                        visitsArray[1] = visitsArrayBackup[1];
                    } else {
                        visitsArray[1] = dasDatum();
                    }
                } else {
                    visitsArray[1] = "none";
                }
                updateVisits();
            }
        });

        switchVisit03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit03.isChecked()) {
                    if (!visitsArrayBackup[2].equals("none")) {
                        visitsArray[2] = visitsArrayBackup[2];
                    } else {
                        visitsArray[2] = dasDatum();
                    }
                } else {
                    visitsArray[2] = "none";
                }
                updateVisits();
            }
        });

        switchVisit04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit04.isChecked()) {
                    if (!visitsArrayBackup[3].equals("none")) {
                        visitsArray[3] = visitsArrayBackup[3];
                    } else {
                        visitsArray[3] = dasDatum();
                    }
                } else {
                    visitsArray[3] = "none";
                }
                updateVisits();
            }
        });

        switchVisit05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit05.isChecked()) {
                    if (!visitsArrayBackup[4].equals("none")) {
                        visitsArray[4] = visitsArrayBackup[4];
                    } else {
                        visitsArray[4] = dasDatum();
                    }
                } else {
                    visitsArray[4] = "none";
                }
                updateVisits();
            }
        });



        switchVisit06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit06.isChecked()) {
                    if (!visitsArrayBackup[5].equals("none")) {
                        visitsArray[5] = visitsArrayBackup[5];
                    } else {
                        visitsArray[5] = dasDatum();
                    }
                } else {
                    visitsArray[5] = "none";
                }
                updateVisits();
            }
        });

        switchVisit07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit07.isChecked()) {
                    if (!visitsArrayBackup[6].equals("none")) {
                        visitsArray[6] = visitsArrayBackup[6];
                    } else {
                        visitsArray[6] = dasDatum();
                    }
                } else {
                    visitsArray[6] = "none";
                }
                updateVisits();
            }
        });

        switchVisit08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit08.isChecked()) {
                    if (!visitsArrayBackup[7].equals("none")) {
                        visitsArray[7] = visitsArrayBackup[7];
                    } else {
                        visitsArray[7] = dasDatum();
                    }
                } else {
                    visitsArray[7] = "none";
                }
                updateVisits();
            }
        });

        switchVisit09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit09.isChecked()) {
                    if (!visitsArrayBackup[8].equals("none")) {
                        visitsArray[8] = visitsArrayBackup[8];
                    } else {
                        visitsArray[8] = dasDatum();
                    }
                } else {
                    visitsArray[8] = "none";
                }
                updateVisits();
            }
        });

        switchVisit10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisit10.isChecked()) {
                    if (!visitsArrayBackup[9].equals("none")) {
                        visitsArray[9] = visitsArrayBackup[9];
                    } else {
                        visitsArray[9] = dasDatum();
                    }
                } else {
                    visitsArray[9] = "none";
                }
                updateVisits();
            }
        });


    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            visitsArray = new String[10];
            visitsArrayBackup = new String[10];

            loadVisits();

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            updateVisits();
            leaveEditMode();
        }
    }

    private void addNewVisit() {
        Random rn = new Random();

        int visitToAdd = rn.nextInt(10);
        while (!visitsArray[visitToAdd].equals("none")) {
            visitToAdd = rn.nextInt(10);
        }
        visitsArray[visitToAdd] = dasDatum();
    }

    private void clearVisit() {
        for (int i=0; i<visitsArray.length; i++) {
            visitsArray[i] = "none";
        }
    }

    private String dasDatum() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("d.M.yyyy");
        return df.format(c.getTime());
    }

    private void updateVisits() {
        numberOfVisits=0;
        if (visitsArray[0].equals("none")) {
            switchVisit01.setText(getString(R.string.labelNotVisited));
            switchVisit01.setChecked(false);
        } else {
            switchVisit01.setText(getString(R.string.labelVisited,visitsArray[0]));
            switchVisit01.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[1].equals("none")) {
            switchVisit02.setText(getString(R.string.labelNotVisited));
            switchVisit02.setChecked(false);
        } else {
            switchVisit02.setText(getString(R.string.labelVisited,visitsArray[1]));
            switchVisit02.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[2].equals("none")) {
            switchVisit03.setText(getString(R.string.labelNotVisited));
            switchVisit03.setChecked(false);
        } else {
            switchVisit03.setText(getString(R.string.labelVisited,visitsArray[2]));
            switchVisit03.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[3].equals("none")) {
            switchVisit04.setText(getString(R.string.labelNotVisited));
            switchVisit04.setChecked(false);
        } else {
            switchVisit04.setText(getString(R.string.labelVisited,visitsArray[3]));
            switchVisit04.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[4].equals("none")) {
            switchVisit05.setText(getString(R.string.labelNotVisited));
            switchVisit05.setChecked(false);
        } else {
            switchVisit05.setText(getString(R.string.labelVisited,visitsArray[4]));
            switchVisit05.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[5].equals("none")) {
            switchVisit06.setText(getString(R.string.labelNotVisited));
            switchVisit06.setChecked(false);
        } else {
            switchVisit06.setText(getString(R.string.labelVisited,visitsArray[5]));
            switchVisit06.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[6].equals("none")) {
            switchVisit07.setText(getString(R.string.labelNotVisited));
            switchVisit07.setChecked(false);
        } else {
            switchVisit07.setText(getString(R.string.labelVisited,visitsArray[6]));
            switchVisit07.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[7].equals("none")) {
            switchVisit08.setText(getString(R.string.labelNotVisited));
            switchVisit08.setChecked(false);
        } else {
            switchVisit08.setText(getString(R.string.labelVisited,visitsArray[7]));
            switchVisit08.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[8].equals("none")) {
            switchVisit09.setText(getString(R.string.labelNotVisited));
            switchVisit09.setChecked(false);
        } else {
            switchVisit09.setText(getString(R.string.labelVisited,visitsArray[8]));
            switchVisit09.setChecked(true);
            numberOfVisits++;
        }
        if (visitsArray[9].equals("none")) {
            switchVisit10.setText(getString(R.string.labelNotVisited));
            switchVisit10.setChecked(false);
        } else {
            switchVisit10.setText(getString(R.string.labelVisited,visitsArray[9]));
            switchVisit10.setChecked(true);
            numberOfVisits++;
        }
        progress.setProgress(numberOfVisits);

        if (numberOfVisits == 10) {
            newCardButton.setEnabled(true);
            visitButton.setEnabled(false);
        } else {
            newCardButton.setEnabled(false);
            visitButton.setEnabled(true);
        }
    }

    private void loadVisits() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        clearVisit();
        try {
            fis = openFileInput(FILENAME);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String s;
            int i=0;
            //System.out.println("Reading file start:");
            while ((s = br.readLine()) != null) {
                visitsArray[i] = s.replace("\n","");
                //System.out.println("(" + s +")");
                i++;
            }
            //System.out.println("Reading file done:");
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            fos = openFileOutput(FILENAME, MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);

            System.out.println("Writing file");
            for (int i=0; i<visitsArray.length; i++) {
                osw.write(visitsArray[i]+"\n");
            }
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void leaveEditMode() {
        editMode=false;
        saveButton.setVisibility(View.INVISIBLE);
        cancleButton.setVisibility(View.INVISIBLE);
        switchVisit01.setClickable(false);
        switchVisit02.setClickable(false);
        switchVisit03.setClickable(false);
        switchVisit04.setClickable(false);
        switchVisit05.setClickable(false);
        switchVisit06.setClickable(false);
        switchVisit07.setClickable(false);
        switchVisit08.setClickable(false);
        switchVisit09.setClickable(false);
        switchVisit10.setClickable(false);
        updateVisits();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_times:
                Intent myIntent = new Intent(TenCardActivity.this, TimesActivity.class);
                TenCardActivity.this.startActivity(myIntent);
                return true;
            case R.id.action_edit:
                if (!editMode) {
                    editMode = true;
                    saveButton.setVisibility(View.VISIBLE);
                    cancleButton.setVisibility(View.VISIBLE);
                    switchVisit01.setClickable(true);
                    switchVisit02.setClickable(true);
                    switchVisit03.setClickable(true);
                    switchVisit04.setClickable(true);
                    switchVisit05.setClickable(true);
                    switchVisit06.setClickable(true);
                    switchVisit07.setClickable(true);
                    switchVisit08.setClickable(true);
                    switchVisit09.setClickable(true);
                    switchVisit10.setClickable(true);
                    for (int i = 0; i < visitsArray.length; i++) {
                        visitsArrayBackup[i] = visitsArray[i];
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
