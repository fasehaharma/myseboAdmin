package com.example.myseboadminstaff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myseboadminstaff.asset.Asset;
import com.example.myseboadminstaff.reservation.Reservation;
import com.example.myseboadminstaff.reservation.ReservationDetailActivity;
import com.example.myseboadminstaff.usermanagement.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FirebaseHelper extends ViewModel {

    private final FirebaseFirestore mFirestore;
    private final String TAG = FirebaseHelper.class.getSimpleName();


    private MutableLiveData<List<Asset>> assetListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Reservation>> reservationListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Reservation> reservationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userDetailMutableLiveData = new MutableLiveData<>();

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

        List<Long> longList = new ArrayList<>();
        longList.add(Long.valueOf(Reservation.STATUS_ACCEPT));
        longList.add(Long.valueOf(Reservation.STATUS_PICKUP));
        longList.add(Long.valueOf(Reservation.STATUS_RETURN));

        collection.whereIn("status", longList).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void pickupReservation(String reservationId, String name, String staffId, String phone, String pickUpDate) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");


        DocumentReference documentReference = collection.document(reservationId);
        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("pickUpName",name);
        hashMap.put("pickUpId", staffId);
        hashMap.put("pickUpPhone", phone);
        hashMap.put("status",Reservation.STATUS_PICKUP);

        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(pickUpDate);
            hashMap.put("pickUpDate", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        documentReference.set(hashMap, SetOptions.merge());
    }

    public void returnReservation(String reservationId, String name, String staffId, String phone, String note, String returnDate) {
        CollectionReference collection = mFirestore
                .collection("EquipmentReservation");

        DocumentReference documentReference = collection.document(reservationId);
        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("returnName",name);
        hashMap.put("returnIdStaff", staffId);
        hashMap.put("returnPhone", phone);
        hashMap.put("note", note);
        hashMap.put("status",Reservation.STATUS_RETURN);

        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(returnDate);
            hashMap.put("returnTheDate", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        documentReference.set(hashMap, SetOptions.merge());
    }

    public MutableLiveData<List<User>> getUserListMutableLiveData() {
        return userListMutableLiveData;
    }

    public void getUserList(){
        Query collection = mFirestore
                .collection("user").whereEqualTo("type", "admin");

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> userList = new ArrayList<>();
                QuerySnapshot result = task.getResult();

                for (QueryDocumentSnapshot doc : result) {
                    User user = doc.toObject(User.class);
                    userList.add(user);
                }

                userListMutableLiveData.postValue(userList);
            }
        });
    }

    public MutableLiveData<User> getUserDetailMutableLiveData() {
        return userDetailMutableLiveData;
    }

    public void getUserDetail(String userId) {
        CollectionReference collection = mFirestore
                .collection("user");

        collection.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                userDetailMutableLiveData.postValue(user);
            }
        });
    }

    public void checkUserBeforeLogin(String uid, Context context) {
        CollectionReference collection = mFirestore
                .collection("user");

        collection.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                if (user.getVerify()){
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else{
                    Toast.makeText(context, "Please wait for verify", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
            }
        });
    }

    public void acceptAdmin(String userId) {
        CollectionReference collection = mFirestore
                .collection("user");

        DocumentReference documentReference = collection.document(userId);
        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("verify",true);


        documentReference.set(hashMap, SetOptions.merge());
    }

    public void rejectAdmin(String userId) {
        CollectionReference collection = mFirestore
                .collection("user");

        DocumentReference documentReference = collection.document(userId);
        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("verify",false);


        documentReference.set(hashMap, SetOptions.merge());
    }

    public void viewPDFLetter(Reservation reservation, Context context) throws IOException{

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String letterPath = reservation.getLetterPath();


        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(letterPath);

        File localFile = File.createTempFile("letter3", ".pdf");
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);

            }
        });
    }

    public void viewPDFID(Reservation reservation, Context context) throws IOException {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String idPath = reservation.getIdPath();

        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(idPath);

        File localFile = File.createTempFile("letter3", ".pdf");
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);

            }
        });


//        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                Log.d(TAG, "onSuccess: Success Download" + localFile.getPath());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(localFile), "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                context.startActivity(intent);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                exception.printStackTrace();
//            }
//        });
    }
}
