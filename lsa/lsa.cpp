#include <stdio.h>
#include <stdlib.h>
#include <math.h>

extern "C"
{
    #ifdef min
    #undef min
    #endif

    #ifdef max
    #undef max
    #endif

    #ifdef small
    #undef small
    #endif

    #include "f2c.h"
    #include "clapack.h"
    #include "blaswrap.h"
}

//#define TEST_SVD

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <strstream>
using namespace std;

typedef map <string, int> MapType;
typedef map <int, int> ConvertMapType;

struct TestDocIDPairType
{
	int id1,id2;
};

vector <TestDocIDPairType> TestDocIDPair;
vector <vector<double> > ProjMat;


int colnum;
int rownum;
int Projrownum;
int Projcolnum;

double *inputM = NULL;
double *inputMbak = NULL;
double *matU = NULL;
double *matV = NULL;
double *s = NULL;

int InDocNum;

MapType TimesVocabMap;
MapType TestVocabMap;
MapType StopWordMap;
MapType SharedVocabMap;
MapType SingleDocWordMap;

ConvertMapType ConvertMap;

MapType::iterator mit;

vector<string> TimesVocabIndex;

ifstream fin;
ofstream fout;
string str;

char cmdstr[300];
char doclistfname[300];

void insert_key(string word, MapType & wmap)
{
	if(wmap.find(word)!=wmap.end())
	{
		wmap[word]++;
		return;
	}	
	wmap[word]=1;	
}

bool is_inmap(string word, MapType & wmap)
{
	return (wmap.find(word)!=wmap.end());
}

bool is_num(string word)
{
	for(int i=0; i<(int)word.size(); i++)
	{
		if(word[i]>='a' && word[i]<='z')return false;
  }
  return true;
}

bool SetInputMatCR(int rownum, int colnum)
{
	inputM = new double[rownum*colnum];
	inputMbak = new double[rownum*colnum];
	if(inputM==NULL)
	{
		printf("Not able to allocate memory for input matrix.\n");
		return false;
	}	
	return true;
}

bool SetInputMatVal(int rowcoord, int colcoord, double val)
{
	if(rowcoord<0 || rowcoord>=rownum)return false;
	if(colcoord<0 || colcoord>=colnum)return false;
		
	   inputM[colcoord*rownum+rowcoord]=val;
	inputMbak[colcoord*rownum+rowcoord]=val;
	return true;
}

void DoSVD()
{
	char     JOBU  = 'A';
	char     JOBVT = 'A';
	integer  M     = rownum;
	integer  N     = colnum;
	integer  LDA   = M;				
	integer  LDU   = M; //select the leading Principal Component
	integer  LDVT  = N;
	integer  INFO;
	
	
	double   *matW;

	integer  lwork;
  
  lwork = 5*M*N;
  matW= new double[lwork];  
  printf("Working Matrix allocated.\n");  
  s = new double[min(M,N)];
  printf("Diagonal Matrix allocated.\n");
  matU = new double[M*M];
  printf("Left Matrix allocated.\n");
  matV = new double[N*N];
  printf("Right Matrix allocated.\n");  
  printf("Running Clapack Routine...\n");
  
  dgesvd_(&JOBU,&JOBVT,&M,&N,inputM,&LDA,s,matU,&LDU,
			    matV,&LDVT,matW,&lwork,&INFO);
			    
	if(INFO!=0)printf("SVD Error INFO: %d\n",INFO);
	else printf("Routine exited Successfully.\n");
		
}

double RetU(int rowcoord, int colcoord)
{	
	return matU[colcoord*rownum+rowcoord];
}

double RetVT(int rowcoord, int colcoord)
{
	return matV[colcoord*colnum+rowcoord];
}

double RetUT(int rowcoord, int colcoord)
{
	return matU[rowcoord*rownum+colcoord];
}

double RetV(int rowcoord, int colcoord)
{
	return matV[rowcoord*colnum+colcoord];
}


double RetS(int rowcoord, int colcoord)
{	
	if(rowcoord==colcoord)return s[rowcoord];
	return 0;
}

double RetA(int rowcoord, int colcoord)
{
	return inputMbak[colcoord*rownum+rowcoord];	
}

void Init()
{
	TimesVocabMap.clear();
  TestVocabMap.clear();
  StopWordMap.clear();
  TimesVocabIndex.clear();
  ConvertMap.clear();
  SharedVocabMap.clear();
  SingleDocWordMap.clear();
  TestDocIDPair.clear();
  ProjMat.clear();
  InDocNum = 0;
  strcpy(doclistfname,"doclist.txt");
}

double CalProjMat(int strowcoord, int utcolcoord, int dim)
{
	double dotP = 0;
	for(int i=0; i<dim; i++)
	{
		dotP+= RetS(strowcoord,i)*RetUT(i,utcolcoord);
	}
	return dotP;
}

double SimTest(int col1, int col2, int dim)
{
	vector<double> VecA;
	vector<double> VecB;
	VecA.clear();
	VecB.clear();
	
	for(int i=0; i<rownum; i++)
	{
		VecA.push_back(RetA(i,col1));
		VecB.push_back(RetA(i,col2));
	}
	
	vector<double>ProjA;
	vector<double>ProjB;
	ProjA.clear();
	ProjB.clear();
	
	for(int i=0; i<Projrownum; i++)
	{
		double dotPA = 0;
		double dotPB = 0;
		for(int j=0; j<Projcolnum; j++)
		{
			dotPA+= ProjMat[i][j]*VecA[j];
			dotPB+= ProjMat[i][j]*VecB[j];
		}
		ProjA.push_back(dotPA);
		ProjB.push_back(dotPB);
	}	
	
	double magA = 0;
	double magB = 0;
	double dotP = 0;
	
	for(int i=0; i<dim; i++)
	{
		magA+= ProjA[i]*ProjA[i];
		magB+= ProjB[i]*ProjB[i];
		dotP+= ProjA[i]*ProjB[i];
	}
	
	return 1-acos(dotP/(sqrt(magA)*sqrt(magB)))/M_PI;
}

double SimBefTest(int col1, int col2)
{
	double magA = 0;
	double magB = 0;
	double dotP = 0;
	
	for(int i=0; i<rownum; i++)
	{
		magA+= RetA(i,col1)*RetA(i,col1);
		magB+= RetA(i,col2)*RetA(i,col2);
		
		//printf("(%f %f) ",RetA(i,col1),RetA(i,col2));
		
		dotP+= RetA(i,col1)*RetA(i,col2);
	}
	
	//printf("\n");
	
	return 1-acos(dotP/(sqrt(magA)*sqrt(magB)))/M_PI;	
}

void ComputeProjMatrix()
{
	Projrownum = colnum;
	Projcolnum = rownum;
	ProjMat.resize(Projrownum);
	
	for(int i=0; i<Projrownum; i++)
	{
		ProjMat[i].clear();
		ProjMat[i].resize(Projcolnum,0);
		for(int j=0; j<Projcolnum; j++)
		{
			double tmp = CalProjMat(i,j,rownum);
			ProjMat[i][j]=tmp;
		}
	}
	
	printf("Projecting Matrix Caculated.\n");
}

int main()
{
	
//Test data for validation. Answer should be:
//U =
//-0.869 -0.494 
//-0.494 0.869 
//S = (23.293 8.090 )
//VT = 
//-0.661 -0.304 -0.686 
//-0.655 -0.213 0.725 
//-0.366 0.929 -0.058 

#ifdef TEST_SVD
	rownum=5;
	colnum=6;
	SetInputMatCR(rownum,colnum);
	SetInputMatVal(0,0,1);
	SetInputMatVal(0,1,2);
	SetInputMatVal(0,2,3);
	SetInputMatVal(0,3,4);
	SetInputMatVal(0,4,5);
	SetInputMatVal(0,5,6);
	SetInputMatVal(1,0,7);
	SetInputMatVal(1,1,8);
	SetInputMatVal(1,2,9);
	SetInputMatVal(1,3,10);
	SetInputMatVal(1,4,11);
	SetInputMatVal(1,5,12);
	SetInputMatVal(2,0,13);
	SetInputMatVal(2,1,14);
	SetInputMatVal(2,2,15);
	SetInputMatVal(2,3,16);
	SetInputMatVal(2,4,17);
	SetInputMatVal(2,5,18);
	SetInputMatVal(3,0,19);
	SetInputMatVal(3,1,20);
	SetInputMatVal(3,2,21);
	SetInputMatVal(3,3,22);
	SetInputMatVal(3,4,23);
	SetInputMatVal(3,5,24);
	SetInputMatVal(4,0,25);
	SetInputMatVal(4,1,26);
	SetInputMatVal(4,2,27);
	SetInputMatVal(4,3,28);
	SetInputMatVal(4,4,29);
	SetInputMatVal(4,5,30);

  for(int i=0; i<rownum; i++)
  {
   for(int j=0; j<colnum; j++)
   {
   	printf("%f ",RetA(i,j));
   }
   printf("\n");
  }
	
	DoSVD();
	
	printf("U =\n");
	for(int i=0; i<rownum; i++)
	{
		for(int j=0; j<rownum; j++)
		 printf("%.10f ",RetU(i,j));
		printf("\n");
  }
  
  printf("S = (");
  for(int i=0; i<min(colnum,rownum); i++)
   printf("%.10f ",s[i]);
  printf(")\n");
  
  printf("VT = \n");
  for(int i=0; i<colnum; i++)
	{
		for(int j=0; j<colnum; j++)
		 printf("%.10f ",RetVT(i,j));
		printf("\n");
  }
  
  ComputeProjMatrix();
  
  for(int i=0; i<colnum; i++)
  {
   for(int j=0; j<rownum; j++)
   {
   	printf("%f ", ProjMat[i][j]);
   }
   printf("\n");
  }
  
  printf("Sime Test (1,2): %f\n",SimTest(0,1,3));
  printf("Sime Test (2,3): %f\n",SimTest(1,2,3));
  printf("Sime Test (1,3): %f\n",SimTest(0,2,3));
  
  exit(0);
  
#endif
	
		
	Init();
	
	// load timesnews vocabulary

	fin.open("cutoffvocab.nytimes.txt");
	int wordinx=0;
	while(fin>>str)
	{
		wordinx++;
		TimesVocabMap[str]=wordinx;
	}
	fin.close();
	
	// load stop words
	fin.open("stop_words");
	while(fin>>str)
	{
		insert_key(str,StopWordMap);		
	}
	fin.close();
	
	// load test vocabulary	
	
	//sprintf(cmdstr,"ls ../Data/lsa/doc/*.html > %s",doclistfname);
	sprintf(cmdstr,"ls ../Data/transdata/2*.en_NOSW ../Data/transdata/2*.tra_NOSW > %s",doclistfname);
	
	if(system(cmdstr))
	{
		printf("Error while running: %s\n",cmdstr);
		exit(1);
	}
	
	ifstream listfin(doclistfname);
	
	if(!listfin.good())
	{
		printf("Can't open file list: %s\n",doclistfname);
		exit(1);
	}
	
	ifstream inputfile;
	
	while(listfin>>str)
	{
		inputfile.open(str.c_str());
		if(!inputfile.good())continue;
		string token;
		while(inputfile>>token)
		{
			if(is_inmap(token, StopWordMap))continue;
			insert_key(token, TestVocabMap);				
		}		
		inputfile.close();
		InDocNum++;
	}
		
	int commoncount=0;
	int windx=0;
				
	for(mit=TimesVocabMap.begin();mit!=TimesVocabMap.end();mit++)
	{
	 if(is_inmap(mit->first, TestVocabMap))
	 {
	  windx++;	 	
	 	ConvertMap[mit->second]=windx;
	 	SharedVocabMap[mit->first]=windx;
	 	commoncount++;
	 	
	 	//printf("%s: %d\n",(mit->first).c_str(),TestVocabMap[mit->first]);
	 }
	}
	
	printf("Stop Word Vocabulary: %d\n",(int)StopWordMap.size());
	printf("Timesnews Vocabulary: %d\n",(int)TimesVocabMap.size());
	printf("     Test Vocabulary: %d\n",(int)TestVocabMap.size());
	printf("   Shared Vocabulary: %d\n",commoncount);
	printf("    Input Doc Number: %d\n",InDocNum);
	
	// read in smalldoc test data		
	
	int TotalDocNum,TimesWordNum;
	fin.open("smalldoc.txt");
	fin>>TotalDocNum;
	colnum = TotalDocNum;
	printf("    Times Doc Number: %d\n",TotalDocNum);
	fin>>TimesWordNum;
	rownum = commoncount;
	
	SetInputMatCR(rownum,colnum);
  memset(inputM,0,rownum*colnum*sizeof(double));
  
  int inDid,oldinDid,Wid,wnum;
  inDid = oldinDid = 0;
  int Did = 0;
  int tmpint;
  
  fin>>tmpint;
  
  while(fin>>inDid>>Wid>>wnum)
  {
  	if(Wid>TimesWordNum)continue;
  	if(ConvertMap.find(Wid)==ConvertMap.end())continue;
  	if(inDid!=oldinDid)
  	{
  		Did++;
  		oldinDid = inDid;
  	}
  	if(Did>TotalDocNum)break;
  	Wid = ConvertMap[Wid];
  	SetInputMatVal(Wid-1,Did-1,wnum);  	  	
  }

	ofstream mtfout("mat.dat");
	
	for(int i=0; i<rownum; i++)
	 for(int j=0; j<colnum; j++)
	  {
	  	if(RetA(i,j)<-0.000000001 || RetA(i,j)>0.000000001)
	  		{
	  			mtfout<<i+1<<" "<<j+1<<" "<<RetA(i,j)<<endl;
	  		}
	  }	  
	mtfout.close();
	printf("Matrix is exported. Row Number: %d Col Number: %d\n",rownum, colnum);
  printf("Computing SVD...\n");
 	DoSVD();
	printf("SVD finished.\n");
	printf("Computing Projecting Matrix...\n");		
  ComputeProjMatrix();
  
  printf("Writing Projecting Matrix to ProjMatrix.dat...\n");

	ofstream ProjMatFout;
	ProjMatFout.open("ProjMatrix.dat",ios::out | ios::binary);
	
	/*	
  Projrownum = colnum;
	Projcolnum = rownum;
	ProjMat.resize(Projrownum);
	*/
	
	ProjMatFout.write((char*)(&Projrownum),sizeof(int));
	ProjMatFout.write((char*)(&Projcolnum),sizeof(int));
	
	for(int i=0; i<Projrownum; i++)
	 for(int j=0; j<Projcolnum; j++)
	 {
	 	double dtmp = ProjMat[i][j];
	 	ProjMatFout.write((char*)(&dtmp),sizeof(double));
	 }
	 
	ProjMatFout.close();
	
	printf("Finished.\n");
	
	vector<string> VocabDic;
	VocabDic.resize(SharedVocabMap.size());
	printf("Size of the Shared Vocab: %d\n", VocabDic.size());
	
	for(mit=SharedVocabMap.begin(); mit!=SharedVocabMap.end(); mit++)
	{
		//cout<<mit->second<<" "<<mit->first<<endl;
		//printf("Term: %s\n Index: %d\n",(*mit).second-1,(*mit).first.c_str());
		VocabDic[mit->second-1]=mit->first;
	}
	
	ofstream DicFout("SharedVocab.txt");
	
	for(int i=0; i<(int)VocabDic.size(); i++)
	{
		DicFout<<VocabDic[i]<<endl;		
	}
	
	DicFout.close();
	
	printf("Shared Vocabulary has been exported to SharedVocab.txt\n");
	
	 	
	delete inputM;
	delete matU;
	delete matV;
	delete s;
}
