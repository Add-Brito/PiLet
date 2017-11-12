package com.hk.pimote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
	private ServerHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.ma_listView);

		handler = ((MainApplication) getApplication()).startup();

		final SingularAdapter adpt = new SingularAdapter(handler);
		listView.setAdapter(adpt);

		final Button shutdownButton = (Button) findViewById(R.id.ma_shutdown_button);

		shutdownButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(handler == null)
				{
					shutdownButton.setText(R.string.shutdown);
					handler = ((MainApplication) getApplication()).startup();
					adpt.notifyDataSetChanged();
				}
				else
				{
					shutdownButton.setText(R.string.startup);
					handler = ((MainApplication) getApplication()).shutdown();
					adpt.notifyDataSetChanged();
				}
			}
		});
    }

    private class SingularAdapter extends BaseAdapter implements ProgressListener
	{
		private final ServerHandler handler;
		private RadioButton button;

		private SingularAdapter(ServerHandler handler)
		{
			this.handler = handler;
		}

		@Override
		public int getCount()
		{
			return 1;
		}

		@Override
		public Object getItem(int i)
		{
			return null;
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup)
		{
			if(view == null)
			{
				LayoutInflater lf = LayoutInflater.from(MainActivity.this);
				view = lf.inflate(R.layout.layout_cont, viewGroup, false);
			}

			TextView labelView = view.findViewById(R.id.lc_label);

			final RadioButton changeButton = view.findViewById(R.id.lc_button);
			changeButton.setEnabled(handler != null);

			if(handler == null)
			{
				labelView.setText("-");
				changeButton.setText(R.string.disabled);
			}
			else
			{
				labelView.setText(handler.getName());
				changeButton.setText(R.string.turn_on);
			}

			changeButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if(handler == null)
					{
						throw new RuntimeException("This should never in the existence of mankind would this ever print");
					}
					handler.setListener(SingularAdapter.this);
					button = changeButton;
					button.setText(R.string.loading);
					handler.toggleSwitch();
				}
			});

			return view;
		}

		@Override
		public void onReceive(boolean isOn)
		{
			button.setChecked(isOn);
			button.setText(isOn ? R.string.turn_off : R.string.turn_on);
			handler.setListener(null);
		}
	}
}
