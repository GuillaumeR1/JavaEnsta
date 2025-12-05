package com.ensta.myfilmlist.dao;

import java.util.List;
import com.ensta.myfilmlist.model.Film;

public interface FilmDAO {
    
    List<Film> findAll();
    Film findById(long id);
    Film create(Film film);
    void delete(long id);

}