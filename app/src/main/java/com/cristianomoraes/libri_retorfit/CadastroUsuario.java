package com.cristianomoraes.libri_retorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.cristianomoraes.libri_retorfit.model.Usuario;
import com.cristianomoraes.libri_retorfit.remote.RouterInterface;

public class CadastroUsuario extends AppCompatActivity {

    /** ATRIBUTOS DE INTERFACE GRÁFICA **/
    EditText txtNome;
    EditText txtSobrenome;
    EditText txtEmail;
    EditText txtLogin;
    EditText txtSenha;
    Button btnInserir;

    /** ATRIBUTO DE REPRESENTAÇÃO DAS ROTAS **/
    RouterInterface routerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        /** RECEBENDO OS OBJETOS DE INTERFACE **/
        txtNome = findViewById(R.id.txtNome);
        txtSobrenome = findViewById(R.id.txtSobrenome);
        txtEmail = findViewById(R.id.txtEmail);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        btnInserir = findViewById(R.id.btnCadastrarUsuario);

        /** TRATAMENTO DO EVENTO DE CLIQUE NO BOTÃO INSERIR **/
        btnInserir.setOnClickListener(view -> {

            /** CRIA UM OJETO DA MODEL DE USUÁRIO **/
            Usuario usuario = new Usuario();

            /** CARREGA OS DADOS DO FORMULÁRIO NO OBJETO DE MODEL USUÁRIO **/
            usuario.setNome(txtNome.getText().toString());
            usuario.setSobrenome(txtSobrenome.getText().toString());
            usuario.setEmail(txtEmail.getText().toString());
            usuario.setLogin(txtLogin.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());

            /** PASSAR OS DADOS PARA A APIREST **/
            
        });
    }
}