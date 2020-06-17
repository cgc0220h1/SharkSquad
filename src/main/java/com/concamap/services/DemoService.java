package com.concamap.services;

import com.concamap.model.DemoEntity;
import com.concamap.repositories.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DemoService {
    private final DemoRepository demoRepository;

    @Autowired
    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public List<DemoEntity> findAll() {
        List<DemoEntity> demoEntities = new LinkedList<>();
        for (DemoEntity demoEntity : demoRepository.findAll()) {
            demoEntities.add(demoEntity);
        }
        return demoEntities;
    }
}
