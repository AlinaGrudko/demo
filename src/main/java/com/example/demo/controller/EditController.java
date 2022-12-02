package com.example.demo.controller;

import com.example.demo.dto.password.PasswordDto;
import com.example.demo.entity.user.User;
import com.example.demo.exception.IncorrectPasswordException;
import com.example.demo.exception.PasswordsNotMatchException;
import com.example.demo.validator.group.BasicInfo;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@Setter
@RequestMapping("/edit")
@SessionAttributes(EditController.ATTRIBUTE_USER_NAME)
public class EditController {

    public static final String ATTRIBUTE_USER_NAME = "user";
    public static final String BINDING_RESULT_USER_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_USER_NAME;

    public static final String ATTRIBUTE_PASSWORD_NAME = "passwordDto";
    public static final String BINDING_RESULT_PASSWORD_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_PASSWORD_NAME;

    @Value("${upload.path}")
    private String uploadPath;

    private UserService userService;


    @GetMapping("/profile")
    public String showEditProfile(Model model) {

        if (!model.containsAttribute(BINDING_RESULT_USER_NAME)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = this.userService.findUserByUserName(auth.getName());
            model.addAttribute("user", user);
        }

        return "edit/profile";
    }

    @PostMapping("/profile")
    public String editProfile(@RequestParam MultipartFile file,
                              @ModelAttribute @Validated(BasicInfo.class) User user,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              SessionStatus sessionStatus) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_USER_NAME, bindingResult);
            return "redirect:/edit/profile";
        }

        if(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            File uploadDir = new File(uploadPath + "/user");

            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/user/" + resultFileName));

            if(!user.getFileName().equals("noname.png")) {
                File oldFile = new File(uploadPath + "/user/" + user.getFileName());
                oldFile.delete();
            }

            user.setFileName(resultFileName);
        }

        this.userService.updateUser(user);
        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("successMessage", "Ваш профиль обновлён успешно!");
        return "redirect:/";
    }

    @GetMapping("/password")
    public String editPassword(Model model) {
        if (!model.containsAttribute(BINDING_RESULT_PASSWORD_NAME)) {
            model.addAttribute(ATTRIBUTE_PASSWORD_NAME, new PasswordDto());
        }

        return "edit/password";
    }

    @PostMapping("/password")
    public String editPassword(@ModelAttribute @Valid PasswordDto passwordDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if(!bindingResult.hasErrors()) {
            try {
                this.userService.setNewPassword(passwordDto);
            } catch (PasswordsNotMatchException e) {
                bindingResult.rejectValue("matchingPassword", "error.password.matching", e.getMessage());
            } catch (IncorrectPasswordException e) {
                bindingResult.rejectValue("oldPassword", "error.password.old", e.getMessage());
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PASSWORD_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(ATTRIBUTE_PASSWORD_NAME, passwordDto);
            return "redirect:/edit/password";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Ваш пароль изменён успешно!");
        return "redirect:/";
    }
}
