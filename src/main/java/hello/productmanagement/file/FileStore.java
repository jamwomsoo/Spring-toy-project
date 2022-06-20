package hello.productmanagement.file;

import hello.productmanagement.domain.DownloadFile;
import hello.productmanagement.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException{
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); //업로드 파일명
        String storeFileName = createStoreFileName(originalFilename);  //서버 내부에서 관리하는 파일명
        multipartFile.transferTo(new File(getFullPath(storeFileName))); //파일 저장
        return new UploadFile(originalFilename, storeFileName);
    }
    //서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID를 사용해서 충돌하지 않도록 한다
    private String createStoreFileName(String originalFilename){
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    //확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다
    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
    //////////////////////////////////////////

    public List<String> deStoreFiles(List<UploadFile> uploadFiles) {
        List<String> fileResult = new ArrayList<>();
        for (UploadFile uploadFile : uploadFiles){
            if(!Objects.isNull(uploadFile)){
               fileResult.add(deStoreFile(uploadFile));
            }
        }
        return fileResult;
    }



    public String deStoreFile(UploadFile uploadFile){
        if(Objects.isNull(uploadFile)){
            return null;
        }

        return  uploadFile.getStoreFileName();
    }



}
