package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.InboundMaster;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long>{


//	List<FileUpload> findByInboundMasterAndUploadType(InboundMaster inboundMaster, String uploadType);
//	List<FileUpload> findByInboundMasterAndFileuploadType(InboundMaster inboundMaster, Common fileuploadType);
	List<FileUpload> findByInboundMasterAndFileType(InboundMaster inboundMaster, int fileType);
	List<FileUpload> findByInboundMasterAndFileTypeAndPath3(InboundMaster inboundMaster, int fileType,String path3);
	                              
//	List<FileUpload> findCondition(Specification<FileUpload> condition, InboundMaster inboundMaster);
	
}
