package katecam.luvicookie.ditto.domain.study.dao;

import katecam.luvicookie.ditto.domain.study.domain.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyRepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Test
    @DisplayName("스터디 생성, 조회를 성공해야 한다")
    void should_successfully_create_and_read_study() {
        Study study = Study.builder()
                .name("스터디")
                .description("스터디 소개")
                .topic("스터디 주제")
                .isOpen(true)
                .profileImage("https://test-study.com")
                .build();

        Study savedStudy = studyRepository.save(study);
        Study foundStudy = studyRepository.findById(savedStudy.getId())
                .orElse(null);

        assertThat(foundStudy).isNotNull();
        assertThat(foundStudy.getId()).isEqualTo(savedStudy.getId());
    }

    @Test
    @DisplayName("스터디 검색 필터링 인자가 없어도 스터디 검색을 성공해야 한다")
    void should_successfully_search_study_when_query_parameters_are_null() {
        String name = "3220ae3584858d7798de655286eeab1f";

        Study study = Study.builder()
                .name(name)
                .description("스터디 소개")
                .topic("스터디 주제")
                .isOpen(true)
                .profileImage("https://test-study.com")
                .build();

        Study savedStudy = studyRepository.save(study);

        Page<Study> studies = studyRepository.findAllByTopicAndNameAndIsOpen(null, name.substring(3, 10), true, PageRequest.of(0, 10));
        List<Study> studyList = studies.toList();

        assertThat(studyList.isEmpty()).isFalse();
        assertThat(studyList.getFirst().getId()).isEqualTo(savedStudy.getId());
    }

}