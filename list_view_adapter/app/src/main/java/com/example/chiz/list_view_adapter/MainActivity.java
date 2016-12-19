package com.example.chiz.list_view_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("user");
    DatabaseReference mRecipeRef = mRootRef.child("recipes");

    ListView listView;
    EditText searchField;
    Button button;
    List<Recipes> rowItems;
    ArrayList<String> searchedIngredients;

    private static final String TAG = "Chiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.recipe_list);
        searchField = (EditText) findViewById(R.id.search_field);
        button = (Button) findViewById(R.id.search_button);

        this.getData();
    }

    public void getData(){
        mRecipeRef.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rowItems = new ArrayList<Recipes>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final String recipeKey = postSnapshot.getKey();
                    final String title = postSnapshot.child("title").getValue(String.class);
                    final String url = postSnapshot.child("url").getValue(String.class);
                    final String image_url = postSnapshot.child("image_url").getValue(String.class);

                    Log.d(TAG, "recipeKey: "+recipeKey);
                    Log.d(TAG, "title: "+title);
                    Log.d(TAG, "url: "+url);
                    Log.d(TAG, "image_url: "+image_url);
                    Log.d(TAG, "-----");

                    Recipes item = new Recipes(title, url, image_url);
                    rowItems.add(item);

                    RecipeListAdapter adapter = new RecipeListAdapter(getApplicationContext(), rowItems);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView text = (TextView) view.findViewById(R.id.Text2);
                            String recipe_url = text.getText().toString().trim();

                            Intent intent = new Intent(getApplicationContext(), SpecificRecipe.class);
                            intent.putExtra("url", recipe_url);
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void searchIngredient(View view){
        hideKeypad();
        listView.setAdapter(null);
        getSearchedIngredient();

        mRecipeRef.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rowItems = new ArrayList<Recipes>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final String recipeKey = postSnapshot.getKey();
                    final String title = postSnapshot.child("title").getValue(String.class);
                    final String url = postSnapshot.child("url").getValue(String.class);
                    final String image_url = postSnapshot.child("image_url").getValue(String.class);
                    DatabaseReference mIngredientRef = mRootRef.child("recipes/"+recipeKey+"/content/ingredients");

                    Log.d(TAG, "recipeKey: "+recipeKey);
                    Log.d(TAG, "title: "+title);
                    Log.d(TAG, "url: "+url);
                    Log.d(TAG, "image_url: "+image_url);
                    Log.d(TAG, "-----");

                    mIngredientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int size = searchedIngredients.size();
                            int count = 0;
                            for (DataSnapshot ingSnapshot: dataSnapshot.getChildren()) {
                                String ingr = ingSnapshot.getValue().toString().toLowerCase();
                                ingr = ingr.replace("{ingredient=","");
                                ingr = ingr.replaceAll("\\}", "");

                                for(int a = 0; a <size; a++){
                                    String search = searchedIngredients.get(a);
                                    if(ingr.contains(search.toLowerCase())) {
                                        count++;
                                        Log.d(TAG, "ingredient found");
                                        break;
                                    }
                                }
                            }
                            Log.d(TAG, "test: size"+size);
                            Log.d(TAG, "test: count"+count);
                            if(count == size) {
                                Recipes item = new Recipes(title, url, image_url);
                                rowItems.add(item);

                                RecipeListAdapter adapter = new RecipeListAdapter(getApplicationContext(), rowItems);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView text = (TextView) view.findViewById(R.id.Text2);
                                        String recipe_url = text.getText().toString().trim();

                                        Intent intent = new Intent(getApplicationContext(), SpecificRecipe.class);
                                        intent.putExtra("url", recipe_url);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void getSearchedIngredient(){
        String string = searchField.getText().toString();
        //Log.d(TAG, "test: comma separated string: "+string);

        searchedIngredients = new  ArrayList<String>(Arrays.asList(string.split(",|\\, |\\ , |\\ ,")));
        //Log.d(TAG, "test: ArrayList size: "+searchedIngredients.size());
    }

    public void hideKeypad(){
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void clearSearchText(View view){
        searchField.setText("");
        listView.setAdapter(null);
        getData();
    }
}
