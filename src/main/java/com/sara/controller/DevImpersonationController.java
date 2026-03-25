package com.sara.controller;

import com.sara.entity.User;
import com.sara.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@Profile("dev")
public class DevImpersonationController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public DevImpersonationController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/impersonate/{username}")
    public String impersonate(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            UserDetails uds = userDetailsService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(uds, uds.getPassword(), uds.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}
