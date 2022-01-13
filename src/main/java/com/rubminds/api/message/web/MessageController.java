package com.rubminds.api.message.web;

import com.rubminds.api.message.dto.MessageRequest;
import com.rubminds.api.message.dto.MessageResponse;
import com.rubminds.api.message.service.MessageService;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse.OnlyId> create(@RequestBody MessageRequest.Create request, @CurrentUser CustomUserDetails customUserDetails) {
        MessageResponse.OnlyId response = messageService.create(request, customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/message/" + response.getId())).body(response);
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<MessageResponse.Info> getOne(@PathVariable Long messageId, @CurrentUser CustomUserDetails customUserDetails) {
        MessageResponse.Info response = messageService.getOne(messageId, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }
}
