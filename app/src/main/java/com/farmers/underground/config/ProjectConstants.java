package com.farmers.underground.config;

/**
 * Created by omar
 * on 9/30/15.
 */
public class ProjectConstants {

    public static final String KEY_DATA = "data";
    public static final String KEY_FOCUS_SEARCH_VIEW = "focus_search_view";

    public static final String KEY_START_MAIN_ACTIVITY_PAGE  = "StartWithPageSelected";

    public static final String MAIN_ACTIVITY_PAGE_FAV  = "main_page_favourites";
    public static final String MAIN_ACTIVITY_PAGE_ALL  = "main_page_all";

    public static final String PREFERENCES_FILE_NAME_USR = "preferences_usr";
    public static final String PREFERENCES_FILE_NAME_APP = "preferences_app";


    public static final String KEY_CURRENT_USER_SEARCH_HINTS        = "currentUser::search_hints";
    public static final String KEY_CURRENT_USER_LOGIN               = "currentUser::Login";
    public static final String KEY_CURRENT_USER_PASSWORD            = "currentUser::Password";
    public static final String KEY_CURRENT_USER_COOKIES             = "currentUser::Cookies";
    public static final String KEY_CURRENT_USER_LOGIN_SUCCESSFUL    = "currentUser::LoginSuccessful";
    public static final String KEY_CURRENT_USER_SKIP_MODE           = "currentUser::SkipMode";

    public static final String KEY_APP_SHOW_SKIP_TUTORIAL           = "application::TutorialShowSkip";
    public static final String KEY_APP_LAUNCHED_BEFORE              = "application::LaunchedBefore";

    public static final String SERVER_DATE_FORMAT                   = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";

    public static final int MAX_RAM_CACHE_SIZE                      = 1024 * 1024 * 30; // 30MB

    private ProjectConstants(){/*prevent creation*/}
}
