package org.taskstodo.service.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;


@Transactional
abstract class AbstractService{
    
    @Autowired 
    private Mapper dozerBeanMapper;
    
    public AbstractService(){}

     public <TO,E> TO mapEntity2TO(Class<TO> clazz, E e){
        return dozerBeanMapper.map(e, clazz);
    }
    
    public <E,TO> E mapTO2Entity(Class<E> clazz, TO to){
        return dozerBeanMapper.map(to, clazz);
    }

    public Mapper getDozerBeanMapper() {
        return dozerBeanMapper;
    }

    public void setDozerBeanMapper(Mapper dozerBeanMapper) {
        this.dozerBeanMapper = dozerBeanMapper;
    }
    
    protected String getUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}