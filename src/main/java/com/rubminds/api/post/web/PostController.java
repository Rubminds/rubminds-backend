package com.rubminds.api.post.web;//package com.rubminds.api.post.web;

import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.EditPostRequest;
import com.rubminds.api.post.dto.SkillRequest;
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
        Long postId = postService.savePost(request, customUserDetails.getUser());
        return postId;
    }

    @PutMapping("/{id}/edit")
    public Long editPost(@RequestBody EditPostRequest request, @PathVariable("id") Long id,
                         @CurrentUser CustomUserDetails customUserDetails) {
        Long postId = postService.EditPost(id,request, customUserDetails.getUser());
        postService.findOne(id);

        return postId;
    }

    @PostMapping("/{id}/delete")
    public void DeletePost(@PathVariable("id") Long id) {
        postService.DeletePost(id);

    }
}




