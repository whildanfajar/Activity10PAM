package com.example.activity9pam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.activity9pam.adapter.AdapterBarang;
import com.example.activity9pam.database.barang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class LihatBarang extends AppCompatActivity {
    /**
     * Mendefinisikan variable yang akan dipakai
     */
    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<barang> daftarBarang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_barang);
        /* Inisialisasi RecyclerView & komponennya
         */
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        /**
         * Inisialisasi dan mengambil Firebase Database Reference
         */
        database = FirebaseDatabase.getInstance().getReference();
        /**
         * Mengambil data barang dari Firebase Realtime DB
         */
        database.child("Barang").addValueEventListener(new
                                                               ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) { /**
                                                                    * Saat ada data baru, masukkan datanya ke ArrayList
                                                                    */
                                                                       daftarBarang = new ArrayList<>();
                                                                       for (DataSnapshot noteDataSnapshot :
                                                                               dataSnapshot.getChildren()) {
                                                                           /**
                                                                            * Mapping data pada DataSnapshot ke dalam object Barang
                                                                            * Dan juga menyimpan primary key pada object Barang
                                                                            * untuk keperluan Edit dan Delete data
                                                                            */
                                                                           barang barang = noteDataSnapshot.getValue(barang.class);
                                                                           barang.setKode(noteDataSnapshot.getKey());
                                                                           /**
                                                                            * Menambahkan object Barang yang sudah dimapping
                                                                            * ke dalam ArrayList
                                                                            */
                                                                           daftarBarang.add(barang);
                                                                       }
                                                                       /**
                                                                        * Inisialisasi adapter dan data barang dalam bentuk ArrayList
                                                                        * dan mengeset Adapter ke dalam RecyclerView
                                                                        */
                                                                       adapter = new AdapterBarang(daftarBarang,
                                                                               LihatBarang.this);
                                                                       rvView.setAdapter(adapter);
                                                                   }
                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                       /**
                                                                        * Kode ini akan dipanggil ketika ada error dan
                                                                        * pengambilan data gagal dan memprint error nya
                                                                        * ke LogCat
                                                                        */
                                                                       System.out.println(databaseError.getDetails()+" "
                                                                               +databaseError.getMessage());
                                                                   }
                                                               });
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, LihatBarang.class);
    }
}
