package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostSearch;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponse.OnlyId create(PostRequest.Create request, User user) {
        Post post = Post.create(request, user);
        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(UserNotFoundException::new);
        return PostResponse.Info.build(post);
    }


    public PostResponse.OnlyId update(Long postId, PostRequest.Update request) {
        Post post = postRepository.findById(postId).orElseThrow(UserNotFoundException::new);
        post.update(request);

        return PostResponse.OnlyId.build(post);
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }


}
