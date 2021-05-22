package com.droidevils.hired.Helper.Bean;

import java.util.ArrayList;

public interface CategoryInterface {
    default void getCategoryById(Category category){};
    default void getAllCategory(ArrayList<Category> categories){};
}
