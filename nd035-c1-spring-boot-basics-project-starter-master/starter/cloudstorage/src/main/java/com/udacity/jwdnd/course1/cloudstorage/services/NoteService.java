package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public ArrayList<Note> getListByUserId(int userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public int insertOrUpdate(Note note) {
        return note.getNoteId() == null ? noteMapper.insert(note) : noteMapper.update(note);
    }

    public Note getNoteById(int noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public int delete(int noteId) {
        return noteMapper.delete(noteId);
    }

    public boolean isNullOrNotOwned(Note note, int userId){
        return note == null || note.getUserId() != userId;
    }
}
