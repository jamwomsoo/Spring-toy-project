package hello.productmanagement.domain;

import lombok.Data;

@Data
public class DownloadFile {

    private String downloadFileName;
    private String storeFileName;

    public DownloadFile(String downloadFileName, String storeFileName){
        this.downloadFileName = downloadFileName;
        this.storeFileName = storeFileName;
    }
}
