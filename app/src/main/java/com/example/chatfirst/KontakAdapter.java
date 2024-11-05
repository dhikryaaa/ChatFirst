package com.example.chatfirst;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KontakAdapter extends ArrayAdapter<Kontak> {
    private List<Kontak> kontakListFull;       // Full list of contacts
    private List<Kontak> kontakListFiltered;    // Filtered list of contacts
    private int selectedPosition = -1;          // Position of the selected contact

    public KontakAdapter(Context context, List<Kontak> contacts) {
        super(context, 0, contacts);
        this.kontakListFull = new ArrayList<>(contacts);
        this.kontakListFiltered = new ArrayList<>(contacts);
    }

    @Override
    public int getCount() {
        return kontakListFiltered.size();
    }

    @Override
    public Kontak getItem(int position) {
        return kontakListFiltered.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Kontak dtkontak = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_detail, parent, false);
        }

        TextView tNama = convertView.findViewById(R.id.tNama);
        TextView tnoHp = convertView.findViewById(R.id.tnoHp);

        if (dtkontak != null) {
            tNama.setText(dtkontak.getNama());
            tnoHp.setText(dtkontak.getNoHp());
        }

        // Highlight the selected item
        if (position == selectedPosition) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.selected_item_color));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void addItem(Kontak newKontak) {
        kontakListFull.add(newKontak);
        kontakListFiltered.add(newKontak);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < kontakListFiltered.size()) {
            Kontak deletedItem = kontakListFiltered.get(position);
            kontakListFiltered.remove(position);        // Remove from filtered list
            kontakListFull.remove(deletedItem);        // Remove from full list
            notifyDataSetChanged();

            // Reset the selected position if the deleted item was selected
            if (position == selectedPosition) {
                selectedPosition = -1;                 // Clear selection if the selected item is deleted
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Kontak> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(kontakListFull); // No filter applied
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Kontak kontak : kontakListFull) {
                        if (kontak.getNama().toLowerCase().contains(filterPattern) ||
                                kontak.getNoHp().contains(filterPattern)) {
                            filteredList.add(kontak);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                kontakListFiltered.clear();
                if (results.values != null) {
                    kontakListFiltered.addAll((List<Kontak>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}
