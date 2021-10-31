package com.example.myseboadminstaff;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myseboadminstaff.asset.Asset;
import com.example.myseboadminstaff.reservation.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FirebaseHelper extends ViewModel {

    private final FirebaseFirestore mFirestore;
    private final String TAG = FirebaseHelper.class.getSimpleName();


    private MutableLiveData<List<Asset>> assetListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Reservation>> reservationListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Reservation> reservationMutableLiveData = new MutableLiveData<>();

    public FirebaseHelper() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Asset>> getAssetListMutableLiveData() {
        return assetListMutableLiveData;
    }

    public void readAssetList() {

        CollectionReference collection = mFirestore
                .collection("itemList");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (DocumentSnapshot document : task.getResult()) {
//                    Asset asset = document.toObject(Asset.class);
//                    asset.setId(document.getId());
//                    assetList.add(asset);
//                }
                List<Asset> assetList = new ArrayList<>();


                for (QueryDocumentSnapshot doc : value) {
                    Asset asset = doc.toObject(Asset.class);
                    asset.setId(doc.getId());

                    assetList.add(asset);

                }


                assetListMutableLiveData.postValue(assetList);
            }
        });
    }

    public void addAsset(String itemName, int quantity) {
        CollectionReference collection = mFirestore
                .collection("itemList");

        HashMap<String, Object> data = new HashMap<>();
        data.put("item", itemName);
        data.put("quantity", quantity);


        collection.add(data);

    }

    public void deleteAsset(String id) {
        DocumentReference documentReference = mFirestore
                .collection("itemList").document(id);

        documentReference.delete();
    }

    public void editAsset(String sId, String sItem, int iQuantity) {
        CollectionReference collection = mFirestore
                .collection("itemList");

        HashMap<String, Object> data = new HashMap<>();
        data.put("item", sItem);
        data.put("quantity", iQuantity);


        collection.document(sId).set(data, SetOptions.merge());
        Log.d(TAG, "editAsset: " + collection.getPath());
    }

    public void readReservationList() {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                List<Reservation> reservationList = new ArrayList<>();


                for (QueryDocumentSnapshot doc : value) {
                    Reservation reservation = doc.toObject(Reservation.class);
                    reservation.setId(doc.getId());

                    reservationList.add(reservation);

                    List<Asset> equipment = reservation.getEquipment();

                    Log.d(TAG, "onEvent: " + reservation.getEventName());

                }


                Log.d(TAG, "onEvent: ");
                reservationListMutableLiveData.postValue(reservationList);
            }
        });
    }

    public MutableLiveData<List<Reservation>> getReservationListMutableLiveData() {
        return reservationListMutableLiveData;
    }

    public MutableLiveData<Reservation> getReservationMutableLiveData() {
        return reservationMutableLiveData;
    }

    public void readReservationDetail(String reservationId) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        collection.document(reservationId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Reservation reservation = task.getResult().toObject(Reservation.class);
                reservationMutableLiveData.postValue(reservation);
            }
        });
    }

    public void acceptReservation(String reservationId) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("status", 2);
        collection.document(reservationId).set(hashMap, SetOptions.merge());
    }

    public void rejectReservation(String reservationId) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("status", 1);
        collection.document(reservationId).set(hashMap, SetOptions.merge());
    }

    public void deleteReservationAsset(String reservationId, Asset asset) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        DocumentReference documentReference = collection.document(reservationId);

        HashMap <String, Object> assetHashMap = new HashMap<>();
        assetHashMap.put("id", asset.getId());
        assetHashMap.put("item", asset.getItem());
        assetHashMap.put("new", true);
        assetHashMap.put("quantity", asset.getQuantity());



        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("equipment", FieldValue.arrayRemove(assetHashMap));

        documentReference.update(hashMap);

        Log.d(TAG, "deleteReservationAsset: " + reservationId);


    }

    public void editAssetReservation(String assetId, String assetName, int quantity, String reservationId, int originalQuantity) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        DocumentReference documentReference = collection.document(reservationId);

        HashMap <String, Object> assetHashMap = new HashMap<>();
        assetHashMap.put("id", assetId);
        assetHashMap.put("item", assetName);
        assetHashMap.put("new", true);
        assetHashMap.put("quantity", originalQuantity);



        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("equipment", FieldValue.arrayRemove(assetHashMap));

        documentReference.update(hashMap);

        assetHashMap.put("quantity", quantity);
        hashMap.put("equipment", FieldValue.arrayUnion(assetHashMap));

        documentReference.update(hashMap);

    }

    public void readAcceptedReservationList() {

        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");
        collection.whereEqualTo("status", 2).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                List<Reservation> reservationList = new ArrayList<>();


                for (QueryDocumentSnapshot doc : value) {
                    Reservation reservation = doc.toObject(Reservation.class);
                    reservation.setId(doc.getId());

                    reservationList.add(reservation);

                    List<Asset> equipment = reservation.getEquipment();

                    Log.d(TAG, "onEvent: " + reservation.getEventName());

                }


                Log.d(TAG, "onEvent: ");
                reservationListMutableLiveData.postValue(reservationList);
            }
        });

    }
}
