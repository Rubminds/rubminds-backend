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

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> create(@RequestBody PostRequest.CreateOrUpdate request, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.OnlyId response = postService.createRecruit(request, customUserDetails.getUser());

        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse.Info> getInfo(@PathVariable Long postId) {
        PostResponse.Info infoResponse = postService.getPost(postId);
        return ResponseEntity.ok().body(infoResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.CreateOrUpdate request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postId}")
    public Long delete(@PathVariable("postId") Long postId) {
        return postService.delete(postId);
    }
}