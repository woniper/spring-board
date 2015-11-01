package net.woniper.board.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.errors.support.KindBoardNotFoundException;
import net.woniper.board.repository.KindBoardRepository;
import net.woniper.board.service.KindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by woniper on 2015. 10. 30..
 */
@Service
@Transactional
@Slf4j
public class KindBoardServiceImpl implements KindBoardService {

    @Autowired private KindBoardRepository kindBoardRepository;

    @Override
    public KindBoard getKindBoard(Long kindBoardId) {
        KindBoard kindBoard = kindBoardRepository.findOne(kindBoardId);
        if(kindBoard == null)
            throw new KindBoardNotFoundException(String.valueOf(kindBoardId));

        return kindBoard;
    }

    @Override
    public KindBoard getKindBoard(String kindBoardName) {
        KindBoard kindBoard = kindBoardRepository.findByKindBoardName(kindBoardName);
        if(kindBoard == null)
            throw new KindBoardNotFoundException(kindBoardName);

        return kindBoard;
    }

    @Override
    public List<KindBoard> getKindBoard() {
        return kindBoardRepository.findAll();
    }

    @Override
    public void updateKindBoard(Long kindId, String kindBoardName) {
        KindBoard kindBoard = getKindBoard(kindId);
        kindBoard.setKindBoardName(kindBoardName);
    }
}
