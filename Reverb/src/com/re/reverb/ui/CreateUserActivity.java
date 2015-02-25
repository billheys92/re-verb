package com.re.reverb.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.re.reverb.R;
import com.re.reverb.androidBackend.account.CreateUserDto;
import com.re.reverb.network.AccountManagerImpl;

public class CreateUserActivity extends Activity
{
    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    String email;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            this.email = extras.getString(EMAIL_KEY);
            this.token = extras.getString(TOKEN_KEY);
        }
    }

    public void createUser(View view)
    {
        CreateUserDto createUserDto = new CreateUserDto(
                ((TextView)findViewById(R.id.username)).getText().toString(),
                ((TextView)findViewById(R.id.handle)).getText().toString(),
                email,
                token,
                "test about me"
        );

        AccountManagerImpl.createUser(createUserDto);
    }
}
