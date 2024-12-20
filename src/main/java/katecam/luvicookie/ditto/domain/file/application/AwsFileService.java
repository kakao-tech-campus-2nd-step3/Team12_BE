package katecam.luvicookie.ditto.domain.file.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import katecam.luvicookie.ditto.domain.assignment.dto.response.FileResponse;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsFileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String STUDY_IMG_DIR = "study";
    private static final String MEMBER_IMG_DIR = "member";
    private static final String ASSIGNMENT_DIR = "assignment";

    public String saveMemberProfileImage(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new GlobalException(ErrorCode.FILE_CONVERT_FAILED));
        return upload(uploadFile, MEMBER_IMG_DIR);
    }

    public FileResponse saveAssignment(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new GlobalException(ErrorCode.FILE_CONVERT_FAILED));
        String fileName = ASSIGNMENT_DIR + "/" + UUID.randomUUID() + uploadFile.getName();
        String fileUrl = uploadAssignment(uploadFile, fileName);
        return FileResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .build();
    }

    public String saveStudyProfileImage(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new GlobalException(ErrorCode.FILE_CONVERT_FAILED));
        return upload(uploadFile, STUDY_IMG_DIR);
    }

    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadFileUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return uploadFileUrl;
    }

    public String uploadAssignment(File uploadFile, String fileName) {
        String uploadFileUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return uploadFileUrl;
    }

    // S3 파일 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬 파일 삭제
    private void removeNewFile(File targetFile) {
        String name = targetFile.getName();

        if (targetFile.delete()) {
            log.info(name + "File delete success");
            return;
        }
        log.info(name + "File delete fail");
    }

    // 로컬 파일 업로드 및 변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.home") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()){
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    public ResponseEntity<byte[]> downloadFile(String fileName) throws IOException {

        S3Object s3Object = amazonS3Client.getObject(bucket, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        byte[] fileContent = inputStream.readAllBytes();
        inputStream.close();

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", encodedFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

}
