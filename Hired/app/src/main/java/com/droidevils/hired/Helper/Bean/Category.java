package com.droidevils.hired.Helper.Bean;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Category {

    private String categoryId;
    private String categoryName;
    private String description;

    public Category() {
    }

    public Category(String categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public static void getAllCategory(CategoryInterface categoryInterface) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Category");
        reference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Category> categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    categories.add(category);
                }
                categoryInterface.getAllCategory(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryInterface.getAllCategory(null);
            }
        });

    }

    public static void getCategoryById(String categoryId, CategoryInterface categoryInterface) {
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.getCategoryById(categoryInterface);
    }

    public void getCategoryById(CategoryInterface categoryInterface) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Category");
        reference.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);
                categoryInterface.getCategoryById(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryInterface.getCategoryById(null);
            }
        });
    }


    public boolean addCategory() {
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Category");
        reference.child(categoryId).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                result[0] = task.isSuccessful();
            }
        });
        return result[0];
    }

    public boolean updateCategory() {
        return addCategory();
    }

    public boolean deleteCategory() {
        final boolean[] result = new boolean[1];
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Category");
        reference.child(categoryId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                result[0] = task.isSuccessful();
            }
        });
        return result[0];
    }

    public void displayCategory() {
        System.out.println("---------------------------------------------");
        System.out.println("CategoryID:\t" + categoryId);
        System.out.println("CategoryName:\t" + categoryName);
        System.out.println("Description:\t" + description);
        System.out.println("---------------------------------------------");
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}