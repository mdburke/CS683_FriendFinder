package com.imminentapps.friendfinder.utils;

/**
 * Holds various public constants to be used throughout the application.
 * Created by mburke on 9/21/17.
 */
public class Constants {
    public static final int RELATIONSHIP_TYPE_ONE_PENDING_TWO = 1;
    public static final int RELATIONSHIP_TYPE_TWO_PENDING_ONE = 2;
    public static final int RELATIONSHIP_TYPE_FRIENDS = 3;

    public static final String SEARCH_FILTER_FRIENDS = "Friends";
    public static final String SEARCH_FILTER_NOT_FRIENDS = "Not Friends";
    public static final String SEARCH_FILTER_ALL_USERS = "All Users";

    public static final String AWS_PROFILE_IMAGE_BUCKET = "imminentapps-friendfinder-cs683-profile-images";

    public static final String GEO_PREFIX = "geo:0,0?q=";
    private Constants(){}
}
