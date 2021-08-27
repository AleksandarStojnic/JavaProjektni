package mapa;

import java.util.concurrent.locks.ReentrantLock;

public class Mapa {
	
	private Object[][] mapaObjekata; // 15 do 30 velicina odredjuje se slucajno pri kreiranju
	public static ReentrantLock mapLock = new ReentrantLock();
	int mapSize = 30;
	
	public Mapa() {
		mapaObjekata = new Object[mapSize][mapSize];
		//Ovde dodaj stanice ako budu trebale
	}
	
	public void lockMap() {
		mapLock.lock();
	}
	
	public void unlockMap() {
		if(mapLock.isHeldByCurrentThread()) {
			mapLock.unlock();
		}
	}
	
	public int getMapSize() {
		return mapaObjekata.length;
	}
	
	public Object[][] getMapa(){
		return mapaObjekata;
	}
	
	public void setMapaXY(int x, int y, Object obj) {
		if(x < 0 || y < 0 || x >= getMapSize() || y >= getMapSize()) {
			return;
		}
		mapaObjekata[x][y] = obj;
	}
	
	public Object getMapaXY(int x, int y) {
		if(x < 0 || y < 0 || x >= getMapSize() || y >= getMapSize()) {
			return null;
		}
		return mapaObjekata[x][y];
	}
}
