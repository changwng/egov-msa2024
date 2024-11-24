package org.egovframe.cloud.boardservice.service.board;

import org.egovframe.cloud.boardservice.api.board.dto.*;
import org.egovframe.cloud.boardservice.domain.board.Board;
import org.egovframe.cloud.boardservice.domain.board.BoardRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    private Board board;
    private BoardSaveRequestDto saveRequestDto;
    private BoardUpdateRequestDto updateRequestDto;
    private BoardResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
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

        saveRequestDto = BoardSaveRequestDto.builder()
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

        updateRequestDto = BoardUpdateRequestDto.builder()
                .boardName("수정된 공지사항")
                .skinTypeCode("gallery")
                .titleDisplayLength(200)
                .postDisplayCount(20)
                .pageDisplayCount(20)
                .newDisplayDayCount(5)
                .editorUseAt(false)
                .userWriteAt(false)
                .commentUseAt(false)
                .uploadUseAt(false)
                .uploadLimitCount(5)
                .uploadLimitSize(20)
                .build();

        responseDto = new BoardResponseDto(board);
    }

    @Test
    @DisplayName("게시판 생성 테스트")
    void save() {
        // given
        given(boardRepository.save(any(Board.class))).willReturn(board);

        // when
        BoardResponseDto result = boardService.save(saveRequestDto);

        // then
        assertThat(result.getBoardName()).isEqualTo("공지사항");
        assertThat(result.getSkinTypeCode()).isEqualTo("normal");
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("게시판 수정 테스트")
    void update() {
        // given
        given(boardRepository.findById(1)).willReturn(Optional.of(board));

        // when
        BoardResponseDto result = boardService.update(1, updateRequestDto);

        // then
        assertThat(result.getBoardName()).isEqualTo("수정된 공지사항");
        assertThat(result.getSkinTypeCode()).isEqualTo("gallery");
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("게시판 단건 조회 테스트")
    void findById() {
        // given
        given(boardRepository.findById(1)).willReturn(Optional.of(board));

        // when
        BoardResponseDto result = boardService.findById(1);

        // then
        assertThat(result.getBoardNo()).isEqualTo(1);
        assertThat(result.getBoardName()).isEqualTo("공지사항");
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("게시판 목록 조회 테스트")
    void findAll() {
        // given
        List<Board> boards = Arrays.asList(board);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> page = new PageImpl<>(boards, pageable, boards.size());
        
        given(boardRepository.findAll(pageable)).willReturn(page);

        // when
        Page<BoardListResponseDto> result = boardService.findAll(new BoardRequestDto(), pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBoardName()).isEqualTo("공지사항");
        verify(boardRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("게시판 삭제 테스트")
    void delete() {
        // given
        given(boardRepository.findById(1)).willReturn(Optional.of(board));

        // when
        boardService.delete(1);

        // then
        verify(boardRepository, times(1)).delete(any(Board.class));
    }
}
