package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.ListPascaClient;
import com.mhr.mobile.manage.response.ListPascaResponse;
import java.util.List;
import com.mhr.mobile.util.SignMaker;

public class ListPascaRequest {
    public Activity activity;

    private String commands;
    private String username;
    private String sign;
    private String status;
    private String apiKey;

    public ListPascaRequest(Activity activity) {
        this.activity = activity;
    }

    public ListPascaRequest(String commands, String username, String sign, String status) {
        this.commands = commands;
        this.username = username;
        this.sign = sign;
        this.status = status;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void startRequestListPasca(RequestListPascaCallback callback) {
        sign = SignMaker.getSign(username, apiKey, "pl");
        ListPascaClient.getInstance().execute(this, "pricelist-pasca", username, sign, "all", apiKey, callback);
    }

    public interface RequestListPascaCallback {
        void onResponse(List<ListPascaResponse.Pasca> pascaList);
        void onFailure(String errorMessage);
    }
}