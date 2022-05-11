package temur.uz.filedownload.file_service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    @Autowired
    private FileDBRepository fileDBRepository;
    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] encode = Base64.getEncoder().encode(file.getBytes());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), encode);
        return fileDBRepository.save(fileDB);
    }
    public FileDB getEFile(String id) {
        Optional<FileDB> byId = fileDBRepository.findById(id);
        return byId.orElse(null);
    }
    public FileDB getDFile(String id) {
        Optional<FileDB> byId = fileDBRepository.findById(id);
        byte[] decode = Base64.getDecoder().decode(byId.get().getData());
        byId.get().setData(decode);
        return byId.orElse(null);
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}