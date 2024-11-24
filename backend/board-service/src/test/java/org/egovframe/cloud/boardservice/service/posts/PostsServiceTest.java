package org.egovframe.cloud.boardservice.service.posts;

import org.egovframe.cloud.boardservice.api.posts.dto.*;
import org.egovframe.cloud.boardservice.domain.posts.Posts;
import org.egovframe.cloud.boardservice.domain.posts.PostsRepository;
import org.egovframe.cloud.boardservice.domain.posts.PostsRead;
import org.egovframe.cloud.boardservice.domain.posts.PostsReadRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @InjectMocks
    private PostsService postsService;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private PostsReadRepository postsReadRepository;

    private Posts posts;
    private PostsSaveRequestDto saveRequestDto;
    private PostsUpdateRequestDto updateRequestDto;
    private PostsResponseDto responseDto;
    private PostsRead postsRead;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        posts = Posts.builder()
                .postsId(new Posts.PostsId(1, 1))
                .postsTitle("테스트 게시글")
                .postsContent("테스트 내용입니다.")
                .postsAnswerContent(null)
                .attachmentCode("ATTACH001")
                .readCount(0)
                .noticeAt(false)
                .deleteAt(0)
                .createdBy("user1")
                .build();

        saveRequestDto = PostsSaveRequestDto.builder()
                .postsTitle("테스트 게시글")
                .postsContent("테스트 내용입니다.")
                .attachmentCode("ATTACH001")
                .noticeAt(false)
                .build();

        updateRequestDto = PostsUpdateRequestDto.builder()
                .postsTitle("수정된 게시글")
                .postsContent("수정된 내용입니다.")
                .attachmentCode("ATTACH002")
                .noticeAt(true)
                .build();

        responseDto = new PostsResponseDto(posts);

        postsRead = PostsRead.builder()
                .boardNo(1)
                .postsNo(1)
                .readNo(1)
                .userId("user1")
                .ipAddr("127.0.0.1")
                .build();
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void save() {
        // given
        given(postsRepository.findNextPostsNo(1)).willReturn(1);
        given(postsRepository.save(any(Posts.class))).willReturn(posts);

        // when
        PostsResponseDto result = postsService.save(1, saveRequestDto);

        // then
        assertThat(result.getPostsTitle()).isEqualTo("테스트 게시글");
        assertThat(result.getPostsContent()).isEqualTo("테스트 내용입니다.");
        verify(postsRepository, times(1)).save(any(Posts.class));
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void update() {
        // given
        given(postsRepository.findById(any(Posts.PostsId.class))).willReturn(Optional.of(posts));

        // when
        PostsResponseDto result = postsService.update(1, 1, updateRequestDto);

        // then
        assertThat(result.getPostsTitle()).isEqualTo("수정된 게시글");
        assertThat(result.getPostsContent()).isEqualTo("수정된 내용입니다.");
        verify(postsRepository, times(1)).findById(any(Posts.PostsId.class));
    }

    @Test
    @DisplayName("게시글 단건 조회 테스트")
    void findById() {
        // given
        given(postsRepository.findById(any(Posts.PostsId.class))).willReturn(Optional.of(posts));
        given(postsReadRepository.countByBoardNoAndPostsNoAndUserId(1, 1, "user1", "127.0.0.1")).willReturn(0L);
        given(postsReadRepository.findNextReadNo(1, 1)).willReturn(1);
        given(postsReadRepository.save(any(PostsRead.class))).willReturn(postsRead);

        // when
        PostsResponseDto result = postsService.findById(1, 1, 0, "user1", "127.0.0.1", null);

        // then
        assertThat(result.getPostsTitle()).isEqualTo("테스트 게시글");
        assertThat(result.getPostsContent()).isEqualTo("테스트 내용입니다.");
        verify(postsRepository, times(1)).findById(any(Posts.PostsId.class));
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void findAll() {
        // given
        List<Posts> postsList = Arrays.asList(posts);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Posts> page = new PageImpl<>(postsList, pageable, postsList.size());
        
        given(postsRepository.findAll(pageable)).willReturn(page);

        // when
        Page<PostsListResponseDto> result = postsService.findAll(1, new PostsRequestDto(), pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPostsTitle()).isEqualTo("테스트 게시글");
        verify(postsRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void delete() {
        // given
        given(postsRepository.findById(any(Posts.PostsId.class))).willReturn(Optional.of(posts));

        // when
        postsService.remove(1, 1, "user1");

        // then
        verify(postsRepository, times(1)).findById(any(Posts.PostsId.class));
        assertThat(posts.getDeleteAt()).isEqualTo(1);
    }

    @Test
    @DisplayName("공지사항 목록 조회 테스트")
    void findNoticeList() {
        // given
        List<Posts> noticeList = Arrays.asList(posts);
        given(postsRepository.findAllByBoardNoAndNoticeAtAndDeleteAt(1, true, 0)).willReturn(noticeList);

        // when
        List<PostsListResponseDto> result = postsService.findNoticeList(1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPostsTitle()).isEqualTo("테스트 게시글");
        verify(postsRepository, times(1)).findAllByBoardNoAndNoticeAtAndDeleteAt(1, true, 0);
    }
}
