package test.rest;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import rest.User;
import rest.config.RestfulConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by L.x on 15-6-1.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class SpringRestfulTest {
    private MockMvc server;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private RestTemplate client;

    @Configuration
    @ComponentScan(basePackageClasses = RestfulConfig.class)
    public static class Config {

    }

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        client = new RestTemplate(new ClientHttpRequestFactory() {
            @Override
            public ClientHttpRequest createRequest(final URI uri, final HttpMethod httpMethod) throws IOException {
                final MockHttpServletResponse response = request(uri, httpMethod);
                return new MockClientHttpRequest(httpMethod, uri) {{
                    setResponse(new MockClientHttpResponse(response.getContentAsByteArray(), HttpStatus.valueOf(response.getStatus())) {{
                        getHeaders().setContentType(MediaType.valueOf(response.getContentType()));
                    }});
                }};
            }
        });
    }

    private MockHttpServletResponse request(URI uri, HttpMethod httpMethod) throws IOException {
        try {
            return server.perform(MockMvcRequestBuilders.request(httpMethod, uri)).andReturn().getResponse();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Test
    public void listAllUsers() throws Exception {
        List<User> users = Arrays.asList(client.getForObject("/users", User[].class));

        assertThat(users, hasSize(1));
        assertThat(users, hasItem(named("zhangsan")));
        assertThat(users, not(hasItem(hasPassword())));

    }

    private Matcher<User> hasPassword() {
        return hasProperty("password", not(isEmptyOrNullString()));
    }

    private Matcher<User> named(String name) {
        return hasProperty("name", equalTo(name));
    }


}
