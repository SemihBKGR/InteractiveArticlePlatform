package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialUser;
import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserListSerializer extends StdSerializer<List<User>> {

    public UserListSerializer(){
        this(null);
    }

    protected UserListSerializer(Class<List<User>> t) {
        super(t);
    }

    @Override
    public void serialize(List<User> users,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        Set<SuperficialUser> userObjectList=new HashSet<>();
        for(User user:users){
            userObjectList.add(new SuperficialUser(user));
        }
        jsonGenerator.writeObject(userObjectList);

    }

}
