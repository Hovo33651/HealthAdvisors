package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSchedulingService {
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        List<User> all = userService.findAll();
        for (User user : all) {
            LocalDateTime tokenCreatedDate = user.getTokenCreatedDate();
            LocalDateTime expireDateTime = tokenCreatedDate.plusDays(1);
            if (StringUtils.isNotEmpty(user.getToken())
                    && LocalDateTime.now().isAfter(expireDateTime)) {
                user.setToken(null);
                user.setTokenCreatedDate(null);
                userService.saveUser(user);
            }
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void removeNotActivatedUser(){
        List<User> allUser = userService.findAll();
        for (User user : allUser) {
            if (user.getTokenCreatedDate().plusDays(1).isAfter(LocalDateTime.now())){
                userService.deleteUserById(user.getId());
            }
        }
    }
}
