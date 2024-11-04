package com.example.urlshortener.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.urlshortener.app.dto.UrlDTO;
import com.example.urlshortener.app.entities.Url;
import com.example.urlshortener.app.repository.UrlRepository;
import com.example.urlshortener.app.services.exceptions.ResourceNotFoundException;
import com.example.urlshortener.app.utils.UrlShortenerUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlService {
    
    @Autowired
    private UrlRepository repository;

    public int getStatus(String shortUrl){
        try{
            Optional<Url> url = repository.findClickCountByShortUrl(shortUrl);
            Url entity = url.orElseThrow(() -> new ResourceNotFoundException("ClickCount doesnt exist"));
        
            return entity.getClickCount();           
        
        } catch (Exception e ){
            throw e;
        }
    }

    public String getLongUrlAndIncreaseClick(String shortUrl) {
        try{
            Optional<Url> url = repository.findByShortUrl(shortUrl);
            repository.incrementClickCount(shortUrl);
            Url longUrl = url.orElseThrow( () -> new ResourceNotFoundException("Long Url doesn't exist"));
            
            return longUrl.getLongUrl();

        } catch (Exception e) {
            log.error("erro ao buscar url");
            throw e;
        }
    }

    public String createShortenerUrl(UrlDTO entityDTO){
        Url url = new Url(entityDTO);
        UrlShortenerUtil util = new UrlShortenerUtil();
        String shortUrl = util.RandomStringFactory(10);
        url.setShortUrl(shortUrl);
        
        url = repository.save(url);
        
        return url.getShortUrl();    
    }

}