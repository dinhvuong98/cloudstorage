package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public ArrayList<File> getListByUserId(int userId) {
        return fileMapper.getListByUserId(userId);
    }

    public File getById(int userId) {
        return fileMapper.getById(userId);
    }

    public int uploadFile(File file) {
        return fileMapper.uploadFile(file);
    }

    public int delete(int fileId) {
        return fileMapper.delete(fileId);
    }

    public boolean isNullOrNotOwned(File file, int userId){
        return file == null || file.getUserId() != userId;
    }

    public boolean isExistFileName(int userId, String fileName){
        return fileMapper.getFileNameListByUserId(userId).contains(fileName);
    }
}
