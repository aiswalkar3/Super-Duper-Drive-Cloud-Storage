package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NotesMapper notesMapper;

    public String addOrUpdateNote(Note note)
    {
        try {
            if (note.getNoteId() == null) {
                Integer noteId = notesMapper.insertUserNote(new Note(null, note.getNoteTitle(),
                        note.getNoteDescription(), note.getUserId()));
            } else {
                notesMapper.updateUserNote(note);
            }

            return "success";
        }
        catch(Exception e)
        {
            System.out.println("There was an error saving or updating an existing note. Please try again.");
            e.printStackTrace();
            return "failure";
        }
    }

    public List<Note> getUserNotes(Integer userId)
    {
        return notesMapper.getUserNotes(userId);
    }

    public String deleteNote(Integer userId, Integer noteId)
    {
        try {
            notesMapper.deleteUserNote(userId, noteId);
            return "success";
        }
        catch(Exception e){
            System.out.println("There was an error deleting an existing note. Please try again.");
            e.printStackTrace();
            return "failure";
        }
    }
}
