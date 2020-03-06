package Android.pan;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import java.text.*;
import java.util.*;

public class KisTurmilar
{
	//By BilQut 4/2/2020
	public static void TextViewHatNushisi(Activity A,int id,String FontNami){
		TextView a=(TextView)A.findViewById(id);
		a.setTypeface(Typeface.createFromAsset(A.getAssets(),FontNami));
	}
	
	public static void EditTextHatNushisi(Activity A,int id,String FontNami){
		EditText e=(EditText)A.findViewById(id);
		e.setTypeface(Typeface.createFromAsset(A.getAssets(),FontNami));
	}
	
	public static void AskartixRamkisi(Activity A,String Mazmun){
		new AlertDialog.Builder(A).setMessage(Mazmun).show();
		
	}
	
	public static void Askartma(Activity A,String Mazmun){
	Toast.makeText(A,Mazmun,Toast.LENGTH_LONG).show();
}

public static void TorKorguqTallax(Activity A,String Adiris){
	Uri uri=Uri.parse(Adiris);
	Intent intent=new Intent();
	intent.setAction(Intent.ACTION_VIEW);	
	intent.setData(uri);						
	A.startActivity(intent);	
}

public static void UnimKoxux(Activity A, View v){
	//Animation anim = AnimationUtils.loadAnimation(A, R.anim.anim2);
	//v.startAnimation(anim);		
}

	public String HazirKiWakit(){
        return new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
    }
	
	public static void PutunBatFont(Activity A, String FontNami, int LayoutID){
        View v= A.findViewById(LayoutID);
        ViewGroup VG = (ViewGroup) v;
        if (VG.getChildCount() > 0) {
            for (int ic = 0; ic < VG.getChildCount(); ic++) {
                View V = VG.getChildAt(ic);
                try {
                    ((TextView) V).setTypeface(Typeface.createFromAsset(A.getAssets(), FontNami));
                } catch (Exception ignored) {
                }
            }
        }
    }
}
