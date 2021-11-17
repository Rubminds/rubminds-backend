//package com.rubminds.api;
//
//
//import com.rubminds.api.post.domain.PostEnumClass.Kinds;
//import com.rubminds.api.post.domain.PostEnumClass.Meeting;
//import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
//import com.rubminds.api.post.domain.PostEnumClass.Region;
//import com.rubminds.api.post.dto.UploadPostRequest;
//import com.rubminds.api.post.service.PostService;
//import com.rubminds.api.user.domain.User;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.annotation.Rollback;
//
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class PostServiceTest {
//    @Autowired
//    PostService postService;
//    UserDetailsService userDetailsService;
//
//    EntityManager em;
//
//    @Test
//    @Rollback(value = false)
//    public void 글쓰기() throws Exception{
//
//        User user = new User();
//        user.builder()
//                .nickname("해피")
//                .oauthId("123").build();
//
//        //given
//        UploadPostRequest post = new UploadPostRequest();
//        post.builder()
//                .content("만나서반가워요")
//                .headcount(3)
//                .kinds(Kinds.PROJECT)
//                .postsStatus(PostStatus.FINISHED)
//                .title("테스트")
//                .meeting(Meeting.BOTH)
//                .region(Region.경기)
//                .user(user)
//                .build();
//
//        //when
//        Long savePost = postService.savePost(post,user,skill);
//
//        //then
//        em.flush();
//    }
//
//
//
//
//}
