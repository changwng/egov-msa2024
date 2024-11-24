package org.egovframe.cloud.boardservice.domain.comment;

import org.egovframe.cloud.boardservice.api.comment.dto.CommentListResponseDto;
import org.egovframe.cloud.boardservice.api.comment.dto.CommentRequestDto;
import org.egovframe.cloud.boardservice.config.TestConfig;
import org.egovframe.cloud.boardservice.domain.board.Board;
import org.egovframe.cloud.boardservice.domain.board.BoardRepository;
import org.egovframe.cloud.boardservice.domain.posts.Posts;
import org.egovframe.cloud.boardservice.domain.posts.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostsRepository postsRepository;

    private Board board;
    private Posts posts;
    private Comment comment1;
    private Comment comment2;
    private Comment reply;

    @BeforeEach
    void setUp() {
        // 게시판 생성
        board = Board.builder()
                .boardNo(1)
                .boardName("공지사항")
                .skinTypeCode("normal")
                .titleDisplayLength(100)
                .postDisplayCount(10)
                .pageDisplayCount(10)
                .newDisplayDayCount(3)
                .editorUseAt(true)
                .userWriteAt(true)
                .commentUseAt(true)
                .uploadUseAt(true)
                .uploadLimitCount(3)
                .uploadLimitSize(10)
                .build();
        boardRepository.save(board);

        // 게시물 생성
        posts = Posts.builder()
                .board(board)
                .postsId(1L)
                .title("게시물")
                .content("게시물 내용")
                .postsTypeCode("normal")
                .readCount(0)
                .noticeAt(false)
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .build();
        postsRepository.save(posts);

        // 댓글 생성
        comment1 = Comment.builder()
                .commentId(1L)
                .posts(posts)
                .content("첫 번째 댓글")
                .groupNo(1L)
                .parentCommentNo(null)
                .depth(0)
                .sortSeq(0)
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .build();

        comment2 = Comment.builder()
                .commentId(2L)
                .posts(posts)
                .content("두 번째 댓글")
                .groupNo(2L)
                .parentCommentNo(null)
                .depth(0)
                .sortSeq(0)
                .createdBy("user2")
                .createdDate(LocalDateTime.now())
                .build();

        reply = Comment.builder()
                .commentId(3L)
                .posts(posts)
                .content("첫 번째 댓글의 답글")
                .groupNo(1L)
                .parentCommentNo(1L)
                .depth(1)
                .sortSeq(1)
                .createdBy("user3")
                .createdDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(reply);
    }

    @Test
    @DisplayName("댓글 저장 테스트")
    void save() {
        // given
        Comment comment = Comment.builder()
                .commentId(4L)
                .posts(posts)
                .content("새로운 댓글")
                .groupNo(3L)
                .parentCommentNo(null)
                .depth(0)
                .sortSeq(0)
                .createdBy("user4")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment.getCommentId()).isEqualTo(4L);
        assertThat(savedComment.getContent()).isEqualTo("새로운 댓글");
    }

    @Test
    @DisplayName("댓글 단건 조회 테스트")
    void findById() {
        // when
        Comment foundComment = commentRepository.findById(1L).orElse(null);

        // then
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo("첫 번째 댓글");
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    void findAll() {
        // given
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setPostsId(1L);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<CommentListResponseDto> result = commentRepository.findPage(requestDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("첫 번째 댓글");
        assertThat(result.getContent().get(1).getContent()).isEqualTo("첫 번째 댓글의 답글");
        assertThat(result.getContent().get(2).getContent()).isEqualTo("두 번째 댓글");
    }

    @Test
    @DisplayName("댓글 그룹 내 정렬 순서 조회 테스트")
    void findMaxSortSeqByGroupNo() {
        // when
        Integer maxSortSeq = commentRepository.findMaxSortSeqByGroupNo(1L);

        // then
        assertThat(maxSortSeq).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete() {
        // when
        commentRepository.deleteById(1L);
        Comment deletedComment = commentRepository.findById(1L).orElse(null);

        // then
        assertThat(deletedComment).isNull();
    }

    @Test
    @DisplayName("게시물의 댓글 수 조회 테스트")
    void countByPostsId() {
        // when
        long count = commentRepository.countByPostsId(1L);

        // then
        assertThat(count).isEqualTo(3);
    }
}
