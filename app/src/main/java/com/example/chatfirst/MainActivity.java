package com.example.chatfirst;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private KontakAdapter KAdapter;
    private Kontak selectedKontak;
    private ImageButton showButton;
    private ImageButton addKontak;
    private ImageButton deleteKontak;
    private ImageButton searchKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showButton = findViewById(R.id.showButton);
        addKontak = findViewById(R.id.addContactButton);
        deleteKontak = findViewById(R.id.deleteContactButton);
        searchKontak = findViewById(R.id.searchContactButton);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addKontak.getVisibility() == View.VISIBLE && deleteKontak.getVisibility() == View.VISIBLE && searchKontak.getVisibility() == View.VISIBLE) {
                    // Fade out
                    animateButtonVisibility(addKontak, false);
                    animateButtonVisibility(deleteKontak, false);
                    animateButtonVisibility(searchKontak, false);
                } else {
                    // Fade in
                    animateButtonVisibility(addKontak, true);
                    animateButtonVisibility(deleteKontak, true);
                    animateButtonVisibility(searchKontak, true);
                }
            }
        });

        list = findViewById(R.id.listView);
        addKontak.setOnClickListener(operasi);
        searchKontak.setOnClickListener(operasi);
        deleteKontak.setOnClickListener(operasi);

        ArrayList<Kontak> listKontak = new ArrayList<>();
        KAdapter = new KontakAdapter(this, listKontak);
        list.setAdapter(KAdapter);

        list.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedKontak = KAdapter.getItem(position);
            KAdapter.setSelectedPosition(position);
            return true;
        });
    }

    private void animateButtonVisibility(View button, boolean show) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "alpha", show ? 0f : 1f, show ? 1f : 0f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                if (show) button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (!show) button.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    public void add_item(String nama, String noHp) {
        Kontak newKontak = new Kontak(nama, noHp);
        KAdapter.addItem(newKontak);
    }

    private void tambah_data() {
        AlertDialog.Builder buat = new AlertDialog.Builder(this);

        View vAdd = LayoutInflater.from(this).inflate(R.layout.add_kontak, null);
        final EditText nm = vAdd.findViewById(R.id.nm);
        final EditText hp = vAdd.findViewById(R.id.hp);

        buat.setView(vAdd);

        buat.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nama = nm.getText().toString().trim();
                String notelp = hp.getText().toString().trim();

                if (nama.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please fill your name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (notelp.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please fill your phone number", Toast.LENGTH_LONG).show();
                    return;
                } else if (!notelp.matches("\\d+")) {
                    Toast.makeText(getBaseContext(), "Your phone number isn't valid", Toast.LENGTH_LONG).show();
                    return;
                }

                add_item(nama, notelp);
                Toast.makeText(getBaseContext(), "Data Saved", Toast.LENGTH_LONG).show();
            }
        });

        buat.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = buat.create();
        dialog.show();
    }

    private void cari_data() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View searchView = LayoutInflater.from(this).inflate(R.layout.search_kontak, null);
        final EditText searchEditText = searchView.findViewById(R.id.searchEditText);

        builder.setView(searchView);
        AlertDialog dialog = builder.create();
        dialog.show();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                KAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void hapus_data() {
        if (selectedKontak != null) {
            // Create an AlertDialog to confirm deletion
            new AlertDialog.Builder(this).setTitle("Delete Contact").setMessage("Are you sure you want to delete " + selectedKontak.getNama() + "?")
                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                        int position = KAdapter.getPosition(selectedKontak);
                        if (position >= 0) {
                            KAdapter.deleteItem(position);
                            Toast.makeText(getBaseContext(), "Contact has been deleted", Toast.LENGTH_LONG).show();
                            selectedKontak = null;
                        }
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
        } else {
            Toast.makeText(getBaseContext(), "Please choose a contact to delete", Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.addContactButton) {
                tambah_data();
            } else if (v.getId() == R.id.deleteContactButton) {
                hapus_data();
            } else if (v.getId() == R.id.searchContactButton) {
                cari_data();
            }
        }
    };
}