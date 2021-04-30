package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.droidevils.hired.Helper.CategoryListAdaptor;
import com.droidevils.hired.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    ArrayAdapter<String> arrayAdapter;
    CategoryListAdaptor categoryListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        expandableListView = findViewById(R.id.category_expandable_list);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        categoryListAdaptor = new CategoryListAdaptor(this, listGroup, listItem);
        expandableListView.setAdapter(categoryListAdaptor);
        initListData();

    }

    private void initListData() {
        String[] categories = getResources().getStringArray(R.array.categories);
        for (String value : categories) {
            listGroup.add(value);
        }

        String[] array;

        List<String>[] list = new ArrayList[10];

        list[0] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.healthcare)));
        list[1] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.hygiene)));
        list[2] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.electronics)));
        list[3] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.software)));
        list[4] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.catering_culinary)));
        list[5] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.outdoor_care)));
        list[6] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.personal_care)));
        list[7] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.interior)));
        list[8] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.child_student_help)));
        list[9] = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.misc)));

        for (int i = 0; i < 10; i++) {
            listItem.put(listGroup.get(i), list[i]);

        }
        categoryListAdaptor.notifyDataSetChanged();

    }
}