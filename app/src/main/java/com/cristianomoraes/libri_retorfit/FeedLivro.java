package com.cristianomoraes.libri_retorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cristianomoraes.libri_retorfit.model.Item;
import com.cristianomoraes.libri_retorfit.model.Livro;
import com.cristianomoraes.libri_retorfit.remote.APIUtil;
import com.cristianomoraes.libri_retorfit.remote.RouterInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedLivro extends AppCompatActivity {

    /** DECLARAÇÃO DOS ATRIBUTOS **/
    RouterInterface routerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_livro);

        /** CONECTA O APLICATIVO COM A API **/
        routerInterface = APIUtil.getUsuarioInterface();

        /** EXECUTA A CHAMADA  PARA A ROTA DE LISTAGEM DE LIVROS **/
        Call<List<Livro>> call = routerInterface.getLivros();

        call.enqueue(new Callback<List<Livro>>() {
            @Override
            public void onResponse(Call<List<Livro>> call, Response<List<Livro>> response) {

                if(response.isSuccessful()){
                    List<Item> itens = new ArrayList<>();

                    /** RECEBE OS DADOS DA API **/
                    List<Livro> list = new ArrayList<Livro>();
                    list = response.body();

                    for(int i = 0; i < list.size(); i++){
                        itens.add(new Item(0, list.get(i)));
                    }

                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setAdapter(new LivroAdapter(itens));

                }

            }

            @Override
            public void onFailure(Call<List<Livro>> call, Throwable t) {

            }
        });

    }//FIM DO MÉTODO ONCREATE

    /** CLASSE DE ADAPTER DA RECYCLERVIEW **/
    private class LivroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Item> itens;

        public LivroAdapter(List<Item> itens){
            this.itens = itens;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LivroAdapter.LivroViewHolder(
                    LayoutInflater.from(
                                        parent.getContext()).inflate(
                                                                        R.layout.item_container_livro,
                                                                        parent,
                                                                        false
                                        ));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            /** DADOS DE LIVRO **/
            if(getItemViewType(position) == 0){

                Livro livro = (Livro) itens.get(position).getObject();
                ((LivroAdapter.LivroViewHolder) holder).setLivroData(livro);

            }

        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        public int getItemViewType(int position){
            return itens.get(position).getType();
        }

        /** CLASSE DE VIEWHOLDER DA RECYCLERVIEW **/
        class LivroViewHolder extends RecyclerView.ViewHolder{

            /** ATRIBUTOS DA CLASS LIVROVIEWHOLDER **/
            private TextView txtTitulo, txtDescricao;
            private int cod_livro;

            public LivroViewHolder(@NonNull View itemView) {
                super(itemView);

                txtTitulo = itemView.findViewById(R.id.txtTitulo);
                txtDescricao = itemView.findViewById(R.id.txtDescricao);

                /** AÇÃO DE CLIQUE PARA EDITAR LIVRO E EXCLUIR LIVRO **/
                itemView.setOnClickListener(view->{
                    /**
                     setMessage -> Título da caixa de alerta
                        Parametros:
                                    1 - Título
                     setPositiveButton -> Define uma opção de ação
                        Parametros:
                                    1 - Título
                                    2 - Ação a ser executada
                     setNegativeButton -> Define uma opção de ação
                        Parametros:
                                    1 - Título
                                    2 - Ação a ser executada
                     *  **/
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedLivro.this)
                            .setMessage("ESCOLHA A AÇAO QUE DESEJA EXECUTAR")
                            .setPositiveButton("ALTERAR", (dialog1, witch)->{

                                Intent intent = new Intent(FeedLivro.this, AlterarLivro.class);
                                intent.putExtra("cod_livro", cod_livro);
                                startActivity(intent);

                            })
                            .setNegativeButton("EXCLUIR", (dialog1, witch)->{

                                routerInterface = APIUtil.getUsuarioInterface();

                                Call<Livro> call = routerInterface.deleteLivro(cod_livro);

                                call.enqueue(new Callback<Livro>() {
                                    @Override
                                    public void onResponse(Call<Livro> call, Response<Livro> response) {

                                        Toast.makeText(FeedLivro.this,
                                                        "LIVRO EXCLUÍDO COM SUCESSO!",
                                                        Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(FeedLivro.this, FeedLivro.class));

                                    }

                                    @Override
                                    public void onFailure(Call<Livro> call, Throwable t) {

                                    }
                                });

                            });

                    alertDialog.show();

                });

            }//FIM DO CONSTRUTOR DA CLASSE LIVROVIEWHOLDER

            /** MÉTODO QUE CARREGA OS VALORES NOS ELMENTOS DE TEXTVIEW
                - txtTitulo
                - txtDescricao
             * **/
            public void setLivroData(Livro livro){

                txtTitulo.setText(livro.getTitulo());
                txtDescricao.setText(livro.getDescricao());
                cod_livro = livro.getCod_livro();

            }

        }//FIM DA CLASSE LIVROVIEWHOLDER


    }

}//FIM DA CLASSE FEEDLIVRO















