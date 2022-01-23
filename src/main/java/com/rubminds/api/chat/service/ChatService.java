package com.rubminds.api.chat.service;

import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.chat.domain.repository.ChatRepository;
import com.rubminds.api.chat.dto.ChatRequest;
import com.rubminds.api.chat.dto.ChatResponse;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rubminds.api.post.domain.Kinds;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;

    @Transactional
    public ChatResponse.OnlyId create(ChatRequest.Create request, User sender) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(PostNotFoundException::new);
        Chat chat = Chat.create(post,sender,request.getContent());
        Chat saveChat = chatRepository.save(chat);

        return ChatResponse.OnlyId.build(saveChat);
    }

    @Transactional
    public ChatResponse.GetList getChatList(User sender, Long postId, PageDto pageDto) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        isFirstParticipate(sender, post);
        Page<Chat> chats = chatRepository.findAllByPostId(postId, pageDto.of());
        return ChatResponse.GetList.build(post, chats);
    }


    public Page<ChatResponse.GetPostList> getPostList(User loginUser, Kinds kinds, PageDto pageDto) {
        Page<Post> posts = postRepository.findAllBySenderAndKinds(loginUser.getId(), kinds, pageDto.of());
        return posts.map(post -> ChatResponse.GetPostList.build(post));
    }


    private void isFirstParticipate(User sender, Post post) {
        boolean exists = chatRepository.existsBySenderAndPost(sender, post);
        if(exists == false){
            ChatRequest.Create request = ChatRequest.Create.builder()
                    .postId(post.getId())
                    .content(sender.getNickname()+"님이 입장하셨습니다")
                    .build();

            create(request, sender);
        }
    }

}
