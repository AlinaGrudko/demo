package com.example.demo.controller;

import com.example.demo.entity.user.User;
import com.example.demo.service.user.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Setter
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam("search") Optional<String> search,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage;
        if(search.isPresent()) {
            userPage = this.userService.findPaginatedAllUsers(PageRequest.of(currentPage - 1, pageSize), search.get());
            model.addAttribute("search", search.get());
        } else {
            userPage = this.userService.findPaginatedAllUsers(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/users";
    }

    @GetMapping("/moderators")
    public String moderators(Model model,
                             @RequestParam("search") Optional<String> search,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage;
        if(search.isPresent()) {
            userPage = this.userService.findPaginatedModerators(PageRequest.of(currentPage - 1, pageSize), search.get());
            model.addAttribute("search", search.get());
        } else {
            userPage = this.userService.findPaginatedModerators(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/moderators";
    }

    @GetMapping("/blocked-users")
    public String blockedUsers(Model model,
                               @RequestParam("search") Optional<String> search,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage;
        if(search.isPresent()) {
            userPage = this.userService.findPaginatedBlockedUsers(PageRequest.of(currentPage - 1, pageSize), search.get());
            model.addAttribute("search", search.get());
        } else {
            userPage = this.userService.findPaginatedBlockedUsers(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/blocked-users";
    }

    @PostMapping("/block-user")
    public String blockUser(@RequestParam int id,
                            @RequestParam String url) {
        this.userService.blockUser(id);
        return "redirect:" + url;
    }

    @PostMapping("/unblock-user")
    public String unblockUser(@RequestParam int id,
                              @RequestParam String url) {
        this.userService.unblockUser(id);
        return "redirect:" + url;
    }

    @PostMapping("/change-role")
    public String changeRoleUser(@RequestParam int id,
                                 @RequestParam String url,
                                 @RequestParam String roleName) {

        switch (roleName) {
            case "ROLE_USER":
                this.userService.changeRoleToUser(id);
                break;

            case "ROLE_MODERATOR":
                this.userService.changeRoleToModerator(id);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + roleName);
        }

        return "redirect:" + url;
    }
}
