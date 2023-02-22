package android.example.checkrealm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainActivity extends AppCompatActivity {

    String App_id = "yiapp-ojfus";
    Button button;
    EditText editText;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById((R.id.editTextTextPersonName));

        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(App_id).build());

        app.loginAsync(Credentials.anonymous(), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess())
                    Log.v("User", "Login Successgull");
                    Log.v("Abhihsek","my comment");
                else
                    Log.v("User", "Login Unsuccessfull");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("tnpProjectDB");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("verticals");
                mongoCollection.insertOne(new Document("userId",user.getId()).append("data",editText.getText().toString()
                )).getAsync(task->{
                    if(task.isSuccess())
                    {
                        Log.v("kuch bhi ","ues");
                    }
                    else
                    {
                        Log.v("kuch vhi",task.getError().toString());
                    }
                });
            }
        });
    }
}