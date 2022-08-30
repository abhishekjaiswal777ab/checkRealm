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
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {

    String App_id="checkrealm-mxzyz";
    Button button;
    EditText editText;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById((R.id.editTextTextPersonName));

        Realm.init(this);
        App app=new App(new AppConfiguration.Builder(App_id).build());

        app.loginAsync(Credentials.anonymous(), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                    Log.v("User","Login Successgull");
                else
                    Log.v("User","Login Unsuccessfull");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              User user=app.currentUser();
              mongoClient=user.getMongoClient("mongodb-atlas");
              mongoDatabase=mongoClient.getDatabase("userDatabase");
              MongoCollection<Document> mongoCollection=mongoDatabase.getCollection("slotsData");

              mongoCollection.insertOne(new Document("userid",user.getId()).append("data",editText.getText().toString())).getAsync(result -> {
                          if(result.isSuccess())
                          {
                              Log.v("Kuch bhi","data insertd succesfully");
                          }

                          else
                          {
                              Log.v("kuch bhi","Error: "+result.getError().toString());
                          }
                      }
              );
            }
        });

    }
};