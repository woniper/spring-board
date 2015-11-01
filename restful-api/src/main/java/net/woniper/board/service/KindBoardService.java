package net.woniper.board.service;

import net.woniper.board.domain.KindBoard;

/**
 * Created by woniper on 2015. 10. 30..
 */
public interface KindBoardService {

    KindBoard getKindBoard(Long kindBoardId);

    KindBoard getKindBoard(String kindBoardName);
}
