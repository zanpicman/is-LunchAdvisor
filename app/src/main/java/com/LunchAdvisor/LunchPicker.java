package com.LunchAdvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.CircularArray;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import com.squareup.picasso.Picasso;

public class LunchPicker extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ImageView dishes;
    private String url = "https://lunchadvisor1.azurewebsites.net/api/DishesApi";
    private String[] dishNames;
    private int[] dishID;
    private String[] dishURL;
    private String[] restaurantName;
    private int[] restaurantIDs;
    private String[] restaurantURL;
    private boolean[] ocene = new boolean[6];
    private int[] oceneIndex = new int[6];
    private int workingOn;
    private int stOcen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_picker);
        setTitle("Lunch Picker");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        dishes = (ImageView) findViewById(R.id.imageView);
    }

    public void showDishes(View view) {
        if (view != null) {
            Button button = (Button) view;
            button.setVisibility(Button.INVISIBLE);
            View like = findViewById(R.id.like);
            like.setVisibility(View.VISIBLE);
            View dislike = findViewById(R.id.dislike);
            dislike.setVisibility(View.VISIBLE);
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {

        @Override
        public void onResponse(JSONArray response) {
            dishNames = new String[response.length()];
            dishID = new int[response.length()];
            dishURL = new String[response.length()];
            restaurantName = new String[response.length()];
            restaurantIDs = new int[response.length()];
            restaurantURL = new String[response.length()];
            ocene = new boolean[6];
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject dish = response.getJSONObject(i);
                    dishID[i] = dish.getInt("id");
                    restaurantIDs[i] = dish.getInt("restaurantID");
                    dishNames[i] = dish.getString("name");
                    dishURL[i] = dish.getString("imageURL");
                    JSONObject restaurant = dish.getJSONObject("restaurant");
                    restaurantName[i] = restaurant.getString("name");
                    restaurantURL[i] = restaurant.getString("imageURL");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            workingOn = getRandomNumber(0, dishNames.length);
            setImageAndName(workingOn);
            stOcen++;
        }
    };

    private void setImageAndName(int position) {
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get().load(dishURL[position]).into(imageView);
        TextView textView = findViewById(R.id.textView);
        textView.setText(dishNames[position]);
    }

    public void setOcenaTrue(View view) {
        if (stOcen < 6) {
            ocene[stOcen] = true;
            oceneIndex[stOcen] = workingOn;
            workingOn = getRandomNumber(0, dishNames.length);
            setImageAndName(workingOn);
            stOcen++;
        } else prikazRestavracije();

    }

    public void setOcenaFalse(View view) {
        if (stOcen < 6) {
            ocene[stOcen] = false;
            oceneIndex[stOcen] = workingOn;
            workingOn = getRandomNumber(0, dishNames.length);
            setImageAndName(workingOn);
            stOcen++;
        } else prikazRestavracije();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    private void prikazRestavracije() {
        ImageView imageView = findViewById(R.id.imageView);
        int rnd = getRandomNumber(0, stOcen);
        while (!ocene[rnd]) rnd = getRandomNumber(0, stOcen);

        Picasso.get().load(dishURL[oceneIndex[rnd]]).into(imageView);
        TextView textView = findViewById(R.id.textView);
        textView.setText(dishNames[oceneIndex[rnd]]);
        Button b1 = findViewById(R.id.like);
        Button b2 = findViewById(R.id.dislike);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        TextView poskusi = findViewById(R.id.poskusite);
        poskusi.setText("Poskusite:");
        TextView res = findViewById(R.id.restavracijaText);
        res.setText(restaurantName[oceneIndex[rnd]]);
        ImageView img = findViewById(R.id.imageView2);
        Picasso.get().load(restaurantURL[oceneIndex[rnd]]).into(img);

    }
}