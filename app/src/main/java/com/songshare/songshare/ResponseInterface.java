package com.songshare.songshare;

import org.json.JSONObject;

//Interface used for network responses.
public interface ResponseInterface {
    public void responseSuccess(JSONObject response);
    public void responseError();
}
