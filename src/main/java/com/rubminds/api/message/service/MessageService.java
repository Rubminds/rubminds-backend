package com.rubminds.api.message.service;

import com.rubminds.api.common.exception.PermissionException;
import com.rubminds.api.message.domain.Message;
import com.rubminds.api.message.domain.repository.MessageRepository;
import com.rubminds.api.message.dto.MessageRequest;
import com.rubminds.api.message.dto.MessageResponse;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse.OnlyId create(MessageRequest.Create request, User sender) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(PostNotFoundException::new);
        Message message = Message.create(request,post,sender.getId());
        Message saveMessage = messageRepository.save(message);

        return MessageResponse.OnlyId.build(saveMessage);
    }

    @Transactional
    public MessageResponse.Info getOne(Long messageId, User loginUser) {
        Message message = messageRepository.findById(messageId).orElseThrow(PostNotFoundException::new);

        if(message.isRead() == false){
            message.updateRead(message);
        }

        if(!loginUser.getId().equals(message.getReceiverId())){
            throw new PermissionException();
        }

        String sender = userRepository.findNicknameById(message.getSenderId()).orElseThrow(UserNotFoundException::new);
        String receiver = userRepository.findNicknameById(message.getReceiverId()).orElseThrow(UserNotFoundException::new);

        return MessageResponse.Info.build(message, sender, receiver);
    }

}
