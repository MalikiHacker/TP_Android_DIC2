package sn.maliki.crudrestapiapp.Controllers.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import sn.maliki.crudrestapiapp.Model.Eleve;
import sn.maliki.crudrestapiapp.R;

public class EleveAdapter extends RecyclerView.Adapter<EleveAdapter.ViewHolder> {

    Context context;
    List<Eleve> eleves;
    private OnItemClickListener listener;

    public EleveAdapter(Context context, List<Eleve> eleves) {
        this.context = context;
        this.eleves = eleves;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflater
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eleve_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Eleve e = getEleve(position);
        holder.username.setText(MessageFormat.format("{0} {1}", e.getPrenom(),
                e.getNom().toUpperCase()));
        holder.tvEmail.setText(e.getEmail());


    }

    @Override
    public int getItemCount() {
        return this.eleves.size();
    }

    public Eleve getEleve(int position) {
        return eleves.get(position);
    }

    public List<Eleve> getEleves() {
        return eleves;
    }

    public void setEleves(List<Eleve> eleves) {
        this.eleves = eleves;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Eleve eleve);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // textviews
        TextView username, tvEmail;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // matching
            username = itemView.findViewById(R.id.display_username);
            tvEmail = itemView.findViewById(R.id.display_email);

            // listener
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(eleves.get(position));
                }
            });

        }
    }
}
