package net.woniper.board.service;

import net.woniper.board.domain.KindBoard;

import java.util.List;

/**
 * Created by woniper on 2015. 10. 30..
 */
public interface KindBoardService {

    KindBoard find(Long kindBoardId);

    KindBoard find(String kindBoardName);

    List<KindBoard> find();

    void update(Long kindId, String kindBoardName);

    KindBoard save(String kindBoardName);
}
