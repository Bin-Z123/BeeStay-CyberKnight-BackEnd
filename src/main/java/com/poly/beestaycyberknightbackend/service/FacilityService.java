package com.poly.beestaycyberknightbackend.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.poly.beestaycyberknightbackend.domain.Facility;
import com.poly.beestaycyberknightbackend.dto.request.FacilityRequest;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.FacilityMapper;
import com.poly.beestaycyberknightbackend.repository.FacilityRepository;
import com.poly.beestaycyberknightbackend.util.CloudinaryUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacilityService {
    FacilityRepository facilityRepository;
    FacilityMapper mapper;
    CloudinaryUtil cloudinaryUtil;

    @Transactional
    public Facility createFacility(FacilityRequest request, MultipartFile file) {
        if (facilityRepository.existsByFacilityName(request.getFacilityName())) {
            throw new AppException(ErrorCode.FACILITIES_EXISTED);
        }
        Facility facility = mapper.toFacility(request);
        String publicId = null;
        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinaryUtil.uploadFile(file);
                publicId = (String) uploadResult.get("public_id");
                facility.setPublicId(publicId);
            } catch (Exception e) {
                throw new AppException(ErrorCode.UPLOAD_FAILED);
            }
        }
        System.out.println("facility: " + facility);
        return facilityRepository.save(facility);
    }

    public List<Facility> getFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
    }

    @Transactional
    public Facility updateFacility(Long id, FacilityRequest request, MultipartFile file) {

        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
        mapper.updateFacility(facility, request);
        String oldPublicId = facility.getPublicId();
        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinaryUtil.uploadFile(file);
                String publicId = (String) uploadResult.get("public_id");
                facility.setPublicId(publicId);
            } catch (Exception e) {
                throw new AppException(ErrorCode.UPLOAD_FAILED);
            }
        }
        Facility savedFacility = facilityRepository.save(facility);
        if (file != null && !file.isEmpty() && oldPublicId != null && !oldPublicId.isEmpty()) {
            try {
                cloudinaryUtil.deleteFile(oldPublicId);
            } catch (Exception e) {
                // Nếu xóa file cũ thất bại, ta có thể ghi log chứ không nên ném exception
                // vì dữ liệu chính trong DB đã được cập nhật đúng.
                // logger.error("Không thể xóa file cũ trên Cloudinary: " + oldPublicId, e);
            }
        }

        return savedFacility;
    }

    @Transactional
    public Facility deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED));
        if (facility.getPublicId() != null && !facility.getPublicId().isEmpty()) {
            cloudinaryUtil.deleteFile(facility.getPublicId());
        }
        facilityRepository.delete(facility);
        return facility;
    }

}
