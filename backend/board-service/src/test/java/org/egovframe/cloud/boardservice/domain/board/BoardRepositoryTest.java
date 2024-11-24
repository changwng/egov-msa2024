package org.egovframe.cloud.boardservice.domain.board;

import org.egovframe.cloud.boardservice.api.board.dto.BoardListResponseDto;
import org.egovframe.cloud.boardservice.api.board.dto.BoardRequestDto;
import org.egovframe.cloud.boardservice.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    private Board board1;
    private Board board2;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        board1 = Board.builder()
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

        board2 = Board.builder()
                .boardNo(2)
                .boardName("자유게시판")
                .skinTypeCode("gallery")
                .titleDisplayLength(200)
                .postDisplayCount(20)
                .pageDisplayCount(20)
                .newDisplayDayCount(5)
                .editorUseAt(false)
                .userWriteAt(true)
                .commentUseAt(true)
                .uploadUseAt(false)
                .uploadLimitCount(0)
                .uploadLimitSize(0)
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);
    }

    @Test
    @DisplayName("게시판 저장 테스트")
    void save() {
        // given
        Board board = Board.builder()
                .boardNo(3)
                .boardName("QnA")
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

        // when
        Board savedBoard = boardRepository.save(board);

        // then
        assertThat(savedBoard.getBoardNo()).isEqualTo(3);
        assertThat(savedBoard.getBoardName()).isEqualTo("QnA");
    }

    @Test
    @DisplayName("게시판 단건 조회 테스트")
    void findById() {
        // when
        Board foundBoard = boardRepository.findById(1).orElse(null);

        // then
        assertThat(foundBoard).isNotNull();
        assertThat(foundBoard.getBoardName()).isEqualTo("공지사항");
    }

    @Test
    @DisplayName("게시판 목록 조회 테스트")
    void findAll() {
        // given
        BoardRequestDto requestDto = new BoardRequestDto();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardListResponseDto> result = boardRepository.findPage(requestDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getBoardName()).isEqualTo("공지사항");
        assertThat(result.getContent().get(1).getBoardName()).isEqualTo("자유게시판");
    }

    @Test
    @DisplayName("게시판 검색 테스트")
    void search() {
        // given
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setKeyword("공지");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardListResponseDto> result = boardRepository.findPage(requestDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBoardName()).isEqualTo("공지사항");
    }

    @Test
    @DisplayName("게시판 삭제 테스트")
    void delete() {
        // when
        boardRepository.deleteById(1);
        Board deletedBoard = boardRepository.findById(1).orElse(null);

        // then
        assertThat(deletedBoard).isNull();
    }

    @Test
    @DisplayName("게시판 번호 목록으로 조회 테스트")
    void findAllByBoardNoIn() {
        // given
        List<Integer> boardNos = List.of(1, 2);

        // when
        List<Board> boards = boardRepository.findAllById(boardNos);

        // then
        assertThat(boards).hasSize(2);
        assertThat(boards.get(0).getBoardName()).isEqualTo("공지사항");
        assertThat(boards.get(1).getBoardName()).isEqualTo("자유게시판");
    }
}
