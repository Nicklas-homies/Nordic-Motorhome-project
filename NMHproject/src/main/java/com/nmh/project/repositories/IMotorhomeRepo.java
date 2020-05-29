package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;

import java.util.List;

public interface IMotorhomeRepo {

    public boolean create(Motorhome motorhome);

    public Motorhome read(int id);

    public List<Motorhome> readAll();

    public boolean update(Motorhome motorhome);

    public boolean delete(int id);

}
