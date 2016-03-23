package br.projeto.agendatelefonica.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;
import br.projeto.agendatelefonica.model.Grupo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jonathan on 23/03/16.
 */
public class BaseAdapterLista extends BaseAdapter {

    private ArrayList lista = new ArrayList();
    private LayoutInflater inflater;
    private Context context;
    private String flag;

    public BaseAdapterLista(Context context, ArrayList lista, String flag){
        this.lista = lista;
        this.context = context;
        this.flag = flag;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MinhaView minhaView;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_contatos, parent, false);
            minhaView = new MinhaView(convertView);
            convertView.setTag(minhaView);
        } else {
            minhaView = (MinhaView) convertView.getTag();
        }

        if(flag.equals("contato")) {

            Contato contatoAtual = (Contato) getItem(position);
            minhaView.tvTitle.setText(contatoAtual.getNome());
            minhaView.tvDesc.setText(contatoAtual.getTelefone());
            minhaView.ivIcon.setImageBitmap(Util.Base64ParaBitmap(contatoAtual.getImagem()));

        }else if(flag.equals("grupo")) {

            Grupo grupoAtual = (Grupo) getItem(position);
            minhaView.tvTitle.setText(grupoAtual.getNome());
            minhaView.tvDesc.setText("");
            minhaView.ivIcon.setImageBitmap(Util.Base64ParaBitmap(grupoAtual.getImagem()));
        }

        return convertView;
    }

    public class MinhaView{
        TextView tvTitle, tvDesc;
        CircleImageView ivIcon;

        public MinhaView(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvTitle);
            tvDesc = (TextView) item.findViewById(R.id.tvDesc);
            ivIcon = (CircleImageView) item.findViewById(R.id.ivIcon);
        }
    }
}
