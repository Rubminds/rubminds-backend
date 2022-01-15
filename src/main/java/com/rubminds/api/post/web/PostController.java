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
    public ResponseEntity<PostResponse.OnlyId> create(@RequestPart(value = "postInfo") PostRequest.Create request, @RequestPart(value = "files", required = false) List<MultipartFile> files, @CurrentUser CustomUserDetails customUserDetails) {
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
                                                              @RequestParam(name = "region", required = false) String region,
                                                              @RequestParam(name = "skill", required = false) List<Long> skill,
                                                              @RequestParam(name = "keywords", required = false) List<String> customSkillList,
                                                              PageDto pageDto,
                                                              @CurrentUser CustomUserDetails customUserDetails) {
        Page<PostResponse.GetList> response = postService.getList(kinds, postStatus, region, pageDto, skill, customSkillList, customUserDetails);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts/like")
    public ResponseEntity<Page<PostResponse.GetList>> getLikePosts(@RequestParam(name = "kinds", required = false) Kinds kinds,
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

    @PostMapping("/post/{postId}/complete")
    public ResponseEntity<PostResponse.OnlyId> updateCompletePost(@PathVariable Long postId, @RequestPart(value = "completeInfo") PostRequest.CreateCompletePost request, @RequestPart(value = "files", required = false) List<MultipartFile> files, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.OnlyId response = postService.updateCompletePost(postId, request, files, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/post/{postId}/update")
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestPart(value = "postInfo") PostRequest.Create request, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        PostResponse.OnlyId response = postService.update(postId, request, files);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/post/{postId}/changeStatus")
    public ResponseEntity<PostResponse.OnlyId> chanegStatus (@PathVariable Long postId, @RequestBody PostRequest.ChangeStatus request, @CurrentUser CustomUserDetails customUserDetails) {
        PostResponse.OnlyId response = postService.changeStatus(postId, request, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<Page<PostResponse.GetListByStatus>> getListByStatus(@PathVariable Long userId,
                                                              @RequestParam(name = "status", required = false) PostStatus postStatus,
                                                              PageDto pageDto) {
        Page<PostResponse.GetListByStatus> response = postService.getListByStatus(postStatus, userId, pageDto);
        return ResponseEntity.ok().body(response);
    }

//    @DeleteMapping("/post/{postId}")
//    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
//        postService.delete(postId);
//        return ResponseEntity.ok().build();
//    }
}
