package com.cristianomoraes.libri_retorfit.model;

public class Item {

    /*
    0 - livro
    1 - HQ
    2 - manga
    * */
    private int type;

    /*RECEBE OBJETOS DE LIVRO, HQ, MANGA OU QUALQUER OBJETO
    * DE QUALQUER OUTRA CLASSE DE TIPO QUE VENHA A EXISTIR*/
    private Object object;

    public Item() {
    }

    public Item(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
