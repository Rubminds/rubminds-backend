package com.rubminds.api.chat.web;

import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.chat.dto.ChatRequest;
import com.rubminds.api.chat.dto.ChatResponse;
import com.rubminds.api.chat.service.ChatService;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ChatResponse.OnlyId> create(@RequestBody ChatRequest.Create request,
                                                      @CurrentUser CustomUserDetails customUserDetails) {
        ChatResponse.OnlyId response = chatService.create(request, customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/message/" + response.getId())).body(response);
    }

    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<ChatResponse.GetList> getList(@PathVariable(value = "chatRoomId") Long chatRoomId,
                                                        @CurrentUser CustomUserDetails customUserDetails,
                                                        PageDto pageDto) {
        ChatResponse.GetList response = chatService.getChatList(customUserDetails.getUser(), chatRoomId, pageDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/chat")
    public ResponseEntity<Page<ChatResponse.GetPostList>> getPostList(@CurrentUser CustomUserDetails customUserDetails,
                                                                      @RequestParam(name = "kinds", required = true) Kinds kinds,
                                                                      PageDto pageDto) {
        Page<ChatResponse.GetPostList> response = chatService.getPostList(customUserDetails.getUser(), kinds, pageDto);
        return ResponseEntity.ok().body(response);
    }
}

