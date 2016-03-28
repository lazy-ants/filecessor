package model;

import com.lazyants.filecessor.model.PhotoFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class PhotoFileConverter {
    public static MultiValueMap<String, String> convert(PhotoFile file) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        result.add("filePath", file.getFilePath());
        result.add("fileSize", String.valueOf(file.getFileSize()));
        result.add("fileContentType", file.getFileContentType());
        result.add("fileName", file.getFileName());

        return result;
    }
}
