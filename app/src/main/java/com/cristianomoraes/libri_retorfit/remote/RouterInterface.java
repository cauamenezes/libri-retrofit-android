package com.cristianomoraes.libri_retorfit.remote;

import com.cristianomoraes.libri_retorfit.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RouterInterface {

    /** ROTA DE INSERÇÃO DE USUÁRIO **/
    @POST("/usuario/inserirUsuario")
    Call<Usuario> addUsuario(@Body Usuario usuario);
}
