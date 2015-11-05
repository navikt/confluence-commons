package it.no.nav.kiv.confluence.labs.rest;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JiraSearchFuncTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {

        String baseUrl = System.getProperty("baseurl");
        String resourceUrl = baseUrl + "/rest/jirasearch/1.0/search";

        RestClient client = new RestClient();
        Resource resource = client.resource(resourceUrl);

//        JiraSearchResponse message = resource.post(JiraSearchRequest.class);

//        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
