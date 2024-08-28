import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import za.ac.wits.snake.DevelopmentAgent;

import java.util.Queue;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;

public class MyAgent extends DevelopmentAgent {
	
	public static class Cell {
	     int x, y;
	     int cost; // Total cost to reach this cell
	     int heuristic; // Heuristic cost (e.g., Manhattan distance to goal)
	     Cell parent;

	     Cell(int x, int y) {
	         this.x = x;
	         this.y = y;
	         this.cost = 0;
	         this.heuristic = 0;
	         this.parent = null;
	     }
	 }

	 public static class CellComparator implements Comparator<Cell> {
	     @Override
	     public int compare(Cell a, Cell b) {
	         // Compare cells by their total cost (cost + heuristic)
	         return Integer.compare(a.cost + a.heuristic, b.cost + b.heuristic);
	     }
	 }

    public static void main(String args[]) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }
    
    
    public Stack<int[]> meander(int[][] myBoard, int[] rex, int myID) {
    	//REX IN XY-COORDINATES
    	//scan the 9x9
    	int[] dx = {1, -1, 1, -1};
    	int[] dy = { 1,-1, -1, 1};
    	
    	for(int i = 0; i < 4 ; i++) {
    		int nextX = rex[0] + dx[i];
    		int nextY = rex[1] + dy[i];
    		int[] next = new int[] {nextX,nextY};
    		if(nextX >=0 && nextY>=0 && nextX<49 && nextY<49) {
    			if(myBoard[nextY][nextX] == myID) {
	    			Stack<int[]> res = BFS(myBoard, rex, next);
	    			return res;
	    		}
    		}
	    		
    	}
    	return null;
    }
    
    public Stack<int[]> BFS(int[][] myBoard, int[] rex, int[] apple){
		Queue<int[]> queue = new LinkedList<>();
		int[][] distance = new int[50][50];
		int[][][] parent = new int[50][50][2];
		int[] nullParent = new int[] {-2,-2};
		
		//INITIALIZING THE PARENT ARRAY TO -2,-2 EVERYWHERE
		for(int x=0; x < 50; x++) {
			for(int y=0; y<50; y++) {
				parent[x][y] = nullParent;
				distance[x][y] = -1;
			}
		}
		
		boolean pathFound = false;
		int cCol = rex[0];
		int cRow = rex[1];
		distance[cRow][cCol] = 0;
		
		int appleX = apple[0];
		int appleY = apple[1];
		myBoard[appleY][appleX] = 0;
		
		queue.add(rex);
		while(!queue.isEmpty()) {
			int[] curr = queue.poll();
			if(curr[0]== apple[0] && curr[1]==apple[1]) {
				pathFound = true;
				break;
			}
			int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            for (int i = 0; i < 4; i++) {
            	int neighbourX = curr[0] + dx[i];
            	int neighbourY = curr[1] + dy[i];
            	
            	if(neighbourX < 0 || neighbourY < 0 || neighbourX > 49 || neighbourY > 49) {
            		continue;
            	}
            	
            	//RECALL : ROW-> Y AND COL->X AND CURR IS IN XY-COORDINATES THAT IS CURR[X,Y] = [COL, ROW]
            	if(parent[neighbourY][neighbourX][0] ==-2 && parent[neighbourY][neighbourX][1]==-2 && myBoard[neighbourY][neighbourX] == 0) {
            		int currX = curr[0];
            		int currY = curr[1];
            		
            		distance[neighbourY][neighbourX] = distance[currY][currX] + 1;
	            	parent[neighbourY][neighbourX] = curr;
	            	
	            	int[] neighbour = new int[] {neighbourX, neighbourY};
	            	queue.add(neighbour);
            	}	            	
            }
		}
		
		if(!pathFound) {
			//System.err.println("No way out");
			return null;
			//Call makeSimpleMove
		}else {
			Stack<int[]> path = new Stack<int[]>();
			int[] curr = apple;
			while(curr != rex) {
				path.push(curr);
				int currX = curr[0];
				int currY = curr[1];
				curr = parent[currY][currX];
				//PARENT MATRIX IS IN ROW-COL COORDINATES
			}
			//path.push(rex);
			return path;
			//PATH IS IN XY-COORDINATES;
		}
		
	}

   public int makeSimpleMove(int[] rex, int[] apple, int prevMove) {
	   //WORKS WITH XY-COORDINATES
	   int move = -1;
	   int appleX = apple[0];
	   int appleY = apple[1];
	   if(rex[0] < appleX && prevMove != 2) { //apple is on the right
       	move = 3; 
       }else if(rex[0] > appleX && prevMove != 3){//apple is on the left
       	move = 2;    
       }else if(rex[1] < appleY && prevMove != 0) { //apple is below
       	move = 1;
       }else if(rex[1] > appleY && prevMove != 1) {
       	move = 0;
       }else {
       	if(prevMove == 0 || prevMove == 1) {
       		move = 2;
       	}else {
       		move = 0;
       	}
       }
	   return move;
   }
   
   public void initialiseBoard(int x, int y, int[][] myBoard) {
		for(int i = 0 ; i <x ;i++) {
			for(int k =0 ; k<y;k++) {
				myBoard[i][k] = 0;
			}
		}
	}
	
	public void drawLine(int[][] myBoard, String pointA, String pointB, int snakeNum) {
		String[] pointa = pointA.split(","); 
		String[] pointb = pointB.split(",");
		
		int ax = Integer.parseInt(pointa[0]);
		int ay = Integer.parseInt(pointa[1]);
		
		int bx = Integer.parseInt(pointb[0]);
		int by = Integer.parseInt(pointb[1]);
		int minx,miny,maxx,maxy;
		if(ax>bx) {
			minx = bx;
			maxx = ax;
		}else {
			minx = ax;
			maxx = bx;
		}
		if(ay>by) {
			miny = by;
			maxy = ay;
		}else {
			miny = ay;
			maxy = by;
		}
		
		for(int i = minx; i <= maxx; i++) {
			myBoard[miny][i]=snakeNum;
		}
		for(int i = miny; i<=maxy;i++) {
			myBoard[i][minx]=snakeNum;
		}
		
	}
	
	public void drawSnake(String SnakeState, int snakeNum, int[][] myBoard) {
		String[] SnakeCriticalPoints = SnakeState.split(" ");
		for(int i = 3; i<SnakeCriticalPoints.length -1;i++) {
			drawLine(myBoard, SnakeCriticalPoints[i], SnakeCriticalPoints[i+1], snakeNum);
		}
	}
   
	public static int[] getValidDirections(int[][] grid, int x, int y) {
		int UP = 0;
		int DOWN = 1;
		int LEFT = 2;
		int RIGHT = 3;
		
        int numRows = grid.length;
        int numCols = grid[0].length;
        int[] validDirections = {UP, DOWN, LEFT, RIGHT};

        if (x - 1 < 0 || grid[y][x - 1] != 0) {
            validDirections[LEFT] = -1;
        }

        if (x + 1 >= numCols || grid[y][x + 1] != 0) {
            validDirections[RIGHT] = -1;
        }

        if (y - 1 < 0 || grid[y-1][x] != 0) {
            validDirections[UP] = -1;
        }

        if (y + 1 >= numRows || grid[y+1][x] != 0) {
            validDirections[DOWN] = -1;
        }

        // Filter out eliminated directions
        int countValid = 0;
        for (int dir : validDirections) {
            if (dir != -1) {
                countValid++;
            }
        }

        int[] result = new int[countValid];
        int index = 0;

        for (int dir : validDirections) {
            if (dir != -1) {
                result[index] = dir;
                index++;
            }
        }

        return result;
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            int numObstacles=3;
            
            int prevMove = 0;
            
            int[][] myBoard = new int[50][50];
            
            while (true) {
            	initialiseBoard(50,50, myBoard);
            	
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }
                String apple1 = line;
                //do stuff with apples
                String[] appleCoords = apple1.split("\\s+");
                
                
                int appleX = Integer.parseInt(appleCoords[0]);
                int appleY = Integer.parseInt(appleCoords[1]);
                myBoard[appleY][appleX] = 0;
                int[] apple = new int[] {appleX, appleY};
                
                for (int j=0; j<numObstacles; j++) {
                	String obsLine = br.readLine();
                	String[] obsPoints = obsLine.split(" ");
                	for(String obsPoint : obsPoints) {
                		int pointX = Integer.parseInt(obsPoint.split(",")[0]);
                		int pointY = Integer.parseInt(obsPoint.split(",")[1]);
                		myBoard[pointY][pointX] = 1;
                	}
                }
                
                int[] rex = new int[2];
                int[][] enemyHeads = new int[3][2]; //IN XY-COORDINATES
                int myLength = 0;
                int[] rexTail = new int[2];
                int counter = 0;
                int mySnakeNum = Integer.parseInt(br.readLine());
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    
                    if (i == mySnakeNum) {
                        //hey! That's me :)
                    	String[] myinfo = snakeLine.split(" ");
                    	String[] mySnakeCoords = myinfo[3].split(",");
                    	myLength = Integer.parseInt(myinfo[1]);
                    	
                    	int myinfolength = myinfo.length;
                    	String[] myTail = myinfo[myinfolength-1].split(",");
                    	rexTail[0] = Integer.parseInt(myTail[0]);
                    	rexTail[1] = Integer.parseInt(myTail[1]);
                    	rex[0] = Integer.parseInt(mySnakeCoords[0]);
                    	rex[1] = Integer.parseInt(mySnakeCoords[1]);
                    	
                    }else {
                    	//AVOIDING HEAD ON COLLISIONS
                    	String[] enemyInfo = snakeLine.split(" ");
                    	if(enemyInfo.length>3) {
                    		String[] snakeCoords = enemyInfo[3].split(",");
	                    	int enemyHeadRow = Integer.parseInt(snakeCoords[1]);
	                    	int enemyHeadCol = Integer.parseInt(snakeCoords[0]);
	                    	
	                    	enemyHeads[counter][0] = enemyHeadCol;
	                    	enemyHeads[counter][1] = enemyHeadRow;
	                    	counter++;
	                    	
	                    	int[] dx = {0, 0, 1, -1};
	                        int[] dy = {1, -1, 0, 0};
	                        for(int j = 0; j < 4; j++) {
	                        	int noGoRow = enemyHeadRow + dx[j];
	                        	int noGoCol = enemyHeadCol + dy[j];
	                        	if(noGoRow > -1 && noGoRow < 49 && noGoCol > -1 && noGoCol < 49) {
	                        		myBoard[noGoRow][noGoCol] = i+1;
	                        	}
	                        }
                    	}
	                    	
                    }
                    
                    drawSnake(snakeLine, i+1,myBoard);
                    
                    //do stuff with other snakes
                }
                boolean shortestPath = true;
                
                int myDeltaMoves = Math.abs(rex[0] - apple[0]) + Math.abs(rex[1] - apple[1]);
                if(myLength > 60) {
                	myDeltaMoves +=4;
                }else if(myLength > 80) {
                	myDeltaMoves +=8;
                }
                int closestEnemyDelta = 10000;
                for(int[] enemy : enemyHeads) {
                	int deltaMoves = Math.abs(enemy[0] - apple[0]) + Math.abs(enemy[1] - apple[1]);
                	if(deltaMoves < closestEnemyDelta) {
                		closestEnemyDelta = deltaMoves;
                	}
                	if(deltaMoves < myDeltaMoves) {
                		shortestPath = false;
                	}
                }
                boolean emergency = false;
                
                if(closestEnemyDelta == myDeltaMoves) {
                	emergency = true;
//                	System.err.println("COLLISION ALERT!!");
                }
                
                Stack<int[]> CorrectPath = null;
                int move;
                if(emergency) {
                	CorrectPath  = meander(myBoard, rex, mySnakeNum+1);
                }else if(shortestPath) {
                	CorrectPath = BFS(myBoard, rex, apple); 
                }else {
                	int [] SafePoint = new int[2];
                	if(myLength < 50) {
                		while(true) {
	                		int potentialX = new Random().nextInt(49);
	                		int potentialY = new Random().nextInt(49);
	                		
	                		int deltaMoves = Math.abs(rex[0] - potentialX) + Math.abs(rex[1] - potentialY);
	                		if(deltaMoves > myDeltaMoves && myBoard[potentialY][potentialX] == 0) {
	                			SafePoint[0] = potentialX;
	                			SafePoint[1] = potentialY;
	                			break;
	                		}
	                	}
                	}else {
                		int[] cornersX = {0, 0, 49, 49};
                		int[] cornersY = {0, 49, 0, 49};
                		
                		int metricSum = -1;
                		
                		int optimalCornerIndex = -1;
                		
                		for(int i = 0; i < 4; i++) {
                			int appleCornerMetric = Math.abs(apple[0] - cornersX[i]) + Math.abs(apple[1] - cornersY[1]);
                			int headCornerMetric = Math.abs(rex[0] - cornersX[i]) + Math.abs(rex[1] - cornersY[i]);
                			int tailCornerMetric = Math.abs(rexTail[0] - cornersX[i]) + Math.abs(rexTail[1] - cornersY[i]);
                			int tempMetric = appleCornerMetric + headCornerMetric + tailCornerMetric;
                			
                			if(tempMetric > metricSum) {
                				metricSum = tempMetric;
                				optimalCornerIndex = i;
                			}
                		}
                		if(optimalCornerIndex != -1) {
                			
                			SafePoint[0] = cornersX[optimalCornerIndex];
                			SafePoint[1] = cornersY[optimalCornerIndex];
                		}
                		else {
//                			System.err.println("Starting a while");
                			int mycounter = 0;
                			while(true) {
    	                		int potentialX = new Random().nextInt(49);
    	                		int potentialY = new Random().nextInt(49);
    	                		
    	                		int deltaMoves = Math.abs(rex[0] - potentialX) + Math.abs(rex[1] - potentialY);
    	                		if(deltaMoves > myDeltaMoves && myBoard[potentialY][potentialX] == 0) {
    	                			SafePoint[0] = potentialX;
    	                			SafePoint[1] = potentialY;
    	                			break;
    	                		}
    	                		if(mycounter > 40) {
    	                			
    	                			SafePoint[0] = potentialX;
    	                			SafePoint[1] = potentialY;
    	                			break;
    	                		}
    	                		mycounter++;
    	                	}
//                			System.err.println("While loop closed");
                		}                		
                	}
	                	
                	CorrectPath  = BFS(myBoard, rex, SafePoint);
                	if(CorrectPath==null) {
                		CorrectPath = BFS(myBoard, rex, apple);
                	}
                }
                
                if(CorrectPath != null) {
                	
                	move = makeSimpleMove(rex, CorrectPath.pop(), prevMove);
                    
                }else {
                	int [] SafePoint = new int[2];
//                	System.err.println("Starting a while");
            		while(true) {
                		int potentialX = new Random().nextInt(49);
                		int potentialY = new Random().nextInt(49);
                		
                		if(myBoard[potentialY][potentialX] == 0) {
                			SafePoint[0] = potentialX;
                			SafePoint[1] = potentialY;
                			break;
                		}
                	}
//            		System.err.println("While closed");+
                	Stack<int[]>AlternativePath =BFS(myBoard, rex, SafePoint);
                	if(AlternativePath!= null) {
//                		System.err.println("trying alternative");
                		move = makeSimpleMove(rex, AlternativePath.pop(), prevMove);
//                		System.err.println("end alternative");
                	}else {
                		
                		int[] directions = getValidDirections(myBoard, rex[0], rex[1]);
//                		System.err.println("CODE RED | OPTIONS :"+directions.length);
                		if(directions.length > 0) {
                			int moveIndex = new Random().nextInt(directions.length);
                    		move = directions[moveIndex];
                		}else {
//                			System.err.println("ALL ELSE FAILED");
                			move = 5;
                		}
                		
                	}
                	
                }
                //finished reading, calculate move:
                
                
                prevMove = move;
                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
