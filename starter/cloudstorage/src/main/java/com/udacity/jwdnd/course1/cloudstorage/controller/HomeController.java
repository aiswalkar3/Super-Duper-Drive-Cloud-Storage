package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/notes/save")
    public RedirectView addOrUpdateUserNote(Authentication authentication, @ModelAttribute("userNoteForm") Note userNoteForm,
                                    Model model, RedirectAttributes redirectAttributes)
    {
        userNoteForm.setUserId(userService.getUser(authentication.getName()).getUserId());
        String noteSaveStatus = noteService.addOrUpdateNote(userNoteForm);

        if(noteSaveStatus.equals("success"))
        {
            redirectAttributes.addFlashAttribute("noteSaveSuccess", true);
        }
        else{
            String notesSaveError = "There was an error saving notes. Please try again.";
            redirectAttributes.addFlashAttribute("notesSaveError", notesSaveError);
        }

        return new RedirectView("/home#nav-notes");
    }

    @RequestMapping("/notes/delete/user/{userId}/note/{noteId}")
    public RedirectView deleteUserNote(Authentication authentication, @PathVariable("userId") String userId,
                                       @PathVariable("noteId") String noteId, Model model,RedirectAttributes redirectAttributes)
    {
        String deleteResult = noteService.deleteNote(Integer.valueOf(userId),Integer.valueOf(noteId));

        if(deleteResult.equals("success"))
        {
            redirectAttributes.addFlashAttribute("noteDeleteSuccess", true);
        }
        else
        {
            String notesDeleteError = "There was an error deleting notes. Please try again.";
            redirectAttributes.addFlashAttribute("noteDeleteError", notesDeleteError);
        }

        return new RedirectView("/home#nav-notes");
    }

    @PostMapping("/credential/save")
    public RedirectView addUserCredential(Authentication authentication, @ModelAttribute("userCredentialForm") Credential userCredentialForm,
                                          Model model, RedirectAttributes redirectAttributes)
    {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        userCredentialForm.setUserId(userId);

        String credentialSaveStatus = credentialService.addOrUpdateCredential(userCredentialForm);

        if(credentialSaveStatus.equals("success"))
        {
            redirectAttributes.addFlashAttribute("credentialSaveSuccess", true);
        }
        else if(credentialSaveStatus.equals("failure")){
            String credentialSaveError = "There was an error saving credential. Please try again.";
            redirectAttributes.addFlashAttribute("credentialSaveError", credentialSaveError);
        }
        else
        {
            redirectAttributes.addFlashAttribute("credentialSaveError", credentialSaveStatus);
        }

        return new RedirectView("/home#nav-credentials");
    }

    @RequestMapping("/credentials/delete/user/{userId}/credential/{credentialId}")
    public RedirectView deleteUserCredential(Authentication authentication, @PathVariable("userId") String userId,
                                       @PathVariable("credentialId") String credentialId, Model model,
                                             RedirectAttributes redirectAttributes)
    {
        String credentialDeleteStatus = credentialService.deleteCredential(Integer.valueOf(userId),Integer.valueOf(credentialId));

        if(credentialDeleteStatus.equals("success"))
        {
            redirectAttributes.addFlashAttribute("credentialDeleteSuccess", true);
        }
        else
        {
            String credentialDeleteError = "There was an error deleting credential. Please try again.";
            redirectAttributes.addFlashAttribute("credentialDeleteError", credentialDeleteError);
        }

        return new RedirectView("/home#nav-credentials");
    }

    @PostMapping("/files/upload")
    public RedirectView uploadUserFile(Authentication authentication, @ModelAttribute("fileUpload")MultipartFile fileUpload,
                                       Model model, RedirectAttributes redirectAttributes) throws IOException
    {

            if (fileUpload != null && (fileUpload.getOriginalFilename() == null
                    || fileUpload.getOriginalFilename().trim().isEmpty())) {
                redirectAttributes.addFlashAttribute("fileUploadError", "Please upload valid file.");
                return new RedirectView("/home#nav-files");
            }

            Integer userId = userService.getUser(authentication.getName()).getUserId();
            String fileUploadStatus = fileStorageService.addUserFile(fileUpload, userId);

            if (fileUploadStatus.equals("success")) {
                redirectAttributes.addFlashAttribute("fileUploadSuccess", true);
            } else if (fileUploadStatus.equals("failure")) {
                String fileUploadError = "There was an error uploading file. Please try again.";
                redirectAttributes.addFlashAttribute("fileUploadError", fileUploadError);
            } else {
                redirectAttributes.addFlashAttribute("fileUploadError", fileUploadStatus);
            }

            return new RedirectView("/home#nav-files");
    }

    @RequestMapping("/files/download/user/{userId}/file/{fileId}")
    public ResponseEntity<byte[]> downloadUserFile(@PathVariable("userId") String userId,
                                                   @PathVariable("fileId") String fileId,
                                                   Model model)
    {
        File retrievedFile = fileStorageService.loadUserFile(Integer.valueOf(userId), Integer.valueOf(fileId));

        if(retrievedFile != null)
        {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + retrievedFile.getFileName() + "\"")
                    .body(retrievedFile.getFileData());
        }
        else {
            return new ResponseEntity<byte[]>(new byte[]{}, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/files/delete/user/{userId}/file/{fileId}")
    public RedirectView deleteUserFile(Authentication authentication, @PathVariable("userId") String userId,
                                             @PathVariable("fileId") String fileId,
                                             Model model, RedirectAttributes redirectAttributes)
    {
        String fileDeleteStatus = fileStorageService.deleteUserFile(Integer.valueOf(userId),Integer.valueOf(fileId));

        if(fileDeleteStatus.equals("success"))
        {
            redirectAttributes.addFlashAttribute("fileDeleteSuccess", true);
        }
        else
        {
            String fileDeleteError = "There was an error deleting file. Please try again.";
            redirectAttributes.addFlashAttribute("fileDeleteError", fileDeleteError);
        }

        return new RedirectView("/home#nav-files");
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model)
    {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("userNoteForm", new Note());
        model.addAttribute("userCredentialForm", new Credential());

        model.addAttribute("allUserNotes",noteService.getUserNotes(userId));
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        model.addAttribute("userFiles",fileStorageService.getUserFiles(userId));
        return "home";
    }
}
