package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    ArrayList<File> getListByUserId(int userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getById(int fileId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int uploadFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(int fileId);

    @Select("SELECT fileName FROM FILES WHERE userId = #{userId} ")
    ArrayList<String> getFileNameListByUserId(int userId);
}
