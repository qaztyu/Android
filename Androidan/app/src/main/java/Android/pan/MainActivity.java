package Android.pan;

import android.app.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity 
{
//BilQut Tori 
//بىلقۇت تورى پىروگىرامما دۇنياسى
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
           //getActionBar().hide();
     	    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//Apni Stoni Wa Usti Kismini Maslaxturux
            setContentView(R.layout.main);
			//KisTurmilar.AskartixRamkisi(this,"Man Bir Askartix Ramkisi!");
			//KisTurmilar.Askartma(this,"Man Bolsam Toast");
			//KisTurmilar.TextViewHatNushisi(this,R.id.bilqut,"UyghurFont.ttf");
    }
}