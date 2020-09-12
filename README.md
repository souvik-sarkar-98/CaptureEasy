# CaptureEasy
Hi

@Update
	public String getInstaller(String name) throws IOException {
	    String p1= System.getProperty("java.class.path");
	    System.out.println(p1);
		JarFile jarfile = new JarFile(new File(p1));
	    Enumeration<JarEntry> enu= jarfile.entries();
	    File fl=null;
	    while(enu.hasMoreElements())
	    {
	        JarEntry je = enu.nextElement();
		    System.out.println(je.getName());

	        if(je.getName().equalsIgnoreCase(name)){
		        fl = new File(tempFolderPath, je.getName());
			    System.out.println("Matched===="+fl.getAbsolutePath());
			    System.out.println("exists===="+fl.exists());

		        if(!fl.exists())
		        {
		            fl.getParentFile().mkdirs();
		            fl = new File(tempFolderPath, je.getName());
		        }
		        if(je.isDirectory())
		            continue;
		        java.io.InputStream is = jarfile.getInputStream(je);
		        java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
		        while(is.available()>0)
		        {
		            fo.write(is.read());
		        }
		        fo.close();
		        is.close();
			    System.out.println("ok====");

		    }
	    }
	    jarfile.close();
	    if(fl!=null && fl.exists())
	    	return fl.getAbsolutePath();
	    else
	    	return null;
	  }
