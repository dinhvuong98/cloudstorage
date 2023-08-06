package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    ArrayList<Note> getNotesByUserId(int userId);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES SET noteTitle= #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES WHERE NoteId = #{noteId}")
    int delete(int noteId);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNoteById(int noteId);
}
