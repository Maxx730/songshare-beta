package com.songshare.songshare;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NetworkHandler {
    private RequestQueue queue;
    private Properties props;
    private String url = "http://157.245.135.171:666";
    private PreferenceHandler prefs;

    public NetworkHandler(Context con) {
        this.queue = Volley.newRequestQueue(con);
        this.props = new Properties();
        this.prefs = new PreferenceHandler(con);
    }

    //Checks if user login credentials are valid.
    public  void checkLogin(final String username, final String password,final ResponseInterface resp) {
        StringRequest login = new StringRequest(Request.Method.POST, url + "/user/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respon = new JSONObject(response);
                    resp.responseSuccess(respon);
                } catch(JSONException error) {
                    error.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resp.responseError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        this.queue.add(login);
    }

    public void attemptSignup(String username,String password,String email,final ResponseInterface resp) {
        StringRequest signup = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rep = new JSONObject(response);

                    if(rep.getString("TYPE") == "SUCCESS") {
                        resp.responseSuccess(rep);
                    } else {
                        resp.responseError();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resp.responseError();
            }
        });
    }

    public void getTrackData(String trackId,final String accessToken,final ResponseInterface resp) {
        JsonObjectRequest objReq = new JsonObjectRequest("https://api.spotify.com/v1/tracks/" + trackId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resp.responseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();

                params.put("Authorization","Bearer "+accessToken);
                return params;
            }
        };
        this.queue.add(objReq);
    }

    public void getShared(int userId,final ResponseInterface resp) {
        JsonObjectRequest req = new JsonObjectRequest("http://157.245.135.171:666/share/" + userId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resp.responseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.queue.add(req);
    }

    public void getFeed(int userId,final ResponseInterface resp) {
        JsonObjectRequest req = new JsonObjectRequest(url + "/user/" + userId + "/shares", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resp.responseSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.queue.add(req);
    }

    public void shareTrack(final Track track, final int userId, final ResponseInterface resp) {
        StringRequest req = new StringRequest(Request.Method.POST, "http://157.245.135.171:666/share/create", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    resp.responseSuccess(new JSONObject(response));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> newTrack = new HashMap<>();

                newTrack.put("_id",String.valueOf(userId));
                newTrack.put("title",track.getTitle());
                newTrack.put("artist",track.getTitle());
                newTrack.put("spotify_id","");
                newTrack.put("art","");
                return newTrack;
            }
        };
        this.queue.add(req);
    }

    public void getUser(String userId,final ResponseInterface resp) {
        StringRequest request = new StringRequest(Request.Method.GET, url + "/user/" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(new JSONObject(response).getString("TYPE").equals("SUCCESS")) {
                        resp.responseSuccess(new JSONObject(response).getJSONArray("PAYLOAD").getJSONObject(0));
                    } else {
                        resp.responseError();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        this.queue.add(request);
    }

    public void likeTrack(final int trackId,final ResponseInterface resp) {
        StringRequest request = new StringRequest(Request.Method.POST, url + "/share/like", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    resp.responseSuccess(new JSONObject(response));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",prefs.getPreference("songshare-id"));
                params.put("track_id",String.valueOf(trackId));
                return params;
            }
        };
        this.queue.add(request);
    }
}