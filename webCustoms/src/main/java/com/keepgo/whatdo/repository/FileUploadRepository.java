package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.FileUpload;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Integer>{


}
