package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuggestedActivity extends AppCompatActivity {

    private RecyclerView recyclerSuggested;
    private Button btnFindRecipes;
    private TextView tvNoRecipes;
    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        recyclerSuggested = findViewById(R.id.recyclerSuggested);
        btnFindRecipes = findViewById(R.id.btnFindRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        dbHelper = new DatabaseHelper(this);

        recyclerSuggested.setLayoutManager(new LinearLayoutManager(this));

        btnFindRecipes.setOnClickListener(v -> showSuggestedRecipes());

        showSuggestedRecipes();
    }

    private void showSuggestedRecipes() {
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();
        List<String> pantryNames = new ArrayList<>();
        for (PantryItem item : pantryItems) {
            pantryNames.add(item.getName().trim().toLowerCase());
        }

        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<Recipe> perfectMatches = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String ingString = recipe.getIngredients();
            if (ingString == null) continue;

            String[] parts = ingString.split(",");
            List<String> recipeIngredients = new ArrayList<>();
            for (String p : parts) {
                if (!p.trim().isEmpty()) {
                    recipeIngredients.add(p.trim().toLowerCase());
                }
            }

            if (recipeIngredients.size() != 3) {
                continue;
            }

            int matchCount = 0;
            for (String req : recipeIngredients) {
                if (pantryNames.contains(req)) {
                    matchCount++;
                }
            }

            if (matchCount == 3) {
                perfectMatches.add(recipe);
            }
        }

        if (perfectMatches.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerSuggested.setVisibility(View.GONE);
            Toast.makeText(this, "No perfect match - add more pantry items", Toast.LENGTH_LONG).show();
            adapter = new RecipeAdapter(perfectMatches, recipe -> {});
            recyclerSuggested.setAdapter(adapter);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerSuggested.setVisibility(View.VISIBLE);
            adapter = new RecipeAdapter(perfectMatches, recipe -> {
                Intent intent = new Intent(SuggestedActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
                startActivity(intent);
            });
            recyclerSuggested.setAdapter(adapter);
        }
    }
}