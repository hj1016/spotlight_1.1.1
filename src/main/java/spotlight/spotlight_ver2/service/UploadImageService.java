package spotlight.spotlight_ver2.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadImageService {
    public String uploadImage(MultipartFile file) throws IOException {
        // Firebase Storage의 버킷 가져오기
        Bucket bucket = StorageClient.getInstance().bucket();

        // 고유한 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String blobName = "user_documents/" + UUID.randomUUID() + "_" + originalFileName;

        // 파일 업로드
        Blob blob = bucket.create(blobName, file.getInputStream(), file.getContentType());

        // 파일을 공개로 설정
        blob.createAcl(com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER));

        // 파일 URL 가져오기 (파일 이름 인코딩)
        String encodedBlobName = java.net.URLEncoder.encode(blobName, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), encodedBlobName);

        return fileUrl;
    }

    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileUrl = uploadImage(file);
                fileUrls.add(fileUrl);
            }
        } return fileUrls;
    }
}
