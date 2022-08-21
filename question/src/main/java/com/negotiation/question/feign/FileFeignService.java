package com.negotiation.question.feign;

import com.negotiation.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FeignClient("file-service")
public interface FileFeignService {

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R upload(@RequestPart MultipartFile file) throws IOException;

    @GetMapping("/file/download/{fileUID}")
    R download(@PathVariable String fileUID, HttpServletResponse response) throws IOException;
}
