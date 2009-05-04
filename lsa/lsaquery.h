#ifndef _LSAQUERY
#define _LSAQUERY

bool LoadProjMatrix(char* filename);
bool LoadVocabulary(char * filename);
double LSAsimilarity(const char * fstr1, const char * fstr2);
double COSsimilarity(const char * fstr1, const char * fstr2);

#endif
