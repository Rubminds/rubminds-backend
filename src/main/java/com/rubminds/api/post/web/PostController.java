package com.rubminds.api.post.web;

import com.rubminds.api.post.dto.PostLikeRequest;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> savePost(@RequestBody PostRequest.Create request, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.OnlyId response = postService.create(request, customUserDetails.getUser());

        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse.Info> PostInfo(@PathVariable Long postId, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.Info infoResponse = postService.getPost(customUserDetails.getUser(), postId);
        return ResponseEntity.ok().body(infoResponse);
    }

    @GetMapping("/getPosts")
    public ResponseEntity<PostResponse.GetPosts> getPosts(@CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPosts listResponse = postService.getPosts(customUserDetails.getUser());
        return ResponseEntity.ok().body(listResponse);
    }

    @GetMapping("/getLikePosts")
    public ResponseEntity<PostResponse.GetPosts> getLikePosts(@CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPosts listResponse = postService.getLikePosts(customUserDetails.getUser());
        return ResponseEntity.ok().body(listResponse);
    }

    @PostMapping("/postLike")
    public ResponseEntity<PostResponse.GetPostLike> updatePostLike(@RequestBody PostLikeRequest.Update request, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.GetPostLike postLikeResponse = postService.updatePostLike(customUserDetails.getUser(), request);
        return ResponseEntity.ok().body(postLikeResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postId}")
    public Long delete(@PathVariable("postId") Long postId) {
        return postService.delete(postId);

    }
}