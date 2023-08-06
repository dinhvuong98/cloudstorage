package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Constant.Constant;
import com.udacity.jwdnd.course1.cloudstorage.Model.Form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.Authentication.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    public RedirectView insertOrUpdate(Authentication authentication, @ModelAttribute() NoteForm noteForm,
                                       RedirectAttributes redirectAttributes) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        Note note = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);
        if (noteService.insertOrUpdate(note) > 0) {
            redirectAttributes.addFlashAttribute("success", String.format(Constant.MSG_SAVED, "note"));
        } else {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
        }
        return new RedirectView("/home");
    }

    @GetMapping(value = "/delete")
    public RedirectView delete(Authentication authentication, @RequestParam(value = "id", required = true) int noteId,
                               RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/home");
        int userId = userService.getUser(authentication.getName()).getUserId();
        Note note = noteService.getNoteById(noteId);
        if (noteService.isNullOrNotOwned(note, userId)) {
            redirectAttributes.addFlashAttribute("error", Constant.MSG_ERROR);
            return redirectView;
        }

        if (noteService.delete(noteId) > 0) {
            redirectAttributes.addFlashAttribute("delete", String.format(Constant.MSG_DELETED, "note"));
        }
        return redirectView;
    }
}