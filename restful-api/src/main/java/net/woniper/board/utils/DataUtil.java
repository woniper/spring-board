package net.woniper.board.utils;

import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.support.dto.BoardDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by woniper on 15. 2. 7..
 */
public class DataUtil {

    private final static ModelMapper modelMapper = new ModelMapper();
    private DataUtil() {}

    public static <T> Page<T> convert(Page<?> source, Class<T> type, Pageable pageable) {
        List<?> convertContents = source.getContent().stream()
                .map(data -> modelMapper.map(data, type))
                .collect(Collectors.toList());
        return new PageImpl<>((List<T>) convertContents, pageable, source.getTotalElements());
    }

    public static Page<BoardDto.ListResponse> getListBoardPage(Page<Board> boards, Pageable pageable) {
        List<Board> boardList = boards.getContent();
        List<BoardDto.ListResponse> boardListResponses = boardList.parallelStream()
                .map(board -> modelMapper.map(board, BoardDto.ListResponse.class))
                .collect(Collectors.toList());

        if(boardListResponses != null && !boardListResponses.isEmpty()) {
            int size = boardListResponses.size();

            for (int i = 0; i < size; i++) {
                User user = boardList.get(i).getUser();
                BoardDto.ListResponse boardDto = boardListResponses.get(i);

                boardDto.setUserId(user.getUserId());
                boardDto.setUsername(user.getUsername());
                boardDto.setNickName(user.getNickName());
                boardDto.setAuthorityType(user.getAuthorityType());

                boardDto.setKindBoardName(boardList.get(i).getKindBoard().getKindBoardName());
            }
        }
        return new PageImpl<>(boardListResponses, pageable, boards.getTotalElements());
    }
}
