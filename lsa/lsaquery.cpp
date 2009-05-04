#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <strstream>
using namespace std;

#include "lsaquery.h"

typedef map <string, int> MapType;
MapType SharedVocabMap;
MapType VocabTmpMap;
MapType::iterator mit;

double * A;
double * B;
double * ProjA;
double * ProjB;
	

double * ProjMatrix;
int ProjMatRowNum;
int ProjMatColNum;


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

double ProjRet(int Row, int Col)
{
	return ProjMatrix[Row*ProjMatColNum+Col];
}



bool LoadProjMatrix(char* filename)
{
	ifstream fin;
	fin.open(filename,ios::binary|ios::in);
	if(!fin.good())
	{
		return false;
	}
	
	fin.read((char*)&ProjMatRowNum,sizeof(int));
	fin.read((char*)&ProjMatColNum,sizeof(int));
	
	ProjMatrix = new double[ProjMatRowNum*ProjMatColNum];
	
	for(int i=0; i<ProjMatRowNum; i++)
	 for(int j=0; j<ProjMatColNum; j++)
	  fin.read((char*)&(ProjMatrix[i*ProjMatColNum+j]),sizeof(double));
	fin.close();
	
	A = new double[ProjMatColNum];
	B = new double[ProjMatColNum];
	ProjA = new double[ProjMatRowNum];
	ProjB = new double[ProjMatRowNum];
	
	//printf("ProjMatrixRowNum: %d\n",ProjMatRowNum);
	//printf("ProjMatrixColNum: %d\n",ProjMatColNum);
	
	return true;
}

bool LoadVocabulary(char * filename)
{
	ifstream fin;
	fin.open(filename,ios::in);
	if(!fin.good())
	{
		return false;
	}

	SharedVocabMap.clear();
	int windx = 0;
	string str;
	while(fin>>str)
	{
		SharedVocabMap[str] = windx;		
		windx++;
	}

	fin.close();	
	return true;
	
}


double LSAsimilarity(const char * fstr1, const char * fstr2)
{

	istrstream strin1(fstr1);
	istrstream strin2(fstr2);

	string str;
	memset(A,0,ProjMatColNum*sizeof(double));
	memset(B,0,ProjMatColNum*sizeof(double));
	memset(ProjA,0,ProjMatRowNum*sizeof(double));
	memset(ProjB,0,ProjMatRowNum*sizeof(double));	
	
	VocabTmpMap.clear();
	while(strin1>>str)
	{
		if(!is_inmap(str,SharedVocabMap))continue;
		insert_key(str,VocabTmpMap);		
	}
	for(mit =  VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		A[SharedVocabMap[mit->first]]=mit->second;
	}
	
	VocabTmpMap.clear();
	while(strin2>>str)
	{
		if(!is_inmap(str,SharedVocabMap))continue;
		insert_key(str,VocabTmpMap);		
	}
	for(mit =  VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		B[SharedVocabMap[mit->first]]=mit->second;
	}
	    
	for(int i=0; i<ProjMatRowNum; i++)
	{
		double dtmpa=0;
		double dtmpb=0;
		
		for(int j=0; j<ProjMatColNum; j++)
		{
			dtmpa+=ProjRet(i,j)*A[j];
			dtmpb+=ProjRet(i,j)*B[j];
		}
		ProjA[i]=dtmpa;
		ProjB[i]=dtmpb;
	}
	
	double magA = 0;
	double magB = 0;
	double dotP = 0;
	
	for(int i=0; i<ProjMatRowNum; i++)
	{
		magA+= ProjA[i]*ProjA[i];
		magB+= ProjB[i]*ProjB[i];
		dotP+= ProjA[i]*ProjB[i];
	}
	
	//printf("magA: %f magB: %f dotP: %f\n",magA,magB,dotP);
	
		
	if(magA>-0.000000001 && magA<0.000000001)return 0;
	if(magB>-0.000000001 && magB<0.000000001)return 0;
	
	return 1-acos(dotP/(sqrt(magA)*sqrt(magB)))/M_PI;	


}

double COSsimilarity(const char * fstr1, const char * fstr2)
{
	//printf("f1: %s\n",fstr1);
	//printf("f2: %s\n",fstr2);
	istrstream strin1(fstr1);
	istrstream strin2(fstr2);

	string str;
	memset(A,0,ProjMatColNum*sizeof(double));
	memset(B,0,ProjMatColNum*sizeof(double));
	
	VocabTmpMap.clear();
	while(strin1>>str)
	{
		if(!is_inmap(str,SharedVocabMap))continue;
		insert_key(str,VocabTmpMap);		
	}
	for(mit =  VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		A[SharedVocabMap[mit->first]]=mit->second;
	}
	
	/*
	printf("DOC A :\n");
	for(mit = VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		printf("Word: %s Num: %d\n",(mit->first).c_str(),mit->second);
	}	
	*/
	
	VocabTmpMap.clear();
	while(strin2>>str)
	{
		if(!is_inmap(str,SharedVocabMap))continue;
		insert_key(str,VocabTmpMap);		
	}
	for(mit =  VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		B[SharedVocabMap[mit->first]]=mit->second;
	}
	
	/*
	printf("DOC B :\n");
	for(mit = VocabTmpMap.begin();
	    mit != VocabTmpMap.end();
	    mit++)
	{
		printf("Word: %s Num: %d\n",(mit->first).c_str(),mit->second);
	}
  */
	
	double magA = 0;
	double magB = 0;
	double dotP = 0;
	
	for(int i=0; i<ProjMatColNum; i++)
	{
		magA+= A[i]*A[i];
		magB+= B[i]*B[i];
		dotP+= A[i]*B[i];
	}
	
	if(magA>-0.000000001 && magA<0.000000001)return 0;
	if(magB>-0.000000001 && magB<0.000000001)return 0;
	
	return 1-acos(dotP/(sqrt(magA)*sqrt(magB)))/M_PI;	

}


