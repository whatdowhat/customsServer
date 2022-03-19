package com.keepgo.whatdo.entity;

import java.util.List;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVO {

	List<?> data;
	List<Inbound> inboundData;
	List<User> userData;
	List<Common> commonData;
	int startIndex;
	int endIndex;
    int totalSize;
    int pagingSize;
    int pageBy;
    int currentPage;
    
    Member member;
    Inbound inbound;
    User user;
    Common common;
}
