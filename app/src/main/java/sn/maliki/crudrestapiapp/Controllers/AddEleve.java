package sn.maliki.crudrestapiapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sn.maliki.crudrestapiapp.Model.Eleve;
import sn.maliki.crudrestapiapp.R;
import sn.maliki.crudrestapiapp.Utils.ApiConfig;

public class AddEleve extends AppCompatActivity {


    public static final String EXTRA_ELEVE_EMAIL =
            "sn.maliki.crudrestapiapp.Controllers.AddEleve.EXTRA_ELEVE_EMAIL";
    public static final String EXTRA_ELEVE_NAME =
            "sn.maliki.crudrestapiapp.Controllers.AddEleve.EXTRA_ELEVE_NAME";
    public static final String EXTRA_ELEVE_LASTNAME =
            "sn.maliki.crudrestapiapp.Controllers.AddEleve.EXTRA_ELEVE_LASTNAME";
    public static final String EXTRA_DATE_NAISS =
            "sn.maliki.crudrestapiapp.Controllers.AddEleve.EXTRA_DATE_NAISS";
    static final int DATE_DIALOG_ID = 0;

    private EditText elevePrenomEditTxt, eleveNomEditTxt, eleveEmailEditTxt;

    private TextView editDate;

    private String editElev = "";
    private String d1, d2, d3;

    private int mYear;
    private int mMonth;
    private int mDay;

    private ProgressDialog progressDialog;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eleve);

        getSupportActionBar().setTitle("Creation");

        progressDialog = new ProgressDialog(AddEleve.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Traitement en cours...");

        elevePrenomEditTxt = findViewById(R.id.prenom_edit_input);
        eleveNomEditTxt = findViewById(R.id.nom_edit_input);
        eleveEmailEditTxt = findViewById(R.id.email_edit_input);
        editDate = findViewById(R.id.dateNaissance_edit_input);

        Intent intent = getIntent();

        editElev = intent.getStringExtra(EXTRA_ELEVE_EMAIL);

        Button saveButton = findViewById(R.id.save_eleve);
        Button leaveButton = findViewById(R.id.close_eleve);

        editDate.setOnClickListener(view -> showDialog(DATE_DIALOG_ID));

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        mDateSetListener =
                (view, year, monthOfYear, dayOfMonth) -> {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDateDisplay();
                };

        if (!editElev.equals("")) {
            getSupportActionBar().setTitle("Edition");
            getAndFillArea(editElev);
        }
        saveButton.setOnClickListener(v -> {
            String personPrenom = elevePrenomEditTxt.getText().toString();
            String personNom = eleveNomEditTxt.getText().toString();
            String personEmail = eleveEmailEditTxt.getText().toString();

            if (personNom.isEmpty() || personPrenom.isEmpty() || personEmail.isEmpty()) {
                showMessage("Veuillez entrer les champs correctement");
                return;
            }

            if (editElev.equals("")) {
                saveEleve(personPrenom, personNom, personEmail);
            } else {
                editEleve(personPrenom, personNom, personEmail);
            }

        });

        leaveButton.setOnClickListener(view -> {
            Intent i = new Intent(AddEleve.this, MainActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
        }
        return null;
    }

    private void saveEleve(String prenom, String nom, String email) {

        Intent data = new Intent(AddEleve.this, MainActivity.class);
        String clef = MainActivity.MA_CLE;

        setResult(RESULT_OK, data);
        Eleve newEleve = new Eleve(email, clef, prenom, nom);
        newEleve.setDateNaissance(new Date(mYear-1900, mMonth, mDay, 0, 0, 0));
        createEleve(newEleve);
        startActivity(data);
    }

    private void editEleve(String prenom, String nom, String email) {

        Intent data = new Intent(AddEleve.this, MainActivity.class);
        String clef = MainActivity.MA_CLE;

        setResult(RESULT_OK, data);
        Eleve newEleve = new Eleve(email, clef, prenom, nom, d1, d2, d3);
        newEleve.setDateNaissance(new Date(mYear-1900, mMonth, mDay,0,0,0));
        updateEleve(newEleve, email);
        startActivity(data);
    }

    private void getAndFillArea(String email) {
        progressDialog.show();

        (ApiConfig.getApiClient().getEleve(MainActivity.MA_CLE, email)).enqueue(new Callback<Eleve>() {

            @Override
            public void onResponse(Call<Eleve> call, Response<Eleve> response) {
                if (response.body() != null) {
                    progressDialog.dismiss();

                    Eleve p = response.body();
                    elevePrenomEditTxt.setText(p.getPrenom());
                    eleveNomEditTxt.setText(p.getNom());
                    eleveEmailEditTxt.setText(p.getEmail());
                    editDate.setText(formatDate(p.getDateNaissance()));
                    eleveEmailEditTxt.setEnabled(false);
                    d1 = p.getDateNaissance();
                    d2 = p.getDateEnregistrement();
                    d3 = p.getDateModification();
                } else {
                    progressDialog.dismiss();
                    showMessage("Une erreur est survenue");
                }
            }

            @Override
            public void onFailure(Call<Eleve> call, Throwable t) {
                Toast.makeText(AddEleve.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.w(MainActivity.TAG, t.toString());

            }
        });

    }


    private void updateEleve(Eleve p, String email) {
        progressDialog.show();

        (ApiConfig.getApiClient().updateEleve(MainActivity.MA_CLE, email, p)).enqueue(new Callback<Eleve>() {

            @Override
            public void onResponse(Call<Eleve> call, Response<Eleve> response) {
                if (response.body() != null) {
                    Eleve p = response.body();
                    progressDialog.dismiss();
                    showMessage("Eleve: " + p.getPrenom() + " " + p.getNom() + " mise a jour avec succès!!");

                } else {
                    progressDialog.dismiss();
                    showMessage("Une erreur est survenue");
                }
            }

            @Override
            public void onFailure(Call<Eleve> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddEleve.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.w(MainActivity.TAG, t.toString());

            }
        });

    }

    private void createEleve(Eleve p) {
        // set message
        progressDialog.show();
        (ApiConfig.getApiClient().addEleve(MainActivity.MA_CLE, p)).enqueue(new Callback<Eleve>() {

            @Override
            public void onResponse(Call<Eleve> call, Response<Eleve> response) {
                if (response.body() != null) {
                    Eleve p = response.body();
                    progressDialog.dismiss();
                    showMessage("Eleve: " + p.getPrenom() + " " + p.getNom() + " crée avec succès!!");

                } else {
                    showMessage("Une erreur est survenue");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Eleve> call, Throwable t) {
                Toast.makeText(AddEleve.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.w(MainActivity.TAG, t.toString());

            }
        });

    }

    private String formatDate(String s){
        return s.split("T")[0];
    }


    private void updateDateDisplay() {
        editDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mYear).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mDay).append(" "));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}