package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileStorageService {
    @Autowired
    private FileMapper fileMapper;

    public String addUserFile(MultipartFile file, Integer userId)
    {
        try {

            File userFile = fileMapper.getUserFileByFileName(userId,file.getOriginalFilename());

            if(userFile != null)
            {
                return "File with name " + file.getOriginalFilename() + " already exists. " +
                        "Please change the file name and upload again.";
            }

            File newFile = new File();
            newFile.setFileName(file.getOriginalFilename());
            newFile.setFileData(file.getBytes());
            newFile.setContentType(file.getContentType());
            newFile.setFileSize(String.valueOf(file.getSize()));
            newFile.setUserId(userId);
            fileMapper.insertUserFile(newFile);
            return "success";
        }
        catch(IOException e)
        {
            System.out.println("There was an error uploading user file. Please try again.");
            e.printStackTrace();
            return "failure";
        }
        catch(Exception e)
        {
            System.out.println("There was an error uploading user file. Please try again.");
            e.printStackTrace();
            return "failure";
        }
    }

    public List<File> getUserFiles(Integer userId)
    {
        return fileMapper.getUserFiles(userId);
    }

    public File loadUserFile(Integer userId, Integer fileId)
    {
        try {
            File file = fileMapper.getUserFile(userId, fileId);
            return file;
        }
        catch(Exception e)
        {
            System.out.println("There was an error downloading user file. Please try again.");
            return null;
        }
    }

    public String deleteUserFile(Integer userId, Integer fileId)
    {
        try {
            fileMapper.deleteUserFile(userId,fileId);
            return "success";
        }
        catch(Exception e)
        {
            System.out.println("There was an error deleting user file. Please try again.");
            return null;
        }
    }
}
