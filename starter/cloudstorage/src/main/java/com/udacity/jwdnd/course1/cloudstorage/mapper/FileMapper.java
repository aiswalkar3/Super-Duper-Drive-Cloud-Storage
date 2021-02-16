package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("Select * from FILES where userid = #{userId}")
    public List<File> getUserFiles(Integer userId);

    @Select("Select * from FILES where userid = #{userId} and filename like #{fileName}")
    public File getUserFileByFileName(Integer userId, String fileName);

    @Select("Select * from FILES where userid = #{userId} and fileId = #{fileId}")
    public File getUserFile(Integer userId, Integer fileId);

    @Insert("INSERT into FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, " +
            "#{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public Integer insertUserFile(File file);

    @Delete("DELETE FROM FILES WHERE userid = #{userId} AND fileId = #{fileId}")
    public void deleteUserFile(Integer userId, Integer fileId);
}
