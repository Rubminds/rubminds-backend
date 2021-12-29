package com.rubminds.api.post.web;


import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<PostResponse.GetList>> getList(@RequestParam(name = "kinds", required = false) Kinds kinds,
                                                              @RequestParam(name = "status", required = false) PostStatus postStatus,
                                                              @RequestParam(name = "skill", required = false) Long skillId,
                                                              @RequestParam(name = "keywords", required = false) String customSkillName,
                                                              PageDto pageDto,
                                                              @CurrentUser CustomUserDetails customUserDetails) {
        Page<PostResponse.GetList> response = postService.getList(kinds, postStatus, pageDto, customUserDetails);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts/like")
    public ResponseEntity<Page<PostResponse.GetList>> getLikePosts(@RequestParam(name = "kinds") Kinds kinds,
                                                                   PageDto pageDto,
                                                                   @CurrentUser CustomUserDetails customUserDetails) {
        Page<PostResponse.GetList> response = postService.getLikePosts(kinds, pageDto, customUserDetails);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Void> updatePostLike(@PathVariable Long postId, @CurrentUser CustomUserDetails customUserDetails) {
        postService.updatePostLike(postId, customUserDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/post/{postId}/complete")
    public ResponseEntity<PostResponse.OnlyId> updateCompletePost(@PathVariable Long postId, @RequestBody PostRequest.CreateCompletePost request) {
        PostResponse.OnlyId response = postService.updateCompletePost(postId, request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.CreateOrUpdate request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.ok().body(response);
    }

//    @DeleteMapping("/post/{postId}")
//    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
//        postService.delete(postId);
//        return ResponseEntity.ok().build();
//    }
}