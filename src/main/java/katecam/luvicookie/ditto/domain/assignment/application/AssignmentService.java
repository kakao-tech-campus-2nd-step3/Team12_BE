package katecam.luvicookie.ditto.domain.assignment.application;

import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentFileRepository;
import katecam.luvicookie.ditto.domain.assignment.dao.AssignmentRepository;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import katecam.luvicookie.ditto.domain.assignment.domain.AssignmentFile;
import katecam.luvicookie.ditto.domain.assignment.dto.*;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudyRepository studyRepository;
    private final AssignmentFileRepository assignmentFileRepository;

    @Value("${file.upload.path}")
    private String filepath;

    public void create(AssignmentRequest assignmentRequest, Integer studyId) {
        //teammate.role이 팀장인지 아닌지
        Assignment assignment = assignmentRequest.toEntity();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        assignment.setStudy(study);
        assignmentRepository.save(assignment);
    }

    @Transactional
    public AssignmentResponse update(Integer assignmentId, AssignmentRequest assignmentRequest) {
        //teammate.role이 팀장인지 아닌지
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        assignment.updateAssignment(assignmentRequest);
        return AssignmentResponse.from(assignment);
    }

    public void delete(Integer assignmentId) {
        //teammate.role이 팀장인지 아닌지
        if(assignmentRepository.existsById(assignmentId)){
            assignmentRepository.deleteById(assignmentId);
        }
    }

    public AssignmentListResponse getAssignments(Pageable pageable, Integer studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));
        Page<AssignmentResponse> assignmentResponses = assignmentRepository.findAllByStudy(pageable, study)
                .map(AssignmentResponse::from);
        return AssignmentListResponse.from(assignmentResponses);
    }

    public AssignmentResponse getAssignment(Integer assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        return AssignmentResponse.from(assignment);
    }

    public AssignmentFileResponse getAssignmentFiles(Integer assignmentId, Member member){
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        List<AssignmentFile> files = assignmentFileRepository.findAllByAssignmentAndMember(assignment, member);
        return AssignmentFileResponse.from(files);
    }

    /*public isTeamReader(TeamMate teamMate){

    }*/

    public AssignmentFileResponse uploadAssignments(Member member, Integer assignmentId, MultipartFile[] files){
        List<AssignmentFile> entities = new ArrayList<>();
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new GlobalException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        for(MultipartFile file : files){
            String fileName = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));
            saveFile(file, fileName);
            entities.add(new AssignmentFile(fileName, assignment, member));
        }
        List<AssignmentFile> assignmentFiles = assignmentFileRepository.saveAll(entities);
        return AssignmentFileResponse.from(assignmentFiles);
    }

    private String generateFileName(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf(".");
        String name = originalFileName.substring(0, lastIndexOfDot);
        String extension = originalFileName.substring(lastIndexOfDot);
        int fileNumber = 1;

        String fileSequence = "";
        while (new File(filepath + name + fileSequence + extension).exists()) {
            fileSequence = "(" + fileNumber + ")";
            fileNumber++;
        }
        return name + fileSequence + extension;
    }

    private void saveFile(MultipartFile file, String fileName) {
        File targetFile = new File(filepath + fileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("File not saved");
        }
    }

    public ResponseEntity<byte[]> downloadFile(Integer fileId) {
        AssignmentFile file = assignmentFileRepository.findById(fileId).orElseThrow(() -> new GlobalException(ErrorCode.FILE_NOT_FOUND));
        byte[] fileContent = readFileContent(file.getFileName());
        String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8);
        return createResponseEntity(encodedFileName, fileContent);
    }

    private byte[] readFileContent(String fileName) {
        Path filePath = Paths.get(filepath).resolve(fileName).normalize();
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private ResponseEntity<byte[]> createResponseEntity(String fileName, byte[] fileContent) {
        String cleanFileName = StringUtils.cleanPath(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", cleanFileName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }
}
