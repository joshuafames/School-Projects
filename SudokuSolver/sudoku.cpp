#include<iostream>
#include<fstream>
#include<vector>
#include<bitset>
#include<algorithm>

using namespace std;
//using namespace std::chrono;
const int N = 9;
const int N2 =3;
int emptycells;
int backtracks;
bitset<N> sectors[N2][N2];

void pvPrint(bitset<N> mygrid[N][N]) {
    for (int i = 0 ; i < N; i++){
        for (int j = 0 ; j < N; j++){
            cout << mygrid[i][j]<< " ";
        }
        cout << endl;
    }
}
void svPrint(string mygrid[3][N]) {
    for (int i = 0 ; i < 3; i++){
        for (int j = 0 ; j < N; j++){
            cout << mygrid[i][j]<< " ";
        }
        cout << endl;
    }
}
void gridPrint(int mygrid[N][N]) {
    for (int i = 0 ; i < N; i++){
        for (int j = 0 ; j < N; j++){
        	if(j==0){
        		cout << mygrid[i][j];
			}else{
				cout<<" "<< mygrid[i][j];
			}
            
        }
        cout << endl;
    }
}

void removeInvalid(bitset<N>pV[N][N], int posx, int posy, int val){
	for(int i = 0; i<N; i++){
		pV[i][posy].reset(val-1); 
		pV[posx][i].reset(val-1);
	}
	pV[posx][posy].reset();/////CHECK IF THE PREVIOUSLY WOKING FUNCTION NOW GIVE ERRORS
	int xquad = posx/3 * 3;
	int yquad = posy/3 * 3;
	for(int i = xquad; i<xquad+3; i++){
		for(int j = yquad; j<yquad+3; j++){
			pV[i][j].reset(val-1);
		}
	}
}

void removeSV(bitset<N>pV[N][N], int row, int col, string SV[3][N]){
	string ress;
	
	for(int i = 0; i < N ; i++){
		char k = SV[0][row][i] - pV[row][col][N-1-i];
		ress = ress + k;
		
	}
	SV[0][row] = ress;
}

bool isvalid(int theGrid[N][N], int row, int col, int num, int gridsize){
	
	int secX = 3*(row/3);
	int secY = 3*(col/3);
	for(int i = secX; i < secX+3; i++){
		for(int j = secY; j<secY+3 ; j++){
			if(num == theGrid[i][j]){
				//cout << "wrong house"<<endl;
				return false;
				
			}
		}
	}
	
	for(int i = 0; i < 9; i++){
		if(theGrid[i][col] == num){
			//cout << "wrong row"<<endl;
			return false;
			
		}
		if(theGrid[row][i] == num){
			//cout << "wrong column"<<endl;
			return false;
			
		}
	}
	return true;
}

class target{
	public:
		int row,col, numPVs;
		bitset<9> bits;
		target();
		target(int row, int col, int nums, bitset<N> bits){
			this->row = row;
			this->col =col;
			this->numPVs = nums;
			this->bits = bits;
		}
		
		vector<int> PVs(){
			vector<int> temp;
			for(int i = 0 ; i < N; i++){
				if(bits.test(i)){
					temp.push_back(i+1);
				}
			}
			return temp;
		}
		void print(){
			cout << "[" << row << "," << col << "] -> has " << numPVs << " choices"<<endl; 
		}
};



bool FindTarget(int grid[N][N], int& row, int& col)
{
    for (row = 0; row < N; row++){
        for (col = 0; col < N; col++){
            if (grid[row][col] == 0){
                return true;
		    }
		}
    }
    return false;
}
int loops, valids;
bool solve(int theGrid[N][N], vector<target> mystack){
	
//	if(!FindTarget(theGrid, row, col)){
//		return true;
//	}

	cout << mystack.size() << " is the size of the stack" <<endl;
	int row, col;
	if(emptycells = 0){
		return true;
	}
	row = mystack.back().row;
	col = mystack.back().col;
	vector<int> pvs = mystack.back().PVs();
	mystack.pop_back();
	
	for(int i = 1 ; i < 10 ; i++){
		loops++;
		if(mystack.size() == 0){
			break;
		}
		cout<<"Maybe i =" << i << "\t for [" << row << "," << col << "]" <<endl;
		if (isvalid(theGrid, row, col, i, N)){
			cout << "\t CHECKS OUT ;>" << endl;
			valids++;
			theGrid[row][col] = i;
			emptycells--;
			if(solve(theGrid, mystack)){
				return true;
			}
			emptycells++;
			backtracks++;
			theGrid[row][col] = 0;
		}
	}
	cout << "loop ended none worked!!"<<endl;
	return false;
	
}


bool normalBruteForce(int grid[N][N]){
	int row, col;
	if(!FindTarget(grid, row, col)){
		return true;
	}
	
	for(int entry = 1 ; entry < N+1; entry++){
		
		if(isvalid(grid, row, col, entry, N)){
			grid[row][col] = entry;
			emptycells--;
			if(normalBruteForce(grid)){
				return true;
			}
			emptycells++;
			grid[row][col] = 0;
			backtracks++;
		}
	}
	return false;
}


///FOR CONVERTING BACK TO INT, mibitstring.test(pos) if true then cout its POS + 1
int main(){
	///auto start = high_resolution_clock::now();
	vector<pair<int, int>> coords;
	vector<pair<int, int>> cants;
	int theGrid[N][N];
	int gridsize = N;
	vector<target> mystack;
	bitset<9> possibleValues[N][N];
	string svHouse[3][N]; ///SPECIAL VALUE EACH ROW, EACH COL, EACH BLOCK BY XOR
	
	ifstream iFile;
	iFile.open("Input.txt");
	for(int i = 0; i < gridsize;i++){
		for(int j = 0; j < gridsize; j++){
			sectors[i/N2][j/N2].set();
			iFile >> theGrid[i][j]; ///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SWITCH BACK TO CIN
			//cin >> theGrid[i][j];
			if(theGrid[i][j] != 0){
				cants.push_back(make_pair(i,j));
			}else{
				emptycells++;
				possibleValues[i][j].set();
				coords.push_back(make_pair(i,j));
			}
		}
	}
	
	//IDENTIFYING POSSIBLILITIES
	for(auto lmnt : cants){
		//cout << "["<<lmnt.first << "," << lmnt.second<< "] = " << theGrid[lmnt.first][lmnt.second] <<endl;
		removeInvalid(possibleValues, lmnt.first, lmnt.second ,theGrid[lmnt.first][lmnt.second]);
	}
	//NAKED SINGLES
	
	bool ifandonlyif = false;
	do{
		for(auto lmnt : coords){
			ifandonlyif = false;
			if(possibleValues[lmnt.first][lmnt.second].count() == 1){
				int actualValue;
				for(int i = 0 ; i < N; i++){
					if(possibleValues[lmnt.first][lmnt.second].test(i)){
						actualValue = i+1;
						break;
					}
				}
				theGrid[lmnt.first][lmnt.second] = actualValue;
				emptycells--;
				removeInvalid(possibleValues, lmnt.first, lmnt.second ,actualValue);
			}
		}
		for(auto lmnt : coords){
			if(possibleValues[lmnt.first][lmnt.second].count() == 1){
				ifandonlyif = true;
				break;
			}
		}
	}while(ifandonlyif);
	
	if(emptycells != 0){
		for(int i = 0 ; i < N ; i++){
			string temp = "@@@@@@@@@";
			string tempCol = "@@@@@@@@@";
			string tempHouse = "@@@@@@@@@";
			for(int j = 0; j < N ; j++){
				for(int k=0; k<N ; k++){
					if(possibleValues[i][j].test(k)){
						temp[k] = temp[k] + 1; 
					}
					if(possibleValues[j][i].test(k)){
						tempCol[k] = tempCol[k] + 1; 
					}
				}
			}
			svHouse[0][i] = temp;
			svHouse[1][i] = tempCol;
		}
	
		bool confirmEmpty = false;
		do{
			for(int r = 0; r < 2;r++){
				for(int i = 0; i<N; i++){
					bool iff;
					size_t found = svHouse[r][i].find("A");
					do{
						iff = false;
						confirmEmpty = false;
						if(found!=string::npos){
							for(int j = 0 ; j<N ; j++){
								int row, col;
								if(r==1){
									row = j;
									col = i;
								}else{
									row = i;
									col = j;
								}
								if(possibleValues[row][col].test(found)){
									theGrid[row][col] = found + 1;
									confirmEmpty = true;
									emptycells--;
									//cout << "coords [" << row << "," << col << "] --> "<< found+1 <<endl;
									removeInvalid(possibleValues, row, col ,found+1);
									for(int p = 0 ; p < N ; p++){
										string temp = "@@@@@@@@@";
										string tempCol = "@@@@@@@@@";
										for(int u = 0; u < N ; u++){
											for(int ko=0; ko<N ; ko++){
												if(possibleValues[p][u].test(ko)){
													temp[ko] = temp[ko] + 1; 
												}
												if(possibleValues[u][p].test(ko)){
													tempCol[ko] = tempCol[ko] + 1; 
												}
											}
											//svHouse[2][cell] = temp;
										}
										svHouse[0][p] = temp;
										svHouse[1][p] = tempCol;
									}
									
									//cout << r<<":"<<i<<">>>ress = "<< ress<<endl;
											
									break;
								}
							}
						}
						found = svHouse[r][i].find("A", found);
						
						if(found!=string::npos){
							iff = true;
						}
					}while(iff);			
				}
			}
		}while(confirmEmpty);
	}
		
	gridPrint(theGrid);
	for(int i = 0; i<N; i++){
		for(int j = 0 ; j < N ; j++){
			int targetROW = i/N2;
			int targetCOL = j/N2;
			if(theGrid[i][j] != 0){
				sectors[targetROW][targetCOL].reset(theGrid[i][j]-1);
			}
		}
	}
	// for(auto i : sectors){
	// 	for(int k = 0 ; k < N2;k++){
	// 		cout << i[k] << " ";
	// 	}
	// 	cout <<endl;
	// }
	// cout <<endl;

	// pvPrint(possibleValues);
	
	//solve(theGrid, mystack);
//	normalBruteForce(theGrid);
    cout << "================ RESULT ================" << endl;
	if(normalBruteForce(theGrid)){
		gridPrint(theGrid);
	}else{
		cout <<"No Possible Solution" <<endl;
	}
	
	
		
	return 0;
}

