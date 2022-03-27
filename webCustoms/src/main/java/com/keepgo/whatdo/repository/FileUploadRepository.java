package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.FileUploadOld;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadOld, Integer>{


}
