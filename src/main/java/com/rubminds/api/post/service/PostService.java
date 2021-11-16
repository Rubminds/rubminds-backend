package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.*;
import com.rubminds.api.post.dto.UploadPostRequest;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
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
                                request.getPostsStatus(),request.getRegiosn(), request.getSkill());

        PostRepository.save(post1);
        return post1.getId();
    }
//
//    /**
//     * 게시물 수정
//     */
//    @Transactional
//    public void updatePost(Long postId,String title, String content,
//                           int headcount, Kinds kinds, Meeting meeting, PostStatus postsStatus,
//                           Region region) {
//
//        Post findpost = new Post();
//        findpost.setContent(content);
//        findpost.setHeadcount(headcount);
//        findpost.setKinds(kinds);
//        findpost.setMeeting(meeting);
//        findpost.setPostStatus(postsStatus);
//        findpost.setRegion(region);
//
//        return Post.EditPost(findpost);
//    }

}
