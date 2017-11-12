package com.hk.pimote;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		final ServerHandler handler = ((MainApplication) getApplication()).getHandler();
		final Handler hnd = new Handler();

		hnd.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				if(handler.connectionEstablished)
				{
					startActivity(new Intent(LoadingActivity.this, MainActivity.class));
				}
				else
				{
					hnd.postDelayed(this, 500);
				}
			}
		}, 500);
	}
}
