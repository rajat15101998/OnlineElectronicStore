package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    //U = Entity Type
    //V = DTO Type
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> DTOType, ModelMapper mapper) {
        //get list of entities from Page Object
        List<U> allEntities = page.getContent();

        //convert Entities List to DTO List
        List<V> allDtos = allEntities.stream().map(entity -> mapper.map(entity, DTOType)).collect(Collectors.toList());

        //create an Object of PageableResponse
        PageableResponse<V> pageableResponse = new PageableResponse<>();

        //set all the required fields in object of PageableResponse
        pageableResponse.setContent(allDtos);
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setTotalElements(page.getTotalElements());
        pageableResponse.setLastPage(page.isLast());

        return pageableResponse;
    }
}
