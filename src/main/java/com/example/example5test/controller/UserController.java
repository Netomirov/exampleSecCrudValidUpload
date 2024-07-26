package com.example.example5test.controller;

import com.example.example5test.FileUploadUtil;
import com.example.example5test.entity.User;
import com.example.example5test.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/")
    public String vieHomePage(Model model) {
        return findPaginated(1, "name", "asc", model);
    }

    @GetMapping(value = "/home")
    public String project(Model model, @Param("keyword") String keyword) {
        List<User> listUser = userService.getAllUser(keyword);
        model.addAttribute("listUser", listUser);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String registrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }



    @PostMapping(value = "/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result,
                                     @RequestParam("image")MultipartFile multipartFile, Model model) throws IOException {
        if(result.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("errors", result.getAllErrors());
            return "registration";
        }
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setImages(fileName);
        User saveUser = userService.saveUser(user);
        String uploadDir = "user-photos/" + saveUser.getId();
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        return "redirect:/";

    }

    @GetMapping(value = "/user-update/{id}")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public String userUpdateForm(@PathVariable("id") Long id, Model model) {

        User user = userService.getUserById(id);

        model.addAttribute("user", user);
       return "user-update";
    }

    @PostMapping(value = "/user-update")
    public String userUpdate(@ModelAttribute("user") User user,
        @RequestParam("image")MultipartFile multipartFile) throws IOException {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setImages(fileName);
            User saveUser = userService.saveUser(user);
            String uploadDir = "user-photos/" + saveUser.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            return "redirect:/";

    }

    @GetMapping(value = "/user-delete/{id}")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/";
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 2;

        Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<User> listUser = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listUser", listUser);
        return "home";
    }


}
