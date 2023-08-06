package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Constant.Constant;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.Form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.Authentication.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private CredentialService credentialService;

    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public RedirectView insertOrUpdate(Authentication authentication, CredentialForm credentialForm,
                                       RedirectAttributes redirectAttributes) {
        int userId = userService.getUser(authentication.getName()).getUserId();

        Credential credential = new Credential(credentialForm.getCredentialId(), credentialForm.getUrl(),
                credentialForm.getUsername(), null, credentialForm.getPassword(), userId);

        if (credentialService.insertOrUpdate(credential) > 0) {
            redirectAttributes.addFlashAttribute("success", "Your credential has been saved.");
        } else {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
        }
        return new RedirectView("/home");
    }

    @GetMapping(value = "/delete")
    public RedirectView delete(Authentication authentication, @RequestParam(value = "id", required = true) int credentialId,
                               RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/home");
        int userId = userService.getUser(authentication.getName()).getUserId();
        Credential credential = credentialService.getById(credentialId);

        if (credentialService.isNullOrNotOwned(credential, userId)) {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
            return redirectView;
        }

        if (credentialService.delete(credentialId) > 0) {
            redirectAttributes.addFlashAttribute("delete", "Your credential has been deleted.");
        }

        return new RedirectView("/home");
    }
}
