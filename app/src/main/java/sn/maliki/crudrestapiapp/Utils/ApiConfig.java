package sn.maliki.crudrestapiapp.Utils;

public class ApiConfig {
    private ApiConfig() {
    }

    public static final String API_URL = "http://185.98.128.121/";

    public static ApiService getApiClient() {
        return ApiClientInstance.getClient(API_URL).create(ApiService.class);
    }
}