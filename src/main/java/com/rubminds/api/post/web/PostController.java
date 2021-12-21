package com.rubminds.api.post.web;


import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> create(@RequestPart(value = "postInfo") PostRequest.CreateOrUpdate request, @RequestPart(value = "files", required = false) List<MultipartFile> files, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.OnlyId response = postService.create(request, files, customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse.Info> getOne(@PathVariable Long postId, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.Info response = postService.getOne(postId, customUserDetails);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse.GetPosts> getPosts(@CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPosts response = postService.getPosts(customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts/like")
    public ResponseEntity<PostResponse.GetPosts> getLikePosts(@CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPosts response = postService.getLikePosts(customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<PostResponse.GetPostLike> updatePostLike(@PathVariable Long postId, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPostLike response = postService.updatePostLike(postId, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.CreateOrUpdate request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.ok().body(response);
    }

//    @DeleteMapping("/post/{postId}")
//    public Long delete(@PathVariable("postId") Long postId) {
//        return postService.delete(postId);
//    }
}