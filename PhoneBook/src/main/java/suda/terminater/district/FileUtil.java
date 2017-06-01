package suda.terminater.district;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.os.Handler;

public class FileUtil {
	public static final String PATH = Environment.getExternalStorageDirectory()
			.toString() + "/tel/databases/";

	public static void save(InputStream in,InputStream txt,String name,Handler handler) {
		try {
			File file = new File(PATH + name);
			//Log.i("life", file.getAbsolutePath()+"  "+file.exists()+"");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(PATH + name);
				byte[] bytes = new byte[1024];
				int len = -1;
				
				File file_txt=new File(PATH+"/region.txt");
				file_txt.createNewFile();
				FileOutputStream fosregion=new FileOutputStream(PATH+"/region.txt");
				handler.sendEmptyMessage(1);
				while((len = in.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
				}
				fos.close();
				len=-1;
				while((len=txt.read(bytes))!=-1){
					fosregion.write(bytes, 0, len);
				}
				fosregion.close();
				handler.sendEmptyMessage(2);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
