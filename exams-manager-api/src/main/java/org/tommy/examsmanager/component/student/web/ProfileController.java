package org.tommy.examsmanager.component.student.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tommy.examsmanager.component.exam.usecase.DismissExamService;
import org.tommy.examsmanager.component.student.usecase.FindStudentService;
import org.tommy.examsmanager.component.token.TokenExtractor;
import org.tommy.examsmanager.shared.web.WebUtils;

@RestController
@RequestMapping("/profile")
public class ProfileController {

  private final TokenExtractor tokenExtractor;

  private final FindStudentService findStudentService;

  private final DismissExamService dismissExamService;

  public ProfileController(final TokenExtractor tokenExtractor,
                           final FindStudentService findStudentService,
                           final DismissExamService dismissExamService) {
    this.tokenExtractor = tokenExtractor;
    this.findStudentService = findStudentService;
    this.dismissExamService = dismissExamService;
  }

  @PatchMapping("/{visible}")
  public ResponseEntity<UpdateVisibilityHttpReq> updateProfileVisibility(@PathVariable("visible") boolean visible,
                                                                         HttpServletRequest httpReq) {
    String studentId = validateTokenAndGetStudentId(httpReq, tokenExtractor);
    boolean visibleSaved = findStudentService.updateProfileVisibility(studentId, visible);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new UpdateVisibilityHttpReq(visibleSaved));
  }

  @PatchMapping("/dismiss/exam/{examId}")
  public ResponseEntity<Void> dismissExam(
      @PathVariable String examId,
      HttpServletRequest httpReq
  ) {
    String studentId = validateTokenAndGetStudentId(httpReq, tokenExtractor);
    dismissExamService.dismiss(studentId, examId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<?> myProfile(
      HttpServletRequest request
  ) {
    //TODO finish this
    String studentId  = validateTokenAndGetStudentId(request, tokenExtractor);
    return null;
  }

  @GetMapping("/exams")
  public ResponseEntity<?> getExams(
      HttpServletRequest request
  ) {
    // TODO finish this
    String studentId = validateTokenAndGetStudentId(request, tokenExtractor);
    return null;
  }

  private static String validateTokenAndGetStudentId(
      HttpServletRequest request,
      TokenExtractor tokenExtractor
  ) {
    String token = WebUtils.getAuthorizationToken(request);
    tokenExtractor.validate(token);
    return tokenExtractor.getStudentId(token);
  }

  //TODO finish API dismiss exams with a background service
}
