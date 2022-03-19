package com.keepgo.whatdo.controller;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.ApplicationConfig;
import com.keepgo.whatdo.entity.FileUpload;
import com.keepgo.whatdo.repository.FileUploadRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 컨트롤러에서 설정
public class FileUploadController {
	
	private final ApplicationConfig _applicationConfig;
	
	@Autowired
	private FileUploadRepository _fileUploadRepo;
	
    @PostMapping("/uploadFile/image/temps")
    @ResponseBody
    public List<FileUpload> uploadImageToTemps(@RequestParam("uploadFile") List<MultipartFile> uploadfiles)
    {
        List<FileUpload> result = new ArrayList<>();
		String fileName = null;
		FileUpload fileUpload = null;
        
		 if (uploadfiles == null ) {
				fileUpload = FileUpload.builder()
				.fileSaveName(null)
		    	.uploadYn(false)
		    	.uploadUser(null)
		    	.fileOriginalName(fileName)
		    	.Regdt(LocalDateTime.now())
		    	.fileSize(null)
		    	.errorMessage("전달된 파일 없음.")
		    	.build();
				result.add(fileUpload);
	        }
		
        try {
            for(MultipartFile uploadfile : uploadfiles) {
            	
            	String splits[] = uploadfile.getOriginalFilename().split("\\.");
            	String uniq = UUID.randomUUID().toString();

            	
            	BufferedImage image = ImageIO.read(uploadfile.getInputStream());
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	ImageIO.write(image, "png", baos);
            	
//            	byte[] encodedImage = Base64.encodeBase64(baos.toByteArray());    //생성된 바이너리
            	byte[] encodedImage = baos.toByteArray();    //생성된 바이너리
            	byte[] blob = encodedImage;
//            	System.out.println(uploadfile.getName()+" / "+encodedImage);
            	baos.close();
            	System.out.println(blob.length);
            	
	 			fileName = uploadfile.getOriginalFilename();
	 			Path path = Paths.get(_applicationConfig.getUploadDir());
	 			Files.createDirectories(path);
	 			File f = new File(path.toAbsolutePath()+File.separator+uniq+"."+splits[1]);
//	   			File f = new File(path.toAbsolutePath()+File.separator+uploadfile.getOriginalFilename());
	 			
	     		boolean yn =  f.createNewFile();
	     		uploadfile.transferTo(f);
	     		
	 			fileUpload = FileUpload.builder().fileSaveName(uniq+"."+splits[1])
	 			.fileData(blob)
	 	    	.uploadYn(yn)
	 	    	.uploadUser(null)
	 	    	.fileOriginalName(fileName)
	 	    	.Regdt(LocalDateTime.now())
	 	    	.fileSize(uploadfile.getSize())
	 	    	.build();
	 			result.add(fileUpload);
            	
	 			_fileUploadRepo.save(fileUpload);
	 			
	 			List<FileUpload> l = _fileUploadRepo.findAll();
	 			
	 			return l;
            	
            	
//        		if(splits.length<2) {
//        			fileName = uploadfile.getOriginalFilename();
//        			fileUpload = FileUpload.builder().fileSaveName(uniq)
//        	    	.uploadYn(false)
//        	    	.uploadUser(null)
//        	    	.fileOriginalName(fileName)
//        	    	.Regdt(LocalDateTime.now())
//        	    	.fileSize(null)
//        	    	.errorMessage("확장명 없음.")
//        	    	.build();
//        	    	
//        			result.add(fileUpload);
//        			
//        		}else {
//        			
//        			fileName = uploadfile.getOriginalFilename();
//        			Path path = Paths.get(_applicationConfig.getUploadDir());
//        			Files.createDirectories(path);
//        			File f = new File(path.toAbsolutePath()+File.separator+uniq+"."+splits[1]);
////        			File f = new File(path.toAbsolutePath()+File.separator+uploadfile.getOriginalFilename());
//        			
//            		boolean yn =  f.createNewFile();
//            		uploadfile.transferTo(f);
//            		
//        			fileUpload = FileUpload.builder().fileSaveName(uniq+"."+splits[1])
//        	    	.uploadYn(yn)
//        	    	.uploadUser(null)
//        	    	.fileOriginalName(fileName)
//        	    	.Regdt(LocalDateTime.now())
//        	    	.fileSize(uploadfile.getSize())
//        	    	.build();
//        			result.add(fileUpload);
//        		}
            }

        }catch (Exception e) {
        	e.printStackTrace();
			fileUpload = FileUpload.builder().fileSaveName(null)
        	    	.uploadYn(false)
        	    	.uploadUser(null)
        	    	.fileOriginalName(fileName)
        	    	.Regdt(LocalDateTime.now())
        	    	.fileSize(null)
        	    	.errorMessage(e.getMessage())
        	    	.build();
			result.add(fileUpload);
		}
        return result;
    }

}
