package com.concamap.controllers.home;

import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Tạo cổng mặc định
class HomeControllerTest {

    private final WebApplicationContext webApplicationContext;

    MockMvc mockMvc; //Tạo ra url không cần phải localhost

    @InjectMocks
    private HomeController homeController;

    private final PostService postService;

    @Autowired
    public HomeControllerTest(WebApplicationContext webApplicationContext, PostService postService) {
        this.webApplicationContext = webApplicationContext;
        this.postService = postService;
    }

    @BeforeEach
    public void init() {
//        Post post = new Post();
//        post.setTitle("Dân tổ không sợ khổ");
//        postService.save(post);
//
//        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void whenGetRequest_thenReturnPost() throws Exception {
//        mockMvc.perform() .andExpect(status().is(200)).andExpect(view().name("home/index"));
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andExpect(view().name("home/index"));
    }
}