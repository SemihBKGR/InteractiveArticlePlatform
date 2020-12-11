package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.serialization.object.SuperficialUser;
import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSerializer extends StdSerializer<List<User>> {

    public UserSerializer (){
        this(null);
    }

    protected UserSerializer(Class<List<User>> t) {
        super(t);
    }

    @Override
    public void serialize(List<User> users,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        List<SuperficialUser> userObjectList=new ArrayList<>();
        for(User user:users){
            userObjectList.add(convertToSuperficial(user));
        }
        jsonGenerator.writeObject(userObjectList);

    }

    private SuperficialUser convertToSuperficial(User user){

        SuperficialUser superficialUser=new SuperficialUser();
        superficialUser.setId(user.getId());
        superficialUser.setUsername(user.getUsername());
        superficialUser.setEmail(user.getEmail());
        superficialUser.setInformation(user.getInformation());
        return superficialUser;

    }

}
