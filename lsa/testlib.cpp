#include <iostream>
#include <fstream>
#include <string>
using namespace std;

#include "lsaquery.h"

ifstream fin1,fin2;
string str;
string fstr1,fstr2;
int main()
{
  if(!LoadVocabulary("../Data/ShingleLSA/SharedVocab.txt"))
	{
		printf("Unable to read SharedVocab.txt\n");
		exit(1);
	}
	
	printf("Vocab reading finished.\n");
	
	if(!LoadProjMatrix("../Data/ShingleLSA/ProjMatrix.dat"))
	{
		printf("Unable to read ProjMatrix.dat\n");
		exit(1);
	}
  
	string fname1, fname2;
	cout<<"Please Enter Two File Names: "<<endl;
	while(cin>>fname1>>fname2)
	{
		fin1.open(fname1.c_str());
		fin2.open(fname2.c_str());
		
		if(!fin1.good())
		  printf("Can't open: %s\n",fname1.c_str());
		if(!fin2.good())
			printf("Can't open: %s\n",fname2.c_str());
		
		fstr1 = "";
		fstr2 = "";
		
		while(fin1>>str)fstr1 = fstr1+" "+str;
		while(fin2>>str)fstr2 = fstr2+" "+str;
		
		//printf("f1: %s\n",fstr1.c_str());
		//printf("f2: %s\n",fstr2.c_str());
		
		double result1 = LSAsimilarity(fstr1.c_str(),fstr2.c_str());
		double result2 = COSsimilarity(fstr1.c_str(),fstr2.c_str());
		
		printf("LSA Similarity: %f\n",result1);
		printf("COS Similarity: %f\n",result2);
		
		fin1.close();
		fin2.close();
				
	}
	
}
