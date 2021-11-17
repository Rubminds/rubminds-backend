package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.*;
import com.rubminds.api.post.dto.EditPostRequest;
import com.rubminds.api.post.dto.UploadPostRequest;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final com.rubminds.api.post.domain.repository.PostRepository PostRepository;
    private final UserRepository userRepository;

    /**
     * 게시물 업로드
     */
    @Transactional
    public Long savePost(UploadPostRequest request, User user){
        Post post1 = new Post();
        post1 = Post.createPost(user, request.getTitle(), request.getContent(), request.getHeadcount(), request.getKinds(), request.getMeeting(),
                                request.getPostsStatus(),request.getRegion());

        PostRepository.save(post1);
        return post1.getId();
    }

    /**
     * 게시물 수정
     *
     */
    @Transactional
    public Long EditPost(Long id, EditPostRequest request, User user) {
        Post findpost = new Post();
        findpost = PostRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Post.editPost(findpost,user,request.getTitle(), request.getContent(), request.getHeadcount(), request.getKinds(), request.getMeeting(),
                                 request.getPostsStatus(),request.getRegion());

        return id;
    }

    public Post findOne(Long id) {
        return PostRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 게시물 삭제
     *
     */
    public void DeletePost(Long id) {
        Post post = PostRepository.findById(id).orElseThrow(UserNotFoundException::new);
        PostRepository.delete(post);
    }

}
