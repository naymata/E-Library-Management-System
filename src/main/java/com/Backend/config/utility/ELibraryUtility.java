package com.Backend.config.utility;

import java.util.Arrays;

public class ELibraryUtility {

    public static final String SOMETHING_WENT_WRONG = "Something went wrong";

    public static final String NOT_FOUND = "not found";
    public static final String INVALID_INFORMATION = "invalid information";
    public static final String IS_INVALID = "is invalid";
    public static final String SUCCESS = "success";
    public static final String UPDATED_SUCCESSFULLY = "updated successfully";
    public static final String DELETED_SUCCESSFULLY = "successfully deleted";
    public static final String CREATED_SUCCESSFULLY = "successfully created";


    public static Boolean checkPageSize(Integer size) {
        Integer[] sizes = {4, 8, 12, 24};
        return Arrays.asList(sizes).contains(size);
    }

    public static <T> String stringFormatter(String entityName, T field, String i) {
        return stringWithOneField(entityName, field) + " " + i;
    }

    private static <T> String stringWithOneField(String entityName, T field) {
        return String.format("%1s%2s", entityName, field);
    }

}
