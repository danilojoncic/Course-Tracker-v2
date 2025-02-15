package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.dto.ServiceResponse;

public interface TagAbs {
    ServiceResponse createTag(CreateTag dto);
    ServiceResponse deleteTag(Long id);
    ServiceResponse editTag(Long id, CreateTag dto);
    ServiceResponse getAllTags();
    ServiceResponse getAllTagsPaginated(Integer page, Integer size);
    ServiceResponse getOneTag(Long id);
}
