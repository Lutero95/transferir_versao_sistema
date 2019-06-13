package br.gov.al.maceio.sishosp.questionario.model;

import java.io.Serializable;

public class Pestalozzi implements Serializable {

    private Integer id;

    // HERDADOS


    public Pestalozzi() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
