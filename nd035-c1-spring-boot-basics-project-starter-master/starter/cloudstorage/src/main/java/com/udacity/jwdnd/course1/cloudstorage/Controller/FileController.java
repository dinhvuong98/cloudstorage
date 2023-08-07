package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Constant.Constant;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.Authentication.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {

    private FileService fileService;

    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public RedirectView upload(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload,
                               RedirectAttributes redirectAttributes) throws IOException {
        try {
            RedirectView redirectView = new RedirectView("/home");
            int userId = userService.getUser(authentication.getName()).getUserId();

            if (fileUpload == null) {
                redirectAttributes.addFlashAttribute("error", Constant.MSG_NO_FILE);
                return redirectView;
            }

            if (fileUpload.getSize() > 10 ){
                redirectAttributes.addFlashAttribute("error", Constant.MSG_FILE_LARGE);
                return redirectView;
            }

            if (fileService.isExistFileName(userId, fileUpload.getOriginalFilename())) {
                redirectAttributes.addFlashAttribute("error", Constant.MSG_FILE_EXIST);
                return redirectView;
            }

            File file = new File();
            file.setFileName(fileUpload.getOriginalFilename());
            file.setFileSize(String.valueOf(fileUpload.getSize()));
            file.setContentType(fileUpload.getContentType());
            file.setUserId(userId);
            file.setFileData(fileUpload.getBytes());
            if (fileService.uploadFile(file) > 0) {
                redirectAttributes.addFlashAttribute("success", Constant.MSG_FILE_UPLOAD_SUCCESS);
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return new RedirectView("/home");
    }

    @GetMapping(value = "/download")
    public @ResponseBody RedirectView download(Authentication authentication, @RequestParam("id") int fileId,
                                               HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes) throws IOException {
        RedirectView redirectView = new RedirectView("/home");
        File file = fileService.getById(fileId);
        int userId = userService.getUser(authentication.getName()).getUserId();
        if (fileService.isNullOrNotOwned(file, userId)) {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
            return redirectView;
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        servletOutputStream.write(file.getFileData());
        servletOutputStream.close();
        return redirectView;
    }

    @GetMapping("/delete")
    public RedirectView delete(Authentication authentication, @RequestParam("id") int fileId,
                               RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/home");
        File file = fileService.getById(fileId);
        int userId = userService.getUser(authentication.getName()).getUserId();
        if (fileService.isNullOrNotOwned(file, userId)) {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
            return redirectView;
        }
        if (fileService.delete(fileId) > 0) {
            redirectAttributes.addFlashAttribute("delete", String.format(Constant.MSG_DELETED, "file"));
        }
        return new RedirectView("/home");
    }
}
