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

    EditText editName,editEmail,editNumber;
    ImageView contactImage;
    Button save;
    private int mGetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        editName= (EditText) findViewById(R.id.editName);
        editEmail= (EditText) findViewById(R.id.editEmail);
        editNumber= (EditText) findViewById(R.id.editNumber);

        contactImage= (ImageView) findViewById(R.id.ContactImage);

        save= (Button) findViewById(R.id.save);

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2=new Intent(Data.this,Images.class);

                startActivityForResult(intent2,1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Contacts contacts=new Contacts(editName.getText().toString(),
                        editEmail.getText().toString(),editNumber.getText().toString(),
                        mGetImage);

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

        mGetImage =data.getExtras().getInt("img",1);
        contactImage.setImageResource(mGetImage);

    }
}