package mapa;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
 
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FileWatcher extends Thread {

	String path;
	
	public FileWatcher (String path) {
		this.path=path;
	}
	
	public void run() {
		 try {
	            WatchService watcher = FileSystems.getDefault().newWatchService();
	            Path dir = Paths.get(path);
	            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	             
	             
	            while (true) {
	                WatchKey key;
	                try {
	               
	                    key = watcher.take();
	                } catch (InterruptedException ex) {
	                    return;
	                }
	              if (key!=null) {
	            	  
	            	  try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE,e.toString());
					}
	 
	                for (WatchEvent<?> event : key.pollEvents()) {
	                    // get event type
	                    WatchEvent.Kind<?> kind = event.kind();
	             
	                    // get file name
	                    @SuppressWarnings("unchecked")
	                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
	                    Path fileName = ev.context();
	             
	                    System.out.println(kind.name() + ": " + fileName.getFileName());
	             
	                    if (kind == OVERFLOW) {
	                        continue;
	                    } else if (kind == ENTRY_CREATE) {
	             
	                       
	                 
	             
	                    } else if (kind == ENTRY_DELETE) {
	             
	                        // process delete event
	             
	                    } else if (kind == ENTRY_MODIFY) {
	             
	  
	                    	   Simulacija.voz=true;
	                    	   Simulacija.config=true;
	             
	                    }
	                }
	              
	              
	                boolean valid = key.reset();
	                if (!valid) {
	                    break;
	                }
	               
	              }
	              break;
	            }
	             
	        } catch (IOException e) {
	        	Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE,e.toString());;
	        }
	    }
	}

