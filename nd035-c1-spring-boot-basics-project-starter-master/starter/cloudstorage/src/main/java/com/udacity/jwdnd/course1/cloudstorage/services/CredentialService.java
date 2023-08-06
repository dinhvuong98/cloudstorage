package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Constant.Constant;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public ArrayList<Credential> getListByUserId(int userId) {
        return credentialMapper.getListByUserId(userId);
    }

    public ArrayList<String> getDecryptPassword(ArrayList<Credential> credentials) {
        var passwords = new ArrayList<String>();
        for (Credential credential : credentials) {
            passwords.add(encryptionService.decryptValue(credential.getPassword(), Constant.KEY_ENCRYPTION));
        }
        return passwords;
    }

    public int insertOrUpdate(Credential credential) {
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), Constant.KEY_ENCRYPTION);
        credential.setKey(Constant.KEY_ENCRYPTION);
        credential.setPassword(encryptPassword);
        return credential.getCredentialId() == null ? credentialMapper.insert(credential) : credentialMapper.update(credential);
    }

    public Credential getById(int credentialId) {
        return credentialMapper.getById(credentialId);
    }

    public int delete(int credentialId) {
        return credentialMapper.delete(credentialId);
    }

    public boolean isNullOrNotOwned(Credential credential, int userId) {
        return credential == null || credential.getUserId() != userId;
    }

}
