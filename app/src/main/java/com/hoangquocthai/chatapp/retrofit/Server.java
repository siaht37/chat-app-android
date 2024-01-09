package com.hoangquocthai.chatapp.retrofit;

import com.hoangquocthai.chatapp.object.User;

public class Server {
    public static final String BASE_URL = "http://192.168.1.5:8081";
    public static final String prefixUser = "/api/users";
    public static final String prefixLogin = prefixUser + "/login";
    public static final String prefixGetAllUser = prefixUser + "/";


    public static final String prefixGroup = "/api/groups";
    public static final String prefixCreate = prefixGroup + "/create-group";
    public static final String prefixGetGroupById = prefixGroup + "/get-group-by-id";
    public static final String prefixGetAllGroupOfUser = prefixGroup + "/";

    public static final String prefixGetAllMessage = prefixGroup + "/get-messages";

    public static User user = null;
}
