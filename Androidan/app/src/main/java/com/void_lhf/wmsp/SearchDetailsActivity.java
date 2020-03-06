package com.void_lhf.wmsp;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import es.dmoral.toasty.*;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import android.webkit.*;
import java.io.*;
import java.util.*;

public class SearchDetailsActivity extends Activity
{
	private TextView actionBarTitle ;
	private Intent lastIntent;
	private LinearLayout actionBarLayout;
	private long backTempTime = 0;
	private long backTempTime2 = 0;
	private long backTempTime3 = 0;
	private long backTempTime4 = 0;
	private Context currentContext = SearchDetailsActivity.this;
	private String detailsUrl;
	private String detailsName;
	private String detailsPass;
	private String detailsDes;
	private int detailsIndex;
	
	private TextView bdInfo;
	private LinearLayout clearLayout;
	private Button copyButton;
	private Button openButton;
	private Button shareButton;
	private WebView webview1;
	
	private ImageView effectiveStatus;
	
	private String bdBaseUri = "";
	private String shareUri = "";
	
	private boolean needLoad = true;
	private boolean isFinish = false;
	
	private ProgressBar infoProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_details);
		initView();
		getBdUrl();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getBdUrl() {
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					// TODO: Implement this method
					switch(detailsIndex) {
						case 0 : {
								try
								{
									Connection conn1 = Jsoup.connect(detailsUrl);
									conn1.userAgent("MicroMessenger");
									Document document1 = conn1.get();
									
									final String des;
									String des1 = "文件名称："+document1.select("h1").text()+"\n\n"+
										document1.select(".info ul li").get(2).text()+"\n\n"+
										document1.select(".info ul li").get(3).text();
									if(document1.select("div.key").text().length()>0) {
										des = des1 + "\n\n"+document1.select("div.key").text();
									} else {
										des = des1;
									}
									
									String tempUrl2 = document1.select("a.down_button_link").attr("href");
									if(tempUrl2.length()==0) {
										updateNoResult();
										break;
									}
									final Document document3 = Jsoup.connect(tempUrl2).validateTLSCertificates(false).post();
									final String baseUri = document3.baseUri();
									bdBaseUri = baseUri;
									final boolean effectiveResult = isEffective(baseUri);
									shareUri = des+"\n\n链接："+baseUri;
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												showEffectiveState(effectiveStatus,effectiveResult);
												bdInfo.setText(des.replace("文件名称：","").replace("文件大小：","").replace("分享时间：",""));
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;
						}//end case
						
						case 1 : {
								try
								{
									Document document1 = Jsoup.connect(detailsUrl).validateTLSCertificates(false).get();
									Elements elements1;
									if(document1.select("a#html5_href_file").size()!=0) {
										elements1 = document1.select("a#html5_href_file");
									} else {
										elements1 = document1.select("a#html5_href_dir");
									}
									final String des;
									String tempUrl1 = elements1.attr("href");
									String tempUrl2 = "https://m.58wangpan.com"+tempUrl1;
									Document document2 = Jsoup.connect(tempUrl2).validateTLSCertificates(false).get();
									final String baseUri;
									
									if(document2.select("#tip_msg p").last().text().startsWith("http")) {
										baseUri = document2.select("#tip_msg p").last().text();
										des = "文件名称："+
											document1.select("div.file-name").text()+"\n\n"+
											document1.select("div.file-time").get(0).text();
									} else {
										baseUri = document2.select("#tip_msg a").attr("href");
										des = "文件名称："+
											document1.select("div.file-name").text()+"\n\n"+
											document1.select("div.file-time").get(0).text()+"\n\n"+
											document2.select("#tip_msg p").last().text();
									}
									
									bdBaseUri = baseUri;
									final boolean effectiveResult = isEffective(baseUri);
									shareUri = des+"\n\n链接："+baseUri;
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("文件名称：","").replace("分享时间：",""));
												showEffectiveState(effectiveStatus,effectiveResult);
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							break;
						}//end case
						
						case 2 : {
								try
								{
									Document document1 = Jsoup.connect(detailsUrl).validateTLSCertificates(false).get();
									final String des = "文件名称："+document1.select("div.resource-page h1").text()+"\n\n"+
										document1.select("div.share-info p.sep").text();
									final String tempUrl1 = document1.select("a#redirect-link").attr("href");
									Document document2 = Jsoup.connect(tempUrl1).validateTLSCertificates(false).get();
									final String baseUri = document2.select("div.main a").attr("href");
									if(baseUri.length()!=0) {
										bdBaseUri = baseUri;
										shareUri = des+"\n\n链接："+baseUri;
									} else {
										bdBaseUri = tempUrl1;
										shareUri = des+"\n\n链接："+tempUrl1;
									}
									final boolean effectiveResult1 = isEffective(baseUri);
									final boolean effectiveResult2 = isEffective(tempUrl1);
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("文件名称：","").replace("上传用户：",""));
												if(baseUri.length()!=0) {
													showEffectiveState(effectiveStatus,effectiveResult1);
												} else {
													showEffectiveState(effectiveStatus,effectiveResult2);
												}
												
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							break;
						}//end case
						
						case 3 : {
								try
								{
									System.out.println("测试"+detailsUrl);
									Document document1 = Jsoup.connect(detailsUrl).validateTLSCertificates(false).get();
									final String des = detailsName+"\n\n分享时间："+detailsDes;
									final String baseUri = document1.getElementsByAttributeValue("class","btn btn-success").get(0).attr("onclick").replace("window.open('/redirect?url=","").replace("','_blank')","");
									bdBaseUri = baseUri;
									shareUri = "文件名称："+des+"\n\n链接："+baseUri;
									final boolean effectiveResult = isEffective(baseUri);
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("分享时间：",""));
												showEffectiveState(effectiveStatus,effectiveResult);
											}
										});
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
								break;
						}//endcase
						
						case 4 : {
								try
								{
									Document document1 = Jsoup.connect(detailsUrl).validateTLSCertificates(false).get();
									final String des = "文件名称："+document1.select("h1#tb_kks").text()+"\n\n"+
										document1.select("span.date").get(0).text();
									String tempUrl = "https://www.xiaobaipan.com"+document1.select("div.modal-footer a").attr("href");
									final String baseUri = Jsoup.connect(tempUrl).validateTLSCertificates(false).get().baseUri();
									bdBaseUri = baseUri;
									shareUri = des+"\n\n链接："+baseUri;
									final boolean effectiveResult = isEffective(baseUri);
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("文件名称：","").replace("分享时间:",""));
												if(bdBaseUri.startsWith("https://pan.baidu.com/")
												   ||bdBaseUri.startsWith("http://pan.baidu.com/")
												   ||bdBaseUri.startsWith("https://yun.baidu.com/")
												   ||bdBaseUri.startsWith("http://yun.baidu.com/")) {
													showEffectiveState(effectiveStatus,effectiveResult);
												} else {
													effectiveStatus.setImageResource(R.drawable.effective_unkonw);
												}
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;
							}//endcase
							
						case 5 : {
								try
								{
									Document document1 = Jsoup.connect(detailsUrl).validateTLSCertificates(false).get();
									final String des = "文件名称："+detailsName+"\n\n分享时间："+detailsDes;
									final String baseUri = document1.select("a#pan_button").attr("href");
									Connection conn1 = Jsoup.connect(baseUri);
									Connection.Response res = conn1.execute();
									String url2 = res.header("Refresh");
									String baseUri2 = url2.substring(url2.indexOf("http"));
									bdBaseUri = baseUri2;
									shareUri = des+"\n\n链接："+bdBaseUri;
									final boolean effectiveResult = isEffective(bdBaseUri);
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("分享时间：","").replace("文件名称：",""));
												showEffectiveState(effectiveStatus,effectiveResult);
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;
							}//endcase
						
						case 6 : {
								try
								{
									Document document1 = Jsoup.connect("https://www.52sopan.com:443/search.php?mode=get-password&id="+detailsPass).validateTLSCertificates(false).get();
									JSONObject jsob = new JSONObject(document1.text());
									String des1;
									if(jsob.get("success").toString().equals("true")) {
										try {
											des1 = "文件名称："+detailsName+"\n\n分享时间："+detailsDes+"\n\n提取码："+jsob.get("pwd");
										} catch (Exception e) {
											e.printStackTrace();
											des1 = "文件名称："+detailsName+"\n\n分享时间："+detailsDes;
										}
									} else {
										des1 = "文件名称："+detailsName+"\n\n分享时间："+detailsDes;
									}
									final String des = des1;
									final String baseUri = detailsUrl;
									bdBaseUri = baseUri;
									shareUri = des+"\n\n链接："+baseUri;
									final boolean effectiveResult = isEffective(baseUri);
									runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												// TODO: Implement this method
												infoProgressBar.setVisibility(View.GONE);
												bdInfo.setText(des.replace("文件名称：","").replace("分享时间：",""));
												showEffectiveState(effectiveStatus,effectiveResult);
											}
										});
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;
							}//end case
							
						case 7 : {
								try
								{
									if(needLoad) {
										runOnUiThread(new Runnable(){

												@Override
												public void run()
												{
													// TODO: Implement this method
													Map<String,String> extraHeaders = new HashMap<String, String>();
													String dip = IpTools.getDip();
													extraHeaders.put("x-forwarded-for",dip);
													webview1.loadUrl(detailsUrl,extraHeaders);
												}
											});
										needLoad = false;
									} 
									final String apiUrl = "https://www.dalipan.com/api/private?id="+detailsUrl.replace("https://www.dalipan.com/detail/","");
									String json1 = HttpTools.getJsonByInternet(apiUrl);
									if(json1!=null) {
										JSONObject jsonob1 = new JSONObject(json1);
										Boolean haspwd = jsonob1.getBoolean("haspwd");
										String pwd = jsonob1.getString("pwd");
										String url = jsonob1.getString("url");
										final String des ;
										bdBaseUri = url;
										if(haspwd) {
											des = detailsName+"\n\n"+detailsDes+"\n\n提取码："+pwd;
											shareUri = "文件名称："+detailsName+"\n\n分享时间："+detailsDes+"\n\n链接："+bdBaseUri+"\n\n提取码："+pwd;
										} else {
											des = detailsName+"\n\n"+detailsDes;
											shareUri = "文件名称："+detailsName+"\n\n分享时间："+detailsDes+"\n\n链接："+bdBaseUri;
										}
										final boolean effectiveResult = isEffective(bdBaseUri);
										runOnUiThread(new Runnable(){

												@Override
												public void run()
												{
													// TODO: Implement this method
													infoProgressBar.setVisibility(View.GONE);
													bdInfo.setText(des.replace("文件名称：","").replace("分享时间：",""));
													showEffectiveState(effectiveStatus,effectiveResult);
												}
											});
									}//endif
	
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							break;
						}//end case
						
						case 8 : {
							try {
								final String des = detailsName+"\n\n分享时间："+detailsDes;
								final String baseUri = detailsUrl;
								bdBaseUri = baseUri;
								shareUri = "文件名称："+des+"\n\n链接："+baseUri;
								runOnUiThread(new Runnable(){

										@Override
										public void run()
										{
											// TODO: Implement this method
											infoProgressBar.setVisibility(View.GONE);
											bdInfo.setText(des.replace("分享时间：",""));
											effectiveStatus.setImageResource(R.drawable.effective);
										}
								});
							}catch(Exception e) {
								e.printStackTrace();
							}
							break;
						}//end case
					}
				}
			}).start();
	}
	
	private void initView(){
		
		lastIntent = getIntent();
		detailsUrl = lastIntent.getStringExtra("detailsUrl");
		detailsName = lastIntent.getStringExtra("detailsName");
		detailsPass = lastIntent.getStringExtra("detailsPass");
		detailsDes = lastIntent.getStringExtra("detailsDes");
		detailsIndex = lastIntent.getIntExtra("detailsIndex",0);
		infoProgressBar = findViewById(R.id.info_progress_bar);
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
					}
					backTempTime = System.currentTimeMillis();
				}
			});
		actionBarTitle = customActionBar.findViewById(R.id.actionBarTitle);
		actionBarTitle.setGravity(Gravity.CENTER);
		actionBarTitle.setText(getString(R.string.search_result));
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
			ActionBar.LayoutParams.MATCH_PARENT,
			ActionBar.LayoutParams.MATCH_PARENT,
			Gravity.CENTER);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setCustomView(customActionBar,layoutParams);
		getActionBar().setElevation(0);
		if(Build.VERSION.SDK_INT>=21) {
			Toolbar parent = (Toolbar) customActionBar.getParent();
			parent.setContentInsetsAbsolute(0, 0);
		}
		
		bdInfo = findViewById(R.id.bdInfo);
		effectiveStatus = findViewById(R.id.effectiveStatus);
		clearLayout = findViewById(R.id.clearLayout);
		clearLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					bdInfo.clearFocus();
				}
			});
		copyButton = findViewById(R.id.copyButton);
		openButton = findViewById(R.id.openButton);
		shareButton = findViewById(R.id.shareButton);
		webview1 = findViewById(R.id.search_detailsWebView);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				if(isFinish) {
					
				} else {
					super.onPageFinished(view, url);
					getBdUrl();
					isFinish = true;
				}
			}
		});
		/*webview1.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webview1.setWebViewClient(new MyWebViewClient());*/
		copyButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(bdBaseUri.length()!=0) {
						ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						clip.setText(bdBaseUri);
						if(System.currentTimeMillis()-backTempTime2<2000) {

						}else {
							Toast a = Toasty.success(currentContext,getString(R.string.copy_success),Toast.LENGTH_SHORT,true);
							a.setGravity(Gravity.TOP,0,150);
							a.show();
						}
						backTempTime2 = System.currentTimeMillis();
					}
				}
			});
		openButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(bdBaseUri.length()!=0) {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(bdBaseUri));
							startActivity(intent);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			});
		openButton.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					Uri uri;
					if(bdBaseUri.startsWith("https://pan.baidu.com/s/1")
					   ||bdBaseUri.startsWith("http://pan.baidu.com/s/1")
					   ||bdBaseUri.startsWith("https://yun.baidu.com/s/1")
					   ||bdBaseUri.startsWith("http://yun.baidu.com/s/1")
					   ||bdBaseUri.startsWith("https://pan.baidu.com/share/init?surl=")
					   ||bdBaseUri.startsWith("https://pan.baidu.com/share/link?shareid=")) {
						String surl = bdBaseUri.
							replace("https://pan.baidu.com/s/1","").
							replace("http://pan.baidu.com/s/1","").
							replace("https://yun.baidu.com/s/1","").
							replace("http://yun.baidu.com/s/1","").
							replace("https://pan.baidu.com/share/init?surl=","").
							replace("https://pan.baidu.com/share/link?","");
						String openUrl = "bdnetdisk://n/action.SHARE_LINK?surl="+surl;
						
						if(openUrl.startsWith("bdnetdisk://n/action.SHARE_LINK?surl=shareid")) {
							uri = Uri.parse(openUrl.replace("surl=",""));
						} else {
							uri = Uri.parse(openUrl);
						}
						
					} else {
						uri = Uri.parse(bdBaseUri);
					}
					try {
						if(bdBaseUri.startsWith("https://pan.baidu.com/")
						   ||bdBaseUri.startsWith("http://pan.baidu.com/")
						   ||bdBaseUri.startsWith("https://yun.baidu.com/")
						   ||bdBaseUri.startsWith("http://yun.baidu.com/")) {
							   
							if(System.currentTimeMillis()-backTempTime4<2000) {

							}else {
								Toast a = Toasty.success(currentContext,"正在启动百度网盘",Toast.LENGTH_SHORT,true);
								a.setGravity(Gravity.TOP,0,150);
								a.show();
							}
							backTempTime4 = System.currentTimeMillis();
						   }
						
						Intent intent = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(intent);
					}catch(Exception e) {
						if(System.currentTimeMillis()-backTempTime3<2000) {

						}else {
							Toast a = Toasty.warning(currentContext,"未检测到百度网盘APP",Toast.LENGTH_SHORT,true);
							a.setGravity(Gravity.TOP,0,150);
							a.show();
						}
						backTempTime3 = System.currentTimeMillis();
					}
					
					return true;
				}
		});
		
		shareButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(shareUri.length()!=0) {
						try {
							Intent sendIntent = new Intent();
							sendIntent.setAction(Intent.ACTION_SEND);
							sendIntent.putExtra(Intent.EXTRA_TEXT,shareUri);
							sendIntent.setType("text/plain");
							startActivity(sendIntent);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		shareButton.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method
					if(shareUri.length()!=0) {
						View view2 = LayoutInflater.from(currentContext).inflate(R.layout.share_edit_dialog,null);
						final EditText shareEdit = view2.findViewById(R.id.dialog_share_edit);
						shareEdit.setText(shareUri);
						Button dialogShareButton = view2.findViewById(R.id.dialog_share_button);
						final Dialog dialog = new AlertDialog.Builder(currentContext)
							.setView(view2)
							.show();
						dialogShareButton.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
									String newShareUri = shareEdit.getText().toString();
									try {
										Intent sendIntent = new Intent();
										sendIntent.setAction(Intent.ACTION_SEND);
										sendIntent.putExtra(Intent.EXTRA_TEXT,newShareUri);
										sendIntent.setType("text/plain");
										startActivity(sendIntent);
										dialog.dismiss();
									} catch(Exception e) {
										e.printStackTrace();
									}
								}
							});
					}
					return true;
				}
			});
	}
	
	private boolean isEffective(String uri) {
		try
		{
			Document document = Jsoup.connect(uri).get();
			if(document.getElementsByClass("share-error-right").size()!=0||
			   document.getElementsByClass("share-error-left").size()!=0||
			   document.getElementsByClass("error-404").size()!=0) {
				return false;
			} else {
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private void showEffectiveState(ImageView i,boolean b) {
		if(b) {
			i.setImageResource(R.drawable.effective);
		} else {
			i.setImageResource(R.drawable.effective_false);
		}
	}
	
	private void updateNoResult() {
		runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					// TODO: Implement this method
					infoProgressBar.setVisibility(View.GONE);
					bdInfo.setText("获取数据失败");
					effectiveStatus.setImageResource(R.drawable.effective_false);
				}
			});
	}
	
	
}
/*class MyWebViewClient extends WebViewClient{  
	public boolean shouldOverrideUrlLoading(WebView view, String url) {   
		view.loadUrl(url);   
		return true;   
	}  
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		Log.d("WebView","onPageStarted");
		super.onPageStarted(view, url, favicon);
	}    
	public void onPageFinished(WebView view, String url) {
		System.out.println("----------");
		Log.d("WebView","onPageFinished ");
		view.loadUrl("javascript:window.local_obj.showSource('<head>'+"+"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		super.onPageFinished(view, url);
	}
}

class InJavaScriptLocalObj {
	@JavascriptInterface
	public void showSource(String html) {
		html = html.replace("<html>","<html>\n").replace("</html>","\n</html>");
		Log.d("HTML", html);
	}
}*/

