package sn.maliki.crudrestapiapp.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sn.maliki.crudrestapiapp.Model.Eleve;

public interface ApiService {
    @GET("/api/{key}/personnes")
    public Call<List<Eleve>> getAllEleves(@Path("key") String myKey);

    @GET("/api/{key}/personnes/{email}")
    public Call<Eleve> getEleve(@Path("key") String myKey, @Path("email") String email);

    @PUT("/api/{key}/personnes")
    public Call<Eleve> addEleve(@Path("key") String myKey, @Body Eleve newEleve);

    @PUT("/api/{key}/personnes/{email}")
    public Call<Eleve> updateEleve(@Path("key") String myKey, @Path("email") String email, @Body Eleve updatedEleve);

    @DELETE("/api/{key}/personnes/{email}")
    public Call<Void> deleteEleve(@Path("key") String myKey, @Path("email") String email);


}