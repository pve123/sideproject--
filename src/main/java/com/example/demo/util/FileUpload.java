package com.example.demo.util;

import com.example.demo.domain.board.entity.Image;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
public class FileUpload {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public List<Image> setUploadFiles(MultipartFile[] uploadFiles) {


        List<Image> imageList = Lists.newArrayList();

        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일만 업로드 가능
            if (uploadFile.getContentType().startsWith("image") == false) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다.");
            }
            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = Image.builder().uuidFileName(uuid + "_" + fileName).originName(originalName).savePath(savePath.toString()).build();
            imageList.add(image);
        }
        return imageList;

    }

    private String makeFolder() {

        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File uploadPatheFolder = new File(uploadPath, folderPath);
        if (uploadPatheFolder.exists() == false) {
            uploadPatheFolder.mkdirs();
        }
        return folderPath;
    }
}
