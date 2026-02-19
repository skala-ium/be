package com.example.skala_ium.global.scheduler;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.course.domain.entity.Enrollment;
import com.example.skala_ium.course.infrastructure.EnrollmentRepository;
import com.example.skala_ium.global.slack.SlackNotificationService;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.domain.entity.Student;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadlineReminderScheduler {

    private final AssignmentRepository assignmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final SlackNotificationService slackNotificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDeadlineReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusHours(24);

        List<Assignment> assignments = assignmentRepository.findAll().stream()
            .filter(a -> a.getDeadline().isAfter(now) && a.getDeadline().isBefore(deadline))
            .toList();

        for (Assignment assignment : assignments) {
            Long courseId = assignment.getCourse().getId();
            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

            for (Enrollment enrollment : enrollments) {
                Student student = enrollment.getStudent();
                boolean hasSubmitted = submissionRepository
                    .existsByAssignmentIdAndStudentId(assignment.getId(), student.getId());

                if (!hasSubmitted) {
                    String message = String.format(
                        "[마감 임박] '%s' 과제의 마감일이 %s입니다. 아직 제출하지 않았습니다.",
                        assignment.getTitle(),
                        assignment.getDeadline().toString()
                    );
                    slackNotificationService.sendDirectMessage(student.getSlackUserId(), message);
                }
            }
        }

        log.info("마감 리마인드 스케줄러 실행 완료: {}개 과제 처리", assignments.size());
    }
}
