package com.concamap.api;

import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostAPI {
    private final PostService postService;

    @Autowired
    public PostAPI(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Post> findAllExistPost(@PageableDefault(size = 4) Pageable pageable) {
        return postService.findAllExist(pageable).getContent();
    }

    @GetMapping(value = "", params = "title", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Post> findAllExistPostByTitle(@PageableDefault(size = 4) Pageable pageable,
                                              @RequestParam("title") String title) {
        return postService.findExistByTitle(title, pageable).getContent();
    }

    @GetMapping(value = "", params = "content", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Post> findAllExistPostByContent(@PageableDefault(size = 4) Pageable pageable,
                                                @RequestParam("content") String content) {
        return postService.findExistByContent(content, pageable).getContent();
    }

    @GetMapping(value = "", params = {"title", "content"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Post> findAllExistPostByTitleAndContent(@PageableDefault(size = 4) Pageable pageable,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("content") String content) {

        return postService.findExistByTitleAndContent(title, content, pageable).getContent();
    }

    @GetMapping(value = "", params = "query", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Post> findAllExistPostByTitleOrContent(@PageableDefault(size = 3) Pageable pageable,
                                                       @RequestParam("query") String query) {
        return postService.findExistByTitleOrContent(query, pageable).getContent();
    }
}
