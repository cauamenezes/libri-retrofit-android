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

        /** EXECUTA A CHAMADA PARA A ROTA DE LISTAGEM DE LIVROS **/
        Call<List<Livro>> call = routerInterface.getLivros();

        call.enqueue(new Callback<List<Livro>>() {

            @Override
            public void onResponse(Call<List<Livro>> call, Response<List<Livro>> response){

                //Método isSuccessful() é da classe Response representada pela variável response
                if(response.isSuccessful()) {
                    List<Item> itens = new ArrayList<>();

                    /** RECEBE OS DADOS DA API **/
                    List<Livro> list = new ArrayList<Livro>();
                    list = response.body();

                    for (int i = 0; i < list.size(); i++) {
                        itens.add(new Item(0, list.get(i)));
                    }

                    //Chama a activity da RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);

                    //Pega os dados e monta eles na RecyclerView
                    recyclerView.setAdapter(new LivroAdapter(itens));           
                }

            }

            //Só entra aqui se houver um erro maior, como: rota não criada, API não respondeu, e coisas assim.
            @Override
            public void onFailure(Call<List<Livro>> call, Throwable t) {

            }
        });

    }//FIM DO MÉTODO onCreate

    /** CLASSE DE ADAPTER DA RECYCLERVIEW **/
    private class LivroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

       List<Item> itens;

       public LivroAdapter(List<Item> itens) {
           this.itens = itens;
       }

       //Cria os elementos necessários, pega os dados passados pelo onBindViewHolder
       //e os injeta nos elementos.
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LivroAdapter.LivroViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_container_livro, parent, false)
            );                                                                      
        }

        //Pega os dados
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

           /** DADOS DE LIVRO **/
           if(getIntViewType(position) == 0) {

               Livro livro = (Livro) itens.get(position).getObject();
               ((LivroAdapter.LivroViewHolder) holder).setLivroData(livro);
           }

        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        public int getIntViewType(int position) {

           return itens.get(position).getType();
        }

        /** CLASSE DE VIEWHOLDER DA RECYCLERVIEW **/
       class LivroViewHolder extends RecyclerView.ViewHolder {

           /** ATRIBUTOS DA CLASSE LIVROVIEWHOLDER **/
           private TextView txtTitulo, txtDescricao;
           private int cod_livro;

           public LivroViewHolder(@NonNull View itemView) {
               super(itemView);

               txtTitulo = itemView.findViewById(R.id.txtTitulo);
               txtDescricao = itemView.findViewById(R.id.txtLivroDescricao);

               /** AÇÃO DE CLICK PARA EDITAR LIVRO E EXCLUIR LIVRO **/
               itemView.setOnClickListener(view -> {

                   /**
                    * setMessage -> Título da caixa de alerta
                        Parâmetros:
                                    1 - Título

                    * setPositiveButton -> Define uma opção de ação
                        Parâmetros:
                                    1 - Título
                                    2 - Ação a ser executada

                    * setNegativeButton -> Define uma opção de ação
                        Parâmetros:
                                    1 - Título
                                    2 - Ação a ser executada
                    **/

                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedLivro.this)
                           .setMessage("Escolha a ação que deseja executar")
                           .setPositiveButton("Alterar",(dialog1, witch)->{})
                           .setNegativeButton("Excluir", (dialog1, witch)->{

                               routerInterface = APIUtil.getUsuarioInterface();

                               //Executa a requisição
                               Call<Livro> call = routerInterface.deleteLivro(cod_livro);

                               //Executa a response
                               call.enqueue(new Callback<Livro>() {
                                   @Override
                                   public void onResponse(Call<Livro> call, Response<Livro> response) {

                                       Toast.makeText(FeedLivro.this,
                                               "Livro excluído com sucesso!",
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

           /** MÉTODO QUE CARREGA OS VALORES NOS ELEMENTOS DE TEXTVIEW
                - txtTitulo
                - txtDescricao
            * **/
           public void setLivroData(Livro livro) {

               txtTitulo.setText(livro.getTitulo());
               txtDescricao.setText(livro.getDescricao());
               cod_livro = livro.getCod_livro();
           }

       }//FIM DA CLASSE LIVROVIEWHOLDER

    }

}//FIM DA CLASSE FeedLivro