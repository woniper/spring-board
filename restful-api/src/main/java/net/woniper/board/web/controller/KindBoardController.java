package net.woniper.board.web.controller;

import net.woniper.board.service.KindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by woniper on 2015. 11. 1..
 */
@RestController
@RequestMapping("/kindBoards")
public class KindBoardController {

    @Autowired private KindBoardService kindBoardService;

    @RequestMapping(value = "/{kindId}", method = RequestMethod.GET)
    public ResponseEntity<?> getKindBoard(@PathVariable("kindId") Long kindId) {
        return ResponseEntity.ok(kindBoardService.getKindBoard(kindId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getKindBoards() {
        return ResponseEntity.ok(kindBoardService.getKindBoard());
    }

    @RequestMapping(value = "/{kindId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateKindBoard(
                    @PathVariable("kindId") Long kindId,
                    @RequestParam(value = "kindBoardName") String kindBoardName) {
        kindBoardService.updateKindBoard(kindId, kindBoardName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createKindBoard(@RequestParam(value = "kindBoardName") String kindBoardName) {
        return ResponseEntity.ok(kindBoardService.createKindBoard(kindBoardName));
    }
}
