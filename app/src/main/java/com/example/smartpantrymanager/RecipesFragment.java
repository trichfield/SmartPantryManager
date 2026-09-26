package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DatabaseHelper db = new DatabaseHelper(requireContext());
        List<Recipe> recipes = db.getAllRecipes();
        RecipeAdapter adapter = new RecipeAdapter(recipes, recipe -> {
            Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}