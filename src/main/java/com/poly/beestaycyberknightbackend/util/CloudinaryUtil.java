package com.poly.beestaycyberknightbackend.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryUtil {
    Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) {
        try {
            return cloudinary
                    .uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap("folder", "beestay"));
        } catch (IOException e) {
            throw new RuntimeException("Upload Failed", e);
        }
    }
}
