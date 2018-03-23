package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by c-raf on 08/03/2018.
 */

public class Data extends Activity {

    EditText editName,editNumber;
    String key = null;
    boolean isKeySet = false;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        editName= findViewById(R.id.editName);
        editNumber= findViewById(R.id.editNumber);

        save= findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Contacts contacts=new Contacts(editName.getText().toString(),
                       editNumber.getText().toString(), key, isKeySet);

                Intent intent5=new Intent(Data.this,MainActivity.class);

                intent5.putExtra("data",contacts);
                setResult(2, intent5);

                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}