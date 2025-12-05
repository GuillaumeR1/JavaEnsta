package com.ensta.myfilmlist.service.impl;

import com.ensta.myfilmlist.dao.FilmDAO;
import com.ensta.myfilmlist.dao.RealisateurDAO;
import com.ensta.myfilmlist.dao.impl.JdbcFilmDAO;
import com.ensta.myfilmlist.dao.impl.JdbcRealisateurDAO;
import com.ensta.myfilmlist.exception.ServiceException;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;
import com.ensta.myfilmlist.service.MyFilmsService;
import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.mapper.FilmDTOMapper;
import com.ensta.myfilmlist.mapper.FilmMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MyFilmsServiceImpl implements MyFilmsService {

    public static final int NB_FILMS_MIN_REALISATEUR_CELEBRE = 3;
    

    @Override
    public Realisateur updateRealisateurCelebre(Realisateur realisateur) throws ServiceException {
        try {

            if (realisateur.getFilmsRealises() == null) {
                throw new ServiceException("La liste des films réalisés ne doit pas être nulle.");
            }

            int nbFilms = realisateur.getFilmsRealises().size();
            boolean estCelebre = nbFilms >= NB_FILMS_MIN_REALISATEUR_CELEBRE;
            realisateur.setCelebre(estCelebre);

            return realisateur;

        } 
        catch (Exception e) {
            throw new ServiceException("Erreur lors de la mise à jour du statut célèbre du réalisateur.", e);
        }
    }

    @Override
    public long calculerDureeTotale(List<Film> films) {
        /*long total = 0;

        for (Film film : films) {
            total += film.getDuree();
        }

        return total;*/
        return films.stream()
            .mapToInt(Film::getDuree)
            .sum();
    }

    @Override
    public double calculerNoteMoyenne(double[] notes) {
        /*if (notes == null || notes.length == 0) {
            return 0;
        }

        double somme = 0;
        for (double note : notes) {
            somme += note;
        }

        double moyenne = somme / notes.length;*/
        double moyenne = Arrays.stream(notes)
            .average()
            .orElse(0.0);

        double arrondi = Math.pow(10, 2);

        return Math.round(moyenne * arrondi) / arrondi;
    }

    @Override
    public List<Realisateur> updateRealisateurCelebres(List<Realisateur> realisateurs) throws ServiceException {
        try {
            return realisateurs.stream()
                    .peek(r -> r.setCelebre(r.getFilmsRealises().size() >= NB_FILMS_MIN_REALISATEUR_CELEBRE))
                    .filter(Realisateur::isCelebre)
                    .toList();

        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la mise à jour des réalisateurs célèbres.", e);
        }
    }

    private FilmDAO filmDAO = new JdbcFilmDAO();

    @Override
    public List<FilmDTO> findAllFilms() throws ServiceException {
        try {
            List<Film> films = filmDAO.findAll();
            return films.stream()
                    .map(FilmDTOMapper::convertFilmToFilmDTO)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la liste des films.", e);
        }   
    }

    @Override
    public FilmDTO findFilmById(long id) throws ServiceException {
        try {
            Film film = filmDAO.findById(id);

            if (film == null) {
                return null;
            }
            return FilmDTOMapper.convertFilmToFilmDTO(film);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de findFilmById(" + id + ")", e);
        }
    }

    private RealisateurDAO realisateurDAO = new JdbcRealisateurDAO();

    @Override
    public List<Realisateur> findAllRealisateurs() throws ServiceException {
        try {
            return realisateurDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur findAllRealisateurs.", e);
        }
    }

    @Override
    public Optional<Realisateur> findRealisateurById(long id) throws ServiceException {
        try {
            return realisateurDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de findRealisateurById.", e);
        }
    }

    @Override
    public Realisateur findRealisateurByNomAndPrenom(String nom, String prenom) throws ServiceException {
        try {
            return realisateurDAO.findByNomAndPrenom(nom, prenom);
        } catch (Exception e) {
            throw new ServiceException("Erreur findRealisateurByNomAndPrenom.", e);
        }
    }

    @Override
    public FilmDTO createFilm(FilmForm form) throws ServiceException {
        try {
            Film film = FilmMapper.convertFilmFormToFilm(form);
            Optional<Realisateur> realisateurOpt = realisateurDAO.findById(form.getRealisateurId());
            if (realisateurOpt.isEmpty()) {
                throw new ServiceException("Aucun réalisateur trouvé avec id=" + form.getRealisateurId());
            }
            film.setRealisateurId(form.getRealisateurId());
            film.setRealisateur(realisateurOpt.get());
            film = filmDAO.create(film);
            return FilmDTOMapper.convertFilmToFilmDTO(film);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création du film.", e);
        }
    }

    @Override
    public void deleteFilm(long id) throws ServiceException {
        try {
            filmDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression du film avec id=" + id, e);
        }
    }
}
