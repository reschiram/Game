package game.map;

import java.awt.image.BufferedImage;

import Data.Image.Sprite;
import Data.Image.SpriteSheet;
import data.MapResource;
import sprites.Sprites;

public class MapGenerator {
	
	private interface StructureWork {	
		void generate(int x, int groundlayer, int seed, int[][][] ground, int[][][] build);
	}
	
	private static int WIDTH = 200;
	private static int HEIGHT = 200;
	
	private static double DEFAULTGROUNDLEVEL = 0.15;
	private static StructureWork[] STRUCTURES;
	
	public MapGenerator(){
		SpriteSheet sheet = Sprites.MapGenerations.getSpriteSheet();
		STRUCTURES = new StructureWork[sheet.getSpriteAmount()];	
		for(int i = 0; i<sheet.getSpriteAmount(); i++){
			Sprite sprite = sheet.getSprite(i);
			STRUCTURES[i] = new StructureWork() {
				BufferedImage bi = sprite.getImage(0);
				@Override
				public void generate(int x, int groundlevel, int seed, int[][][] ground, int[][][] build) {
					for(int px = 0; px<20; px++){
						for(int y = 0; y<21; y++){
							if(ground.length>x+px && ground[x].length>groundlevel+21-y-10){
								if(bi.getRGB(px, y)!= 0 && y<=11 && build[x+px][groundlevel+21-y-10][0]!=0){
									ground[x+px][groundlevel+21-y-10][0] = MapResource.Air_Background.getID();
									build [x+px][groundlevel+21-y-10][0] = 0;
								}else if(bi.getRGB(px, y)!= 0 && y>=11 && build[x+px][groundlevel+21-y-10][0]==0){
									ground[x+px][groundlevel+21-y-10][0] = MapResource.Dirt_Background.getID();
									build [x+px][groundlevel+21-y-10][0] = MapResource.Dirt.getID();								
								}
							}
						}
					}
				}
			};
		}
	}
	
	public Map generateMap(int seed){
		int[] seedArray = new int[(seed+"").length()];
		for(int i = 0; i<seedArray.length; i++)seedArray[i] = Integer.parseInt((seed+"").charAt(i)+"");
		
		int[][][] ground = new int[WIDTH][HEIGHT][2];
		int[][][] build = new int[WIDTH][HEIGHT][2];
		
		double groundLevelp = (double)(Integer.parseInt((seed+"").substring(0, 3)))/(double)WIDTH;
		while(groundLevelp/DEFAULTGROUNDLEVEL>1.2)groundLevelp/=((seedArray[0]+1.0)/seedArray[0]);
		while(groundLevelp/DEFAULTGROUNDLEVEL<0.8)groundLevelp*=((seedArray[0]+1.0)/seedArray[0]);
		int groundlevel = (int) (groundLevelp*HEIGHT);
		System.out.println(groundlevel);
		
		for(int x = 0; x<ground.length; x++){
			for(int y = 0; y<ground[x].length; y++){
				if(y<groundlevel){
					ground[x][y][0] = MapResource.Air_Background.getID();
				}else{
					setBlock(x, y, ground, build);
				}
			}
		}
		
		int id = seed;
		for(int x = 0; x<WIDTH;x++){
			id = 1+((id+1)*3*21/(17*x+1));
			while((id+"").length()<4)id*=(x+11);
			id = Integer.parseInt((id+"").substring(1, 4));
			int structure = getID(id);
			STRUCTURES[structure].generate(x, groundlevel, seed, ground, build);
		}
		
		//TODO
		smooth(ground, build);
		smooth(ground, build);
		smooth(ground, build);
		
		
		int stonelevel = Integer.parseInt((seed+"").charAt(0)+"");
		if(stonelevel<5)stonelevel = 5;
		
		for(int x = 0; x<ground.length; x++){
			int grass = -1;
			for(int y = 0; y<ground[x].length; y++){
				if(build[x][y][0]==MapResource.Dirt.getID() && grass==-1 && ground[x][y][0] != MapResource.Air_Background.getID()){
//					int mx = x+1;
//					if(mx>=WIDTH)mx-=WIDTH;
//					boolean right = false;
//					boolean rigthUP = false;
//					if(build[mx][y  ][0]!=0)right   = true;
//					if(build[mx][y-1][0]!=0)rigthUP = true;
//					mx = x-1;	
//					if(mx<0)mx+=WIDTH;
//					boolean left = false;
//					boolean leftUP = false;
//					if(build[mx][y  ][0]!=0)left   = true;
//					if(build[mx][y-1][0]!=0)leftUP = true;
//					if		( left && right){
//						if(leftUP) 			build[x][y][0]=MapResource.Dirt_Grass_T_LU.getID();
//						else if(rigthUP)	build[x][y][0]=MapResource.Dirt_Grass_T_RU.getID();
//						else 				build[x][y][0]=MapResource.Dirt_Grass_T   .getID();
//					}else if( left &&!right){
//						build[x][y][0]=MapResource.Dirt_Grass_TR.getID();
//					}else if(!left && right){
//						build[x][y][0]=MapResource.Dirt_Grass_TL.getID();
//					}else if(!left &&!right)build[x][y][0]=MapResource.Dirt_Grass_TLR.getID();
					build [x][y][0]=MapResource.Dirt_Grass.getID();
					ground[x][y][0]=MapResource.Dirt_Grass_Background.getID();
					grass = y;
				}else if(grass!=-1){
					if(y<stonelevel+grass)build[x][y][0]  = MapResource.Dirt.getID();
					else build[x][y][0]  = MapResource.Stone.getID();
					ground[x][y][0] = MapResource.Dirt_Background.getID();
				}
			}
		}
		
		return new Map(ground, build, seed);
	}

	private void smooth(int[][][] ground, int[][][] build) {
		int lastGroundBlock = -1;
		for(int x = 0; x<WIDTH; x++){
			boolean found = false;
			for(int y = 0; y<ground[x].length; y++){
				if(build[x][y][0]!=0){
					if(lastGroundBlock==-1){
						lastGroundBlock = y;
						y = ground[x].length;
						found = true;
					}else{
						if(Math.abs(y-lastGroundBlock)>1){
							if(y-lastGroundBlock>0){
								setBlock(x, y-1, ground, build);
								y-=2;
							}else removeBlock(x, y, ground, build);
							if(Math.abs(y-lastGroundBlock)-1<=1){
								if(y-lastGroundBlock+1>0)lastGroundBlock = y-1;
								else lastGroundBlock = y+1;
								y = ground[x].length;
								found = true;
							}
						}else{
							lastGroundBlock = y;
							y = ground[x].length;
							found = true;							
						}
					}
				}
			}
			if(!found)lastGroundBlock=-1;
		}
		lastGroundBlock = -1;
		for(int x = WIDTH-1; x>=-1; x--){
			int mx = x;
			if(x==-1)mx=WIDTH+x;
			boolean found = false;
			for(int y = 0; y<ground[mx].length; y++){
				
			}
			if(!found)lastGroundBlock=-1;
		}
		for(int x = 0; x<WIDTH; x++){
			for(int y = 0; y<ground[x].length; y++){
				if(build[x][y][0]!=0){
					int mx = x+1;
					if(mx>=WIDTH)mx-=WIDTH;
					if(build[mx][y-1][0]!=0){
						mx = x-1;
						if(mx<0)mx+=WIDTH;
						if(build[mx][y-1][0]!=0){
							setBlock(x,  y-1, ground, build);
							y = ground[x].length;
						}
					}
				}
			}
		}
		for(int x = 0; x<WIDTH; x++){
			for(int y = 0; y<ground[x].length; y++){
				if(build[x][y][0]!=0){
					int mx = x+1;
					if(mx>=WIDTH)mx-=WIDTH;
					if(build[mx][y][0]==0){
						mx = x-1;
						if(mx<0)mx+=WIDTH;
						if(build[mx][y][0]==0){
							if(build[mx][y+1][0]!=0){
//								mx = x-2;
//								if(mx<0)mx+=WIDTH;
//								if(build[mx][y+1][0]!=0){
									mx = x+1;
									if(mx>=WIDTH)mx-=WIDTH;
									if(build[mx][y+1][0]!=0){
//										mx = x+2;
//										if(mx>=WIDTH)mx-=WIDTH;	
//										if(build[mx][y+1][0]!=0){
											removeBlock(x, y, ground, build);
											y = ground[x].length;
										}
									}
								}
							}
						}
					}
				}
			
		
	}

	
	
	private void removeBlock(int x, int y, int[][][] ground, int[][][] build) {
		ground[x][y][0] = MapResource.Air_Background.getID();
		build [x][y][0] = 0;
	}

	private void setBlock(int x, int y, int[][][] ground, int[][][] build) {
		ground[x][y][0] = MapResource.Dirt_Background.getID();
		build [x][y][0] = MapResource.Dirt.getID();
	}

	private int getID(int seedPart) {
		String max = ".0";
		for(int i = 0; i<(seedPart+"").length(); i++)max="9"+max;
		double percent = (double)seedPart/Double.parseDouble(max);
		if(percent>=1.0)percent-=0.001;
		return (int) (percent*STRUCTURES.length);
	}

}
