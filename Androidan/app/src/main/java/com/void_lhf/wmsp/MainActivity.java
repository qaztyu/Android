package com.void_lhf.wmsp;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.TextView.*;
import es.dmoral.toasty.*;
import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import org.json.*;

public class MainActivity extends Activity 
{
	private Context currentContext = MainActivity.this;
	
	private long backTempTime = 0;
	private long backTempTime2 = 0;
	private long backTempTime3 = 0;
	
	private EditText searchEditText;
	private Spinner searchSpinner;
	private Button searchButton;
	private LinearLayout actionBarLayout;
	private String searchUrl;
	
	private SharedPreferences preferences;
	
	private ImageView siteIcon;
	
	private String selectedSym1 = "・  ";
	private String selectedSym2 = "  ・";
	
	private String[] searchHostList2 = {
		"http://m2.pansoso.com/zh/%key%_%index%",
		"http://m.58wangpan.com/so?order=feed_time&feed_time=0&keyword=%key%&page=%index%",
		"http://m.rufengso.net/s/comb/n-%key%&s-feedtime1/%index%",
		"https://www.fastsoso.cn/search?page=%index%&k=%key%&s=1&t=-1",
		"https://www.xiaobaipan.com/list-%key%-p%index%.html",
		"https://wx.panfa.net/s/%key%_%index%",
		"https://www.52sopan.com:443/search.php?mode=so&q=%key%&page_size=30&page_number=%index%",
		"https://www.dalipan.com/search?keyword=%key%&page=%index%",
		"http://api.juapp8.com/search?keyword=%key%&page=%index%"
	};
	private String[] searchSpinnerName = {
		"盘搜搜",
		"58网盘",
		"如风搜",
		"Fast搜搜",
		"小白盘",
		"盘发网盘",
		"我爱搜盘",
		"大力盘",
		"聚应用"
	};
	
	private int listIndex = 0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		View customActionBar = LayoutInflater.
			from(currentContext).
			inflate(R.layout.custom_action_bar,null);
		actionBarLayout = customActionBar.findViewById(R.id.actionBarLayout);
		actionBarLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(System.currentTimeMillis()-backTempTime<500) {
						finish();
						System.exit(0);
						android.os.Process.killProcess(android.os.Process.myPid());
					}
					backTempTime = System.currentTimeMillis();
				}
			});
		actionBarLayout.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					View view = LayoutInflater.from(currentContext)
						.inflate(R.layout.about_dialog,null);
					TextView aboutText = view.findViewById(R.id.aboutText);
					aboutText.setText(
						""+
						getString(R.string.about_version_name)+"\n"+APKVersionCodeUtils.getVerName(currentContext)+"\n\n"+
						getString(R.string.about_responsible)+"\n"+getString(R.string.responsible_text)+"\n\n"+
						getString(R.string.about_icons_author)+"\n"+getString(R.string.icons_author)+"\n\n"+
						getString(R.string.about_project_url)+"\n"+getString(R.string.github)
					);
					new AlertDialog.Builder(currentContext)
						.setView(view)
						.show();
					return true;
				}
			});
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
			ActionBar.LayoutParams.MATCH_PARENT,
			ActionBar.LayoutParams.MATCH_PARENT,
			Gravity.CENTER);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setCustomView(customActionBar,layoutParams);
		getActionBar().setElevation(0);
		if(Build.VERSION.SDK_INT>=21) {
			Toolbar parent = (Toolbar) customActionBar.getParent();
			parent.setContentInsetsAbsolute(0, 0);
		}
		
        setContentView(R.layout.main);
		checkUpdate();
		preferences = currentContext.getSharedPreferences("setting",MODE_PRIVATE);
		siteIcon = findViewById(R.id.siteIcon);
		searchEditText = findViewById(R.id.searchEditText);
		searchEditText.setOnEditorActionListener(new OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
				{
					// TODO: Implement this method
					if(p2==EditorInfo.IME_ACTION_SEARCH||p3.getKeyCode()==KeyEvent.ACTION_DOWN) {
						if(searchEditText.getText().toString().replace(" ","").length()>0) {

							searchUrl = searchHostList2[listIndex].
								replace("%key%",searchEditText.getText().toString());
							Intent intent = new Intent(currentContext,SearchListActivity.class);
							intent.putExtra("searchUrl1",searchUrl);
							intent.putExtra("searchWord",searchEditText.getText().toString());
							intent.putExtra("listIndex",listIndex);
							intent.putExtra("spinnerName",searchSpinnerName[listIndex]);
							startActivity(intent);
						} else {
							if(System.currentTimeMillis()-backTempTime2<2000) {

							}else {
								Toast a = Toasty.error(currentContext,getString(R.string.empty_toast_message),Toast.LENGTH_SHORT,true);
								a.setGravity(Gravity.TOP,0,150);
								a.show();
							}
							backTempTime2 = System.currentTimeMillis();
						}
						return true;
					} else {
						return false;
					}
				}
			});
		searchSpinner = findViewById(R.id.searchSpinner);
		ArrayAdapter adapter = new ArrayAdapter<String>(currentContext,R.layout.custom_search_spinner,searchSpinnerName);
		searchSpinner.setAdapter(adapter);
		
		int selection = preferences.getInt("default_interface",0);
		if(selection > searchSpinnerName.length-1) {
			selection = 0;
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("default_interface",selection);
			editor.apply();
		}
		searchSpinner.setSelection(selection);
		
		searchSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					for(int nameIndex=0;nameIndex<searchSpinnerName.length;nameIndex++) {
						searchSpinnerName[nameIndex] = searchSpinnerName[nameIndex].replace(selectedSym1,"").replace(selectedSym2,"");
					}
					searchSpinnerName[p3] = selectedSym1+searchSpinnerName[p3]+selectedSym2;
					listIndex = p3;
					String siteIndex = "site"+(p3+1);
					if(getResources().getIdentifier(siteIndex,"drawable",getPackageName())!=0) {
						siteIcon.setImageResource(getResources().getIdentifier(siteIndex,"drawable",getPackageName()));
					} else {
						siteIcon.setImageResource(R.drawable.site0);
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
		siteIcon.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					searchSpinner.performClick();
					// TODO: Implement this method
				}
			
		});
		searchButton = findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(searchEditText.getText().toString().replace(" ","").length()>0) {
						searchUrl = searchHostList2[listIndex].
							replace("%key%",searchEditText.getText().toString());
						Intent intent = new Intent(currentContext,SearchListActivity.class);
						intent.putExtra("searchUrl1",searchUrl);
						intent.putExtra("searchWord",searchEditText.getText().toString());
						intent.putExtra("listIndex",listIndex);
						intent.putExtra("spinnerName",searchSpinnerName[listIndex].replace(selectedSym1,"").replace(selectedSym2,""));
						startActivity(intent);
					} else {
						if(System.currentTimeMillis()-backTempTime2<2000) {

						}else {
							Toast a = Toasty.error(currentContext,getString(R.string.empty_toast_message),Toast.LENGTH_SHORT,true);
							a.setGravity(Gravity.TOP,0,150);
							a.show();
						}
						backTempTime2 = System.currentTimeMillis();
					}
				}
			});
		searchButton.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					SharedPreferences.Editor editor = preferences.edit();
					editor.putInt("default_interface",listIndex);
					editor.apply();
					if(System.currentTimeMillis()-backTempTime3<2000) {

					}else {
						Toast a = Toasty.success(currentContext,"\""+searchSpinnerName[listIndex].replace(selectedSym1,"").replace(selectedSym2,"")+"\""+getString(R.string.set_default_interface),Toast.LENGTH_SHORT,true);
						a.setGravity(Gravity.TOP,0,150);
						a.show();
					}
					backTempTime3 = System.currentTimeMillis();
					return true;
				}
			});
    }

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		if(System.currentTimeMillis()-backTempTime<2000) {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}else {
			Toast a = Toasty.warning(currentContext,getString(R.string.exit_toast_message),Toast.LENGTH_SHORT,true);
			a.setGravity(Gravity.TOP,0,150);
			a.show();
		}
		backTempTime = System.currentTimeMillis();
	}
	
	private void checkUpdate() {
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					// TODO: Implement this method
					try
					{
						String updateInfo = HttpTools.getJsonByInternet("https://api.github.com/repos/voidlhf/NetdiskSearcherForAndroid/releases/latest");
						JSONObject jo = new JSONObject(updateInfo);
						JSONArray ja = jo.getJSONArray("assets");
						JSONObject jo2 = ja.getJSONObject(0);
						final String browser_download_url = jo2.getString("browser_download_url");
						String tag_name = jo.getString("tag_name");
						final String name = jo.getString("name");
						//final String published_at = jo.getString("published_at");
						final String body = jo.getString("body");
						
						if(CheckTools.needUpdate(Long.parseLong(tag_name),currentContext)) {

							runOnUiThread(new Runnable(){

									@Override
									public void run()
									{
										// TODO: Implement this method
										AlertDialog dialog = new AlertDialog.Builder(currentContext)
											.setMessage("新版本：\n"+name+"\n\n更新日志：\n"+body)
											.setPositiveButton("下载", new AlertDialog.OnClickListener(){

												@Override
												public void onClick(DialogInterface p1, int p2)
												{
													// TODO: Implement this method
													Intent intent = new Intent(Intent.ACTION_VIEW);
													intent.setData(Uri.parse(browser_download_url));
													startActivity(intent);
												}
											})
											.show();
										dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.themeColor));
									}
								});
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
	}
}
