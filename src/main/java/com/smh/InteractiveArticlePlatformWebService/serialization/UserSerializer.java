package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialUser;
import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer(){
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeObject(new SuperficialUser(user));

    }


}
