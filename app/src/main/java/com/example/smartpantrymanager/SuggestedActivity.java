package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuggestedActivity extends AppCompatActivity {

    RecyclerView recyclerSuggested;
    TextView tvNoRecipes;
    Button btnFind;
    DatabaseHelper dbHelper;
    List<Recipe> allRecipes;
    List<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        recyclerSuggested = findViewById(R.id.recyclerSuggested);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        btnFind = findViewById(R.id.btnFindRecipes);

        recyclerSuggested.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        btnFind.setOnClickListener(v -> findRecipes());
        findRecipes();
    }

    private void findRecipes() {
        allRecipes = dbHelper.getAllRecipes();
        pantryItems = dbHelper.getAllPantryItems();
        List<Recipe> suggestedList = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                suggestedList.add(recipe);
            }
        }

        if (suggestedList.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerSuggested.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerSuggested.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(suggestedList, recipe -> {
                Intent intent = new Intent(SuggestedActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipe_name", recipe.getName());
                intent.putExtra("recipe_ingredients", recipe.getIngredients());
                intent.putExtra("recipe_instructions", recipe.getInstructions());
                startActivity(intent);
            });
            recyclerSuggested.setAdapter(adapter);
        }
    }

    private boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantry) {
        String[] requiredItems = recipe.getIngredients().split(",");

        for (String req : requiredItems) {
            req = req.trim().toLowerCase();
            if (req.isEmpty()) continue;

            String[] parts = req.split(":");
            String reqName = parts[0].trim().toLowerCase();
            reqName = normalizeName(reqName);

            boolean found = false;
            for (PantryItem pItem : pantry) {
                String pantryName = normalizeName(pItem.getName().toLowerCase());
                if (pantryName.equals(reqName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private String normalizeName(String name) {
        name = name.trim().toLowerCase();
        if (name.endsWith("es")) {
            String singular = name.substring(0, name.length() - 2);
            if (singular.length() > 2) return singular;
        }
        if (name.endsWith("s") && name.length() > 3) {
            return name.substring(0, name.length() - 1);
        }
        return name;
    }
}