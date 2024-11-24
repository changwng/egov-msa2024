package org.egovframe.cloud.boardservice.service.comment;

import org.egovframe.cloud.boardservice.api.comment.dto.*;
import org.egovframe.cloud.boardservice.domain.comment.Comment;
import org.egovframe.cloud.boardservice.domain.comment.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private Comment comment;
    private Comment replyComment;
    private CommentSaveRequestDto saveRequestDto;
    private CommentUpdateRequestDto updateRequestDto;
    private CommentListResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        comment = Comment.builder()
                .commentId(new Comment.CommentId(1, 1, 1))
                .commentContent("테스트 댓글입니다.")
                .groupNo(1)
                .parentCommentNo(null)
                .depthSeq(0)
                .sortSeq(1)
                .deleteAt(0)
                .createdBy("user1")
                .build();

        replyComment = Comment.builder()
                .commentId(new Comment.CommentId(1, 1, 2))
                .commentContent("테스트 답글입니다.")
                .groupNo(1)
                .parentCommentNo(1)
                .depthSeq(1)
                .sortSeq(2)
                .deleteAt(0)
                .createdBy("user2")
                .build();

        saveRequestDto = CommentSaveRequestDto.builder()
                .commentContent("테스트 댓글입니다.")
                .build();

        updateRequestDto = CommentUpdateRequestDto.builder()
                .commentContent("수정된 댓글입니다.")
                .build();

        responseDto = new CommentListResponseDto(comment);
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    void save() {
        // given
        given(commentRepository.findNextCommentNo(1, 1)).willReturn(1);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        CommentListResponseDto result = commentService.save(1, 1, saveRequestDto);

        // then
        assertThat(result.getCommentContent()).isEqualTo("테스트 댓글입니다.");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("답글 등록 테스트")
    void saveReply() {
        // given
        given(commentRepository.findById(any(Comment.CommentId.class))).willReturn(Optional.of(comment));
        given(commentRepository.findNextCommentNo(1, 1)).willReturn(2);
        given(commentRepository.save(any(Comment.class))).willReturn(replyComment);

        // when
        CommentListResponseDto result = commentService.saveReply(1, 1, 1, saveRequestDto);

        // then
        assertThat(result.getCommentContent()).isEqualTo("테스트 답글입니다.");
        assertThat(result.getParentCommentNo()).isEqualTo(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void update() {
        // given
        given(commentRepository.findById(any(Comment.CommentId.class))).willReturn(Optional.of(comment));

        // when
        CommentListResponseDto result = commentService.update(1, 1, 1, updateRequestDto);

        // then
        assertThat(result.getCommentContent()).isEqualTo("수정된 댓글입니다.");
        verify(commentRepository, times(1)).findById(any(Comment.CommentId.class));
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete() {
        // given
        given(commentRepository.findById(any(Comment.CommentId.class))).willReturn(Optional.of(comment));

        // when
        commentService.delete(1, 1, 1);

        // then
        verify(commentRepository, times(1)).findById(any(Comment.CommentId.class));
        assertThat(comment.getDeleteAt()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    void findAll() {
        // given
        List<CommentListResponseDto> comments = Arrays.asList(responseDto);
        given(commentRepository.findAll(1, 1, 0)).willReturn(comments);

        // when
        List<CommentListResponseDto> result = commentService.findAll(1, 1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCommentContent()).isEqualTo("테스트 댓글입니다.");
        verify(commentRepository, times(1)).findAll(1, 1, 0);
    }

    @Test
    @DisplayName("댓글 페이지 조회 테스트")
    void findPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, Object> result = new HashMap<>();
        result.put("totalElements", 1L);
        result.put("comments", Arrays.asList(responseDto));
        
        given(commentRepository.findPage(1, 1, 0, pageable)).willReturn(result);

        // when
        Map<String, Object> pageResult = commentService.findPage(1, 1, pageable);

        // then
        assertThat(pageResult.get("totalElements")).isEqualTo(1L);
        List<CommentListResponseDto> comments = (List<CommentListResponseDto>) pageResult.get("comments");
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getCommentContent()).isEqualTo("테스트 댓글입니다.");
        verify(commentRepository, times(1)).findPage(1, 1, 0, pageable);
    }
}
