package katecam.luvicookie.ditto.domain.study.application;

import katecam.luvicookie.ditto.domain.file.application.AwsFileService;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private AwsFileService awsFileService;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @InjectMocks
    private StudyService studyService;

    @Test
    @DisplayName("스터디 목록 페이지 조회 성공")
    void getStudyListSuccess() {
        Study study1 = Study.builder()
                .name("테스트 스터디")
                .topic("테스트 주제")
                .isOpen(true)
                .build();
        study1.initCreatedAt();

        Study study2 = Study.builder()
                .name("CS 스터디")
                .topic("CS 주제")
                .isOpen(true)
                .build();
        study2.initCreatedAt();

        List<Study> studyList = Arrays.asList(study1, study2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Study> studies = new PageImpl<>(studyList, pageable, 0);
        StudyCriteria criteria = StudyCriteria.builder()
                .name("스터디")
                .topic("주제")
                .isOpen(true)
                .build();

        given(studyRepository.findAllByTopicAndNameAndIsOpen(criteria.topic(), criteria.name(), criteria.isOpen(), pageable))
                .willReturn(studies);

        StudyListResponse studyListResponse = studyService.getStudyList(pageable, criteria);

        assertThat(studyListResponse).isNotNull();
        assertThat(studyListResponse.studies().size())
                .isEqualTo(studyList.size());
    }

    @Test
    @DisplayName("스터디 목록 페이지 조회 내용 없음")
    void getStudyListFail() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Study> studies = new PageImpl<>(List.of(), pageable, 0);

        given(studyRepository.findAllByTopicAndNameAndIsOpen(any(), any(), any(), eq(pageable)))
                .willReturn(studies);

        StudyCriteria criteria = StudyCriteria.builder()
                .build();

        StudyListResponse studyListResponse = studyService.getStudyList(pageable, criteria);

        assertThat(studyListResponse).isNotNull();
        assertThat(studyListResponse.studies()).isEmpty();
    }

    @Test
    void create() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }

    @Test
    void updateProfileImage() {
    }

}