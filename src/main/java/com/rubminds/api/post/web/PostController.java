package com.rubminds.api.post.web;//package com.rubminds.api.post.web;

import com.rubminds.api.post.dto.UploadPostRequest;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/upload")
    public Long savePost(@RequestBody UploadPostRequest request, @CurrentUser CustomUserDetails customUserDetails) {
//      UploadPostResponse.Upload uploadResponse = postService.savePost(request, customUserDetails.getUser());
        Long postId = postService.savePost(request, customUserDetails.getUser());
        return postId;
    }
}




