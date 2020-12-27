package core.request;

import core.DataHandler;
import core.DataPolicy;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    @Test
    void searchUser() throws IOException {

        RequestService requestService=new RequestService(DataPolicy.getPolicyBySystemFeatures());
        System.out.println(requestService.searchUser("acc").getData());
        System.out.println(requestService.searchUser("acc").getData().size());

    }

}