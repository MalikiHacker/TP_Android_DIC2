package sn.maliki.crudrestapiapp.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sn.maliki.crudrestapiapp.Controllers.Views.EleveAdapter;
import sn.maliki.crudrestapiapp.Model.Eleve;
import sn.maliki.crudrestapiapp.R;
import sn.maliki.crudrestapiapp.Utils.ApiConfig;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private static final int ADD_ELEVE_REQUEST = 1;
    private static final int EDIT_ELEVE_REQUEST = 2;
    public final static String MA_CLE = "btmaliki";
    public final static String TAG ="CRUD-Eleve";
    private List<Eleve> allEleves;
    private EleveAdapter eleveAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rView);

        getEleveListData();

        FloatingActionButton fab = findViewById(R.id.idFab);


        fab.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddEleve.class);
            intent.putExtra(AddEleve.EXTRA_ELEVE_EMAIL, "");
            startActivityForResult(intent, ADD_ELEVE_REQUEST);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //a l'action glisser on supprime
                deleteEleve(eleveAdapter.getEleve(viewHolder.getAdapterPosition()).getEmail());

                getEleveListData();
            }
        }).
                //on lie la fonctionnalité aux recycleur
                attachToRecyclerView(rv);
    }

    public void deleteEleve(String email){
        (ApiConfig.getApiClient().deleteEleve(MA_CLE, email)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showMessage("Eleve supprimé avec succès !");
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.w(TAG, t.toString());
            }

        });
    }
    private void getEleveListData() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Veuillez patienter"); // set message
        progressDialog.show();

        (ApiConfig.getApiClient().getAllEleves(MA_CLE)).enqueue(new Callback<List<Eleve>>() {
            @Override
            public void onResponse(Call<List<Eleve>> call, Response<List<Eleve>> response) {
                Log.d("responseGET", response.body().get(0).getPrenom());
                progressDialog.dismiss(); //dismiss progress dialog
                allEleves = response.body();
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Eleve>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.w(TAG, t.toString());
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });

    }

    private void setRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(linearLayoutManager);
        // call the constructor of EleveAdapter to send the reference and data to Adapter
        eleveAdapter = new EleveAdapter(MainActivity.this, allEleves);
        rv.setAdapter(eleveAdapter); // set the Adapter to RecyclerView

        eleveAdapter.setOnItemClickListener(eleve -> {


            Intent intent = new Intent(MainActivity.this, AddEleve.class);
            intent.putExtra(AddEleve.EXTRA_ELEVE_EMAIL, eleve.getEmail());

            //on demarre l'activité d'édition d'un nouveau eleve
            startActivityForResult(intent, EDIT_ELEVE_REQUEST);

        });
    }


    // après EDITION ou CREATION on récupère le résultat
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == ADD_ELEVE_REQUEST && resultCode == RESULT_OK) {
                getEleveListData();
                //createEleve(newEleve);

            } else if (requestCode == EDIT_ELEVE_REQUEST && resultCode == RESULT_OK) {
                getEleveListData();
            } else {
                showMessage("Impossible d'effectuer cette action");
            }
        } else {
            getEleveListData();
        }


    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}