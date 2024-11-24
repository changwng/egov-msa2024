package org.egovframe.cloud.boardservice.domain.posts;

import org.egovframe.cloud.boardservice.api.posts.dto.PostsListResponseDto;
import org.egovframe.cloud.boardservice.api.posts.dto.PostsRequestDto;
import org.egovframe.cloud.boardservice.config.TestConfig;
import org.egovframe.cloud.boardservice.domain.board.Board;
import org.egovframe.cloud.boardservice.domain.board.BoardRepository;
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
class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Board board;
    private Posts posts1;
    private Posts posts2;

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
        posts1 = Posts.builder()
                .board(board)
                .postsId(1L)
                .title("첫 번째 게시물")
                .content("첫 번째 게시물 내용")
                .postsTypeCode("normal")
                .readCount(0)
                .noticeAt(false)
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .build();

        posts2 = Posts.builder()
                .board(board)
                .postsId(2L)
                .title("두 번째 게시물")
                .content("두 번째 게시물 내용")
                .postsTypeCode("normal")
                .readCount(0)
                .noticeAt(true)
                .createdBy("user2")
                .createdDate(LocalDateTime.now())
                .build();

        postsRepository.save(posts1);
        postsRepository.save(posts2);
    }

    @Test
    @DisplayName("게시물 저장 테스트")
    void save() {
        // given
        Posts posts = Posts.builder()
                .board(board)
                .postsId(3L)
                .title("새로운 게시물")
                .content("새로운 게시물 내용")
                .postsTypeCode("normal")
                .readCount(0)
                .noticeAt(false)
                .createdBy("user3")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        Posts savedPosts = postsRepository.save(posts);

        // then
        assertThat(savedPosts.getPostsId()).isEqualTo(3L);
        assertThat(savedPosts.getTitle()).isEqualTo("새로운 게시물");
    }

    @Test
    @DisplayName("게시물 단건 조회 테스트")
    void findById() {
        // when
        Posts foundPosts = postsRepository.findById(1L).orElse(null);

        // then
        assertThat(foundPosts).isNotNull();
        assertThat(foundPosts.getTitle()).isEqualTo("첫 번째 게시물");
    }

    @Test
    @DisplayName("게시물 목록 조회 테스트")
    void findAll() {
        // given
        PostsRequestDto requestDto = new PostsRequestDto();
        requestDto.setBoardNo(1);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PostsListResponseDto> result = postsRepository.findPage(requestDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("두 번째 게시물"); // 공지사항이 먼저 나옴
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("첫 번째 게시물");
    }

    @Test
    @DisplayName("게시물 검색 테스트")
    void search() {
        // given
        PostsRequestDto requestDto = new PostsRequestDto();
        requestDto.setBoardNo(1);
        requestDto.setKeyword("첫 번째");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PostsListResponseDto> result = postsRepository.findPage(requestDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("첫 번째 게시물");
    }

    @Test
    @DisplayName("공지사항 목록 조회 테스트")
    void findNoticeList() {
        // given
        PostsRequestDto requestDto = new PostsRequestDto();
        requestDto.setBoardNo(1);

        // when
        List<PostsListResponseDto> result = postsRepository.findNoticeList(requestDto);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("두 번째 게시물");
        assertThat(result.get(0).getNoticeAt()).isTrue();
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    void delete() {
        // when
        postsRepository.deleteById(1L);
        Posts deletedPosts = postsRepository.findById(1L).orElse(null);

        // then
        assertThat(deletedPosts).isNull();
    }
}
