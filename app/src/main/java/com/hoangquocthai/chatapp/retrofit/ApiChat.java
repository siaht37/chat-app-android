package com.hoangquocthai.chatapp.retrofit;

import com.hoangquocthai.chatapp.dto.GroupChat;
import com.hoangquocthai.chatapp.dto.GroupChatRequestDto;
import com.hoangquocthai.chatapp.dto.MessageDTO;
import com.hoangquocthai.chatapp.dto.UserDTO;
import com.hoangquocthai.chatapp.object.Message;
import com.hoangquocthai.chatapp.object.User;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiChat {
    @GET(Server.prefixGetAllUser)
    Observable<List<User>> getAllUser();

    @POST(Server.prefixGetAllGroupOfUser)
    @FormUrlEncoded
    Observable<List<GroupChat>> getAllGroupOfUser(@Field("username") String username);

    @POST(Server.prefixLogin)
    Observable<User> login(@Body UserDTO userLoginDTO);

    @POST(Server.prefixCreate)
    Observable<GroupChat> createGroup(@Body GroupChatRequestDto groupChatRequestDto);

    @POST(Server.prefixGetAllMessage)
    @FormUrlEncoded
    Observable<List<MessageDTO>> getAllMessageByGroup(@Field("groupId") Long groupId);

    @POST(Server.prefixGetGroupById)
    @FormUrlEncoded
    Observable<GroupChat> getGroupById(@Field("groupId") Long id);


//
//    @POST(Server.prefixDelete+Server.prefixDelete)
//    @FormUrlEncoded
//    Observable<Status> deleteProduct(
//    );

}
