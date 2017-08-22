package com.omt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.omt.domain.Watchlist;
import com.omt.service.WatchlistService;

@RestController
@RequestMapping("/watchlists")
public class WatchlistController {

    WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Watchlist> findAll() {
        return watchlistService.findAll();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Watchlist findOne(@PathVariable("id") Long id) {
        return watchlistService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Watchlist save(@RequestBody Watchlist watchlist) throws Exception{
        String username = watchlist.getWatchlistUser();
        Long id = watchlist.getVideo().getId();
        if(checkForDuplicate(username, id) != null) throw new Exception("You already have this in your watchlist");
        return watchlistService.save(watchlist);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Watchlist update(@RequestBody Watchlist watchlist) {
        return watchlistService.save(watchlist);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        watchlistService.delete(id);
    }

    @RequestMapping(path = "get-user-watchlist/{username}")
    public List<Watchlist> findByUsername(@PathVariable("username") String username) {
        return watchlistService.findByUsername(username);
    }

    public Watchlist checkForDuplicate(String username, Long id){
        return watchlistService.checkForDuplicate(username, id);
    }
}
