package de.beanfactory.ug.nginx.nginxdemo.backendserver;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackendControllerApplicationTest {

    @Autowired private WebApplicationContext webApplicationContext;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("BACKEND", "BACKEND-NAME");
    }

    @Test
    public void itShouldRespondToHelloEndpoint() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/hello")
                .accept(MediaType.TEXT_PLAIN)
                .header("X-Header", "HolyMoly")
        )
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("X-Header: HolyMoly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("This is Instance BACKEND-NAME"));
    }

}
