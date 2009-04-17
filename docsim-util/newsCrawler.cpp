#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include <iostream>
#include <fstream>
using namespace std;

#include "tinyxml.h"
#include "urlEncode.h"

#include <boost/algorithm/string.hpp>
using namespace boost;

TiXmlDocument * xmldoc;
TiXmlNode * pParent;
TiXmlNode * pChild;
TiXmlNode * pResult;

char oneline[300];
char cmdline[600];

char* preProcess(char *data) {
    // printf("Original data: %s\n",data);
    string str(const_cast<const char *>(data));
    string result = urlencode(str);
    replace_all(result, "'", "\\'");
    const char *temp = result.c_str();
    strcpy(data, temp);
    // printf("Purified data: %s\n",data);
    return data;
}

int
main (int argc, char **argv)
{
    char argch;
  string str;

  fstream file;

    while ((argch = getopt (argc, argv, "i:o:")) != -1)
    {
        switch (argch)
        {
            case 'i':
                 if(optarg==NULL)break;
                 file.open(optarg,ios::in);
                 if(!file)
                 {
                     cout<<"Error: Input file doesn't exist."<<endl;
                     exit(0);
                 }
                 file.close();
                 freopen (optarg,"r",stdin);
                 break;
            case 'o':
                 if(optarg==NULL)break;
                 file.open(optarg,ios::out);
                 if(!file)
                 {
                     cout<<"Error: Can not create output file."<<endl;
                     exit(0);
                 }
                 file.close();
                 freopen (optarg,"w",stdout);
                 break;
        }
    }

  while(fgets(oneline,300,stdin))
  {
    for(int i=0; i<300; i++)
    {
        if(oneline[i]=='\n')
        {
            oneline[i]='\0';
            break;
        }
    }
    if(oneline[0]=='\0')break;

    printf("Query: %s\n",oneline);

    sprintf(cmdline,"wget "\
            "-a wgetlog "\
            "-O news_tmp.txt "\
            "$'http://search.yahooapis.com/NewsSearchService/V1/newsSearch?appid=LZaP.qTV34HXD_XXGJUsTvC5tWGGMT6p8mJedB9Vcxxab_OMQf9u1ykN2_.kJdXu&query=%s&results=5&language=en'",
        preProcess(oneline));
    system(cmdline);

    xmldoc = new TiXmlDocument("news_tmp.txt");
    if(!(xmldoc->LoadFile()))
    {
        printf("Unable to open result news_tmp.txt ! Also see file: wgetlog\n");
        exit(1);
    }

    pParent = xmldoc->FirstChild();
    pParent = pParent->NextSibling();

    TiXmlAttribute* pAttrib=pParent->ToElement()->FirstAttribute();

    while (pAttrib)
      {
        if(!strcmp(pAttrib->Name(),"totalResultsReturned"))
        {
            printf("TotalResult: %s\n",pAttrib->Value());
            break;
        }
            pAttrib=pAttrib->Next();
      }


    for ( pChild = pParent->FirstChild();
          pChild != 0;
          pChild = pChild->NextSibling())
    {


        for(pResult = pChild->FirstChild();
            pResult != 0;
            pResult = pResult->NextSibling())
         {
              int t = pResult->Type();
              char * vname = (char*)pResult->Value();

              if(t==TiXmlNode::ELEMENT && !strcmp(vname,"Title"))
              {
                printf("Title: %s\n",pResult->FirstChild()->Value());
              }

              if(t==TiXmlNode::ELEMENT && !strcmp(vname,"Url"))
              {
                printf("Url: %s\n",pResult->FirstChild()->Value());
              }
         }
    }
    printf("\n");
  }
}


