package com.mhdlub.sim2048;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		
		Button startB=(Button) findViewById(R.id.start_butn);
		startB.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intt=new Intent(Welcome.this, MainActivity.class);
				startActivity(intt);
				finish();
			}
		});
		
	}

}
