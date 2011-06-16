package org.ebayopensource.turmeric.eclipse.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {
	
	
	
	public void extract(String zipFileLocation,String dest){
		
		File temp = new File(dest);
		FileOutputStream os = null;
		temp.mkdirs();
		FileInputStream zipfile = null;
		try {
			zipfile = new FileInputStream(zipFileLocation);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		try {
		ZipInputStream instream = new ZipInputStream(zipfile);
		ZipEntry entry = instream.getNextEntry();

		
			while(instream.available() != 0 ) {
				byte [] b = new byte[1024];
				int i = 0;
				
				
				int index = entry.getName().lastIndexOf("/");
				if(! entry.isDirectory()) {
					
				
							if(index != -1){
								String dir = entry.getName().substring(0,index);
								String filename = entry.getName().substring(index +1);
								File createDir = new File(temp,dir);
								createDir.mkdirs();
								File file = new File(createDir,filename);
								file.createNewFile();
								os = new FileOutputStream(file);
								
							}else{
								File file = new File(temp,entry.getName());
								file.createNewFile();
								os = new FileOutputStream(file);
								
							}
				
					
				
							while(i != -1){
								
								i = instream.read(b, 0, 1024);
								
								if(i > -1)
									os.write(b,0,i);;
									
							}
				}else{
					File file = new File(temp,entry.getName());
					if(!file.exists()){
						file.mkdirs();
					}
				}
				
				instream.closeEntry();
				entry = instream.getNextEntry();
				if(os!= null)
				os.close();		
						
					
			}
			if(instream!= null)
			instream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	

}
