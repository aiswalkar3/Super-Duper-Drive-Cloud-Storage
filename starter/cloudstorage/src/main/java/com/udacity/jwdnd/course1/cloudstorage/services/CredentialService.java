package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    public String addOrUpdateCredential(Credential credential)
    {
        try {
            if (credential.getCredentialId() == null) {

                Credential existingCredential = credentialMapper
                        .getUserCredentialByUrlAndUsername(credential.getUrl(),credential.getUsername());

                if(existingCredential != null)
                {
                    return "Credential with same URL and username exists. Please edit the existing credential.";
                }

                SecureRandom secureRandom = new SecureRandom();
                byte[] salt = new byte[16];
                secureRandom.nextBytes(salt);
                String key = Base64.getEncoder().encodeToString(salt);
                String encodedPassword = encryptionService.encryptValue(credential.getPassword(), key);
                credentialMapper.insertUserCredential(new Credential(null, credential.getUrl(), credential.getUsername(),
                        key, encodedPassword, credential.getUserId()));
            } else {
                Credential storedCredential = credentialMapper
                        .getUserCredential(credential.getUserId(), credential.getCredentialId());

                String storedSalt = storedCredential.getKey();
                String enteredPassword = credential.getPassword();
                String storedPassword = storedCredential.getPassword();

                String decryptedPassword = encryptionService.decryptValue(storedPassword, storedSalt);
                String encodedPassword = encryptionService.encryptValue(enteredPassword, storedSalt);
                credential.setPassword(encodedPassword);

                credentialMapper.updateUserCredential(credential);
            }

            return "success";
        }
        catch(Exception e)
        {
            System.out.println("There was an error saving or updating an existing credential. Please try again.");
            e.printStackTrace();
            return "failure";
        }
    }

    public List<Credential> getUserCredentials(Integer userId)
    {
        List<Credential> listCredentials = credentialMapper.getUserCredentials(userId);

        for(Credential credential:listCredentials)
        {
            String encodedSalt = credential.getKey();

            String encryptedPassword = credential.getPassword();

            credential.setDecryptedPassword(encryptionService.decryptValue(encryptedPassword,encodedSalt));
        }

        return listCredentials;
    }

    public String deleteCredential(Integer userId, Integer noteId)
    {
        try {
            credentialMapper.deleteUserCredential(userId, noteId);
            return "success";
        }
        catch(Exception e)
        {
            System.out.println("There was an error deleting an existing credential. Please try again.");
            e.printStackTrace();
            return "failure";
        }
    }
}
