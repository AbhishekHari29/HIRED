package com.droidevils.hired.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.droidevils.hired.Helper.Bean.Category;
import com.droidevils.hired.Helper.Bean.CategoryInterface;
import com.droidevils.hired.Helper.Bean.Service;
import com.droidevils.hired.Helper.Bean.ServiceInterface;
import com.droidevils.hired.Helper.Adapter.CategoryListAdaptor;
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
        Display newDisplay = getWindowManager().getDefaultDisplay();//Move dropdown icon to right
        int width = newDisplay.getWidth();
        expandableListView.setIndicatorBounds(width-200, width);
        initializeCategoryList();
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

    private void initializeCategoryList() {
        Category.getAllCategory(new CategoryInterface() {
            @Override
            public void getAllCategory(ArrayList<Category> categories) {
                for (Category category : categories) {
                    listGroup.add(category.getCategoryName());
                    Service.getServiceByCategory(category.getCategoryId(), new ServiceInterface() {
                        @Override
                        public void getServiceArrayList(ArrayList<Service> services) {
                            ArrayList<String> serviceNames = new ArrayList<>();
                            for (Service service : services)
                                serviceNames.add(service.getServiceName()+"::"+service.getServiceId());
                            listItem.put(category.getCategoryName(), serviceNames);

                        }
                    });
                }
                categoryListAdaptor.notifyDataSetChanged();
            }
        });
    }

    public void onClickServiceItem(View view){
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SearchActivity.SEARCH_TYPE, SearchActivity.SERVICE_SEARCH);
        bundle.putString(SearchActivity.SEARCH_ID, view.getContentDescription().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBackButton(View view){
        onBackPressed();
    }

}