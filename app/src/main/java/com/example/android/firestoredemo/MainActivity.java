package com.example.android.firestoredemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    public static final String FIRSTNAME = "firstname";
    public static final String AGE = "age";
    private FirebaseFirestore db;
    private EditText etName, etAge;
    private TextView textOutput;
    private DocumentReference document;
    ListenerRegistration listenerRegistration;
    private CollectionReference collectionReferenceUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        textOutput = findViewById(R.id.txtOutPut);

        db = FirebaseFirestore.getInstance();

    //    collectionReferenceUser2 = db.collection("users2");

        document = db.collection("my_users").document("azeem_unar");



    }

    public void writeData(View view) {

/*        ArrayList<Integer> data = new ArrayList<>();
        data.add(34);
        data.add(342);
        data.add(3435);
        Map<String,Object> docData = new HashMap<String,Object>();
        docData.put("stringExp","i am going to karachi");
        docData.put("booleanExp",true);
        docData.put("numberExp",0.122345);
        docData.put("dateExp", new Timestamp(new Date()));
        docData.put("listExp",data);
        docData.put("nullExp",null);


        Map<String,Object> nestedData = new HashMap<>();
        nestedData.put("a",1);
        nestedData.put("b",2);

        docData.put("nestedMapExp",nestedData);


        db.collection("ExpDta")
                .document("myDoc")
                .set(docData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "data Saved", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        String name = etName.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
// Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put(FIRSTNAME, name);
        user.put(AGE, age);

        document.set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "doc saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


/*  collectionReferenceUser2.document("mydoc")
    .set(user)
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {

                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    });*/
/*document.set(user)
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {

                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    });*/
/*   db.collection("users").document()
    .set(user)
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {

                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    });*/

    }


    @Override
    protected void onStart() {
        super.onStart();

       EventListener<DocumentSnapshot> eventListener =  new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot data, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (data.exists() && data!= null){
                    String s;
                    Object o = data.get(FIRSTNAME);
                    if (o == null){
                        s = null;
                    }else {
                        s = o.toString();
                    }

                    String age = Objects.requireNonNull(data.get(AGE)).toString();
                    textOutput.setText(s +"|"+ age);
                }else {
                    textOutput.setText("Person Does not exists!");
                }
                if (data.getMetadata().hasPendingWrites()){
                    textOutput.append(" Local");
                }else {
                    textOutput.append(" Server");
                }
            }
        };
        listenerRegistration = document.addSnapshotListener(MetadataChanges.INCLUDE, eventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listenerRegistration != null){
            listenerRegistration.remove();
        }

    }

    public void readData(View view) {


        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
             /*       String name = Objects.requireNonNull(task.getResult().get(FIRSTNAME)).toString();
                    String age = Objects.requireNonNull(task.getResult().get(AGE)).toString();*/

                    Map<String, Object> data = task.getResult().getData();
                    String name = data.get(FIRSTNAME).toString();
                    String age = data.get(AGE).toString();
                    textOutput.setText(name +"|"+ age);

                }else {
                    String message = Objects.requireNonNull(task.getException()).toString();
                    textOutput.setText(message);
                }

            }
        });




   /*     db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                textOutput.append((document.getId() + " => " + document.getData()) + "\n");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/
    }


    public void updateData(View view) {

        String name = etName.getText().toString().trim();
       // int age = Integer.parseInt(etAge.getText().toString().trim());
// Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put(FIRSTNAME, name);
    /*    user.put(AGE, age);*/

//        document.set(user, SetOptions.merge())
        document.set(user,SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void deleteName(View view) {

        Map<String, Object> user = new HashMap<>();

        user.put(FIRSTNAME, FieldValue.delete());

        document.update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "person Name delete", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void deletePerson(View view) {

        document.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Person Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void writeObj(View view) {

        String name = etName.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
// Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put(FIRSTNAME, name);
        user.put(AGE, age);

        Person person = new Person(name,age);

        document.set(person)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "obj saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void readObj(View view) {
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){

                   Person person = task.getResult().toObject(Person.class);
                    String name = person.getName();
                    String age = String.valueOf( person.getAge());
                    textOutput.setText(name +"|"+ age);

                }else {
                    String message = Objects.requireNonNull(task.getException()).toString();
                    textOutput.setText(message);
                }

            }
        });


    }

    public void deleteObjfield(View view) {
        Map<String, Object> user = new HashMap<>();

        user.put("name", FieldValue.delete());
        document.update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "person Name delete", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateObjField(View view) {
        String name = etName.getText().toString().trim();
        Map<String, Object> user = new HashMap<>();
        user.put("name",name);
        document.set(user,SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task
                    ) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "person Name update", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}