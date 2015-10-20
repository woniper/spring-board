package net.woniper.board.utils;

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
}
