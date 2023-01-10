package com.LunchAdvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LunchPicker extends AppCompatActivity {
    private RequestQueue requestQueue;
    private TextView dishes;
    private String url = "https://lunchadvisor1.azurewebsites.net/api/DishesApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_picker);
        setTitle("Lunch Picker");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        dishes = (TextView) findViewById(R.id.osebe);
    }

    public void showDishes(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {

        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject dish = response.getJSONObject(i);
                    int id = dish.getInt("id");
                    int restaurantID = dish.getInt("restaurantID");
                    String name = dish.getString("name");
                    String imageURL = dish.getString("imageURL");


                    JSONObject restaurant = dish.getJSONObject("restaurant");

                    String resName = restaurant.getString("name");

                    data.add(name +" restaurant:"+resName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dishes.setText("");

            for (String row : data) {
                String currentText = dishes.getText().toString();
                dishes.setText(currentText + "\n\n" + row);
            }

            }

        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

}