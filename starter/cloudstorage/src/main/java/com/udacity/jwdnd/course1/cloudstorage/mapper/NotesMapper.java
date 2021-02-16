package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("Select * from NOTES where userid = #{userid}")
    public List<Note> getUserNotes(Integer userid);

    @Select("Select * from NOTES where userid = #{userid} and noteid = #{noteId}")
    public Note getUserNote(Integer userId, Integer noteId);

    @Insert("INSERT into NOTES(notetitle, notedescription, userid) VALUES(#{noteTitle}, " +
            "#{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public Integer insertUserNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} where " +
            "userid = #{userId} AND noteid = #{noteId}")
    public void updateUserNote(Note note);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    public void deleteUserNote(Integer userId, Integer noteId);
}
