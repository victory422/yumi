package com.yumikorea.common.mvc.controller;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class BasicController {

    public static void setPage(Page<?> list, Model model ) {
        Long totalBoardCnt = list.getTotalElements();
        int nowPage = list.getPageable().getPageNumber() + 1;	// 현재 페이지
        int totalPage = list.getTotalPages();
        int startPage = Math.max( nowPage - 2, 1 );
        int endPage = Math.min( nowPage + 2, totalPage );
        boolean hasPrivious = list.hasPrevious();
        boolean hasNext = list.hasNext();

        model.addAttribute( "totalBoardCnt", totalBoardCnt );
        model.addAttribute( "nowPage", nowPage );
        model.addAttribute( "startPage", startPage );
        model.addAttribute( "endPage", endPage );
        model.addAttribute( "totalPage", totalPage );
        model.addAttribute( "hasPrivious", hasPrivious );
        model.addAttribute( "hasNext", hasNext );
    }
  
}
