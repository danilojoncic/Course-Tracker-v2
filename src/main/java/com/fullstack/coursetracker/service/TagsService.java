package com.fullstack.coursetracker.service;
import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.Lesson;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.repository.LessonRepository;
import com.fullstack.coursetracker.repository.TagRepository;
import com.fullstack.coursetracker.service.abstraction.TagAbs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagsService implements TagAbs {
    private final TagRepository tagRepository;
    private final LessonRepository lessonRepository;

    public TagsService(TagRepository tagRepository, LessonRepository lessonRepository) {
        this.tagRepository = tagRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    @Transactional
    public ServiceResponse createTag(CreateTag dto) {
        return tagRepository.findByTitle(dto.title())
                .map(tag -> new ServiceResponse(401,
                        new Message("Tag already exists!")))
                .orElseGet(() -> {
                    Tag tag1 = new Tag(dto.title());
                    tagRepository.save(tag1);
                    return new ServiceResponse(200,
                            new Message("Tag has been successfully created!"));
                });
    }

    @Override
    @Transactional
    public ServiceResponse deleteTag(Long id) {
        return tagRepository.findById(id)
                .map(tag -> {
                    List<Lesson> lessons = lessonRepository.findAll();
                    lessons.forEach(lesson -> {
                        lesson.getTags().remove(tag);
                    });
                    lessonRepository.saveAll(lessons);
                    tagRepository.deleteById(id);
                    return new ServiceResponse(201,
                            new Message("Tag with id: " + id + " has been deleted!"));
                })
                .orElseGet(() -> new ServiceResponse(401,
                        new Message("Tag with id: " + id + " does not exist!")));
    }

    @Override
    @Transactional
    public ServiceResponse editTag(Long id, CreateTag dto) {
        return tagRepository.findById(id)
                .map(tag -> {
                    tag.setTitle(dto.title());
                    tagRepository.save(tag);
                    return new ServiceResponse(201,
                            new Message("Tag with id: " + id + " has been edited!"));
                })
                .orElseGet(() -> new ServiceResponse(401,
                        new Message("Tag with id: " + id + " does not exist!")));
    }

    @Override
    public ServiceResponse getAllTags() {
        return new ServiceResponse(200,tagRepository.findAll());
    }

    @Override
    public ServiceResponse getAllTagsPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page,size);
        return new ServiceResponse(200,tagRepository.findAll(pg));
    }

    @Override
    public ServiceResponse getOneTag(Long id) {
        return tagRepository.findById(id)
                .map(tag -> new ServiceResponse(200,tag))
                .orElseGet(() -> new ServiceResponse(401,
                        new Message("Tag with id: " + id + " does not exist!")));
    }
}
