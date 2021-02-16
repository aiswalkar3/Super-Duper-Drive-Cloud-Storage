package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("Select * from CREDENTIALS where userid = #{userId}")
    public List<Credential> getUserCredentials(Integer userId);

    @Select("Select * from CREDENTIALS where userid = #{userId} and credentialid = #{credentialId}")
    public Credential getUserCredential(Integer userId, Integer credentialId);

    @Select("Select * from CREDENTIALS where url like #{url} and username like #{userName}")
    public Credential getUserCredentialByUrlAndUsername(String url, String userName);

    @Insert("INSERT into CREDENTIALS(url, username, key, password, userid) VALUES(#{url}, " +
            "#{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public Integer insertUserCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, " +
            "password = #{password} " +
            "where credentialid = #{credentialId} AND userid = #{userId}")
    public void updateUserCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    public void deleteUserCredential(Integer userId, Integer credentialId);
}
